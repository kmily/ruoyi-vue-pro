package cn.iocoder.yudao.framework.covert.serializer;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.thread.lock.LockUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.covert.annotation.Load;
import cn.iocoder.yudao.framework.covert.handler.LoadArgsHandler;
import cn.iocoder.yudao.framework.covert.handler.params.ParamsHandler;
import cn.iocoder.yudao.framework.covert.handler.rsp.ResponseHandler;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;

@Slf4j
public class LoadSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    /**
     * 值缓存时间
     */
    private static final int dataCacheMinutes = 5;

    /**
     * 锁缓存时间
     */
    private static final int lockCacheMinutes = 10;

    /**
     * 缓存检测周期，单位秒
     */
    private static final int cacheCheckScheduleTime = 10;

    /**
     * 成功翻译
     */
    private static final TimedCache<String, Object> success = new TimedCache(TimeUnit.MINUTES.toMillis(dataCacheMinutes), new ConcurrentHashMap<>());

    /**
     * 失败翻译
     */
    private static final TimedCache<String, Object> error = new TimedCache(TimeUnit.MINUTES.toMillis(dataCacheMinutes), new ConcurrentHashMap<>());

    /**
     * 锁避免同时请求同一ID
     */
    private static final TimedCache<String, StampedLock> lockMap = new TimedCache<>(TimeUnit.MINUTES.toMillis(lockCacheMinutes), new ConcurrentHashMap<>());

    /**
     * 远程调用服务原始calss
     */
    private String loadServiceSourceClassName;

    /**
     * 远程调用服务
     */
    private Object loadService;

    /**
     * 方法
     */
    private String method;

    /**
     * 缓存时间
     */
    private int cacheSecond;

    /**
     * 额外参数
     */
    private String[] args;

    /**
     * 返回结果处理类
     */
    private ParamsHandler paramsHandler;

    /**
     * 返回结果处理类
     */
    private ResponseHandler responseHandler;

    /**
     * 远程服务前缀
     */
    private String prefix;

    public LoadSerializer() {
        super();
        success.schedulePrune(TimeUnit.SECONDS.toMillis(cacheCheckScheduleTime));
        error.schedulePrune(TimeUnit.SECONDS.toMillis(cacheCheckScheduleTime));
        lockMap.schedulePrune(TimeUnit.SECONDS.toMillis(cacheCheckScheduleTime));

    }

    public LoadSerializer(String loadService, String method, int cacheSecond, String[] args, ParamsHandler paramsHandler, ResponseHandler otherResponseHandler) {
        this.loadServiceSourceClassName = loadService;
        this.loadService = SpringUtil.getBean(loadService);
        this.method = method;
        this.cacheSecond = cacheSecond;
        this.args = args;
        this.responseHandler = otherResponseHandler;
        this.paramsHandler = paramsHandler;
        prefix = loadServiceSourceClassName + "-";
    }

    @Override
    public void serialize(Object bindData, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Object params = paramsHandler.handle(bindData);
        if (params == null || loadService == null) {
            gen.writeObject(bindData);
            return;
        }

        // 有效ID，去查询
        String cacheKey = prefix + method + "-" + paramsHandler.getCacheKey(bindData) + String.join(",", args);
        if (StrUtil.isBlank(cacheKey)) {
            gen.writeObject(bindData);
            return;
        }
        Object result = getCacheInfo(cacheKey);
        if (result == null) {
            StampedLock lock = lockMap.get(cacheKey, true, LockUtil::createStampLock);
            Lock writeLock = lock.asWriteLock();
            try {
                // 获取锁成功后请求这个ID
                writeLock.lock();
                // 再次尝试拿缓存
                result = getCacheInfo(cacheKey);
                if (result == null) {
                    try {
                        // 多参数组装
                        List<Object> objectParams = new ArrayList<>();
                        objectParams.add(params);
                        if (this.args != null && this.args.length > 0) {
                            for (String param : this.args) {
                                objectParams.add(param);
                            }
                        }
                        Object r = ReflectUtil.invoke(loadService, method, objectParams.toArray());
                        if (r != null) {
                            result = this.responseHandler.handle(this.loadServiceSourceClassName, method, r, objectParams.toArray());
                            if (cacheSecond > 0) {
                                success.put(cacheKey, result, TimeUnit.SECONDS.toMillis(cacheSecond));
                            }
                        } else {
                            log.error("【{}】 翻译失败，未找到：{}", prefix, params);
                            error.put(cacheKey, bindData);
                            result = null;
                        }
                    } catch (Exception e) {
                        log.error("【{}】翻译服务异常：{}", prefix, e);
                        error.put(cacheKey, bindData);
                        result = bindData;
                    }
                }
            } finally {
                writeLock.unlock();
            }
        }
        gen.writeObject(result);
    }

    /**
     * 获取厍信息
     *
     * @param cacheKey
     * @return
     */
    private Object getCacheInfo(String cacheKey) {
        Object result = success.get(cacheKey, false);
        if (result == null) {
            result = error.get(cacheKey, false);
        }
        return result;
    }

    @SneakyThrows
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            Load load = property.getAnnotation(Load.class);
            if (load == null) {
                throw new RuntimeException("未注解相关 @Load 注解");
            }
            String bean = load.bean();
            String[] args = load.args();
            Class<? extends ParamsHandler> paramsHandlerClass = load.paramsHandler();
            Class<? extends ResponseHandler> responseHandlerClass = load.responseHandler();
            String method = load.method();
            ParamsHandler paramsHandler = paramsHandlerClass.getDeclaredConstructor().newInstance();
            ResponseHandler responseHandler = responseHandlerClass.getDeclaredConstructor().newInstance();
            int cacheSecond = load.cacheSecond();
            // 额外参数处理
            args = LoadArgsHandler.handle(property, args);
            return new LoadSerializer(bean, method, cacheSecond, args, paramsHandler, responseHandler);
        }
        return prov.findNullValueSerializer(property);
    }
}


