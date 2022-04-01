package cn.iocoder.yudao.framework.loader.serializer;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.thread.lock.LockUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.loader.annotation.Load;
import cn.iocoder.yudao.framework.loader.bo.AnnotationsResult;
import cn.iocoder.yudao.framework.loader.handler.params.ParamsHandler;
import cn.iocoder.yudao.framework.loader.handler.rsp.ResponseHandler;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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
     * 成功翻译
     */
    private static final TimedCache<String, Object> success = new TimedCache<>(TimeUnit.SECONDS.toMillis(dataCacheMinutes));

    /**
     * 失败翻译
     */
    private static final TimedCache<String, Object> error = new TimedCache<>(TimeUnit.SECONDS.toMillis(dataCacheMinutes));

    /**
     * 锁避免同时请求同一ID
     */
    private static final TimedCache<String, StampedLock> lockMap = new TimedCache<>(TimeUnit.MINUTES.toMillis(lockCacheMinutes));

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
     * 注解参数处理
     */
    private AnnotationsResult annotationsResult;

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
    }

    public LoadSerializer(String loadService, String method, int cacheSecond, AnnotationsResult annotationsResult, ParamsHandler paramsHandler, ResponseHandler otherResponseHandler) {
        this();
        this.loadServiceSourceClassName = loadService;
        this.loadService = SpringUtil.getBean(loadService);
        this.method = method;
        this.cacheSecond = cacheSecond;
        this.annotationsResult = annotationsResult;
        this.responseHandler = otherResponseHandler;
        this.paramsHandler = paramsHandler;
        prefix = loadServiceSourceClassName;
    }

    @Override
    public void serialize(Object bindData, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 写入原始数据
        gen.writeObject(bindData);
        if (bindData == null || loadService == null) {
            return;
        }

        // 获取要翻译写入的字段
        String writeField = '$' + gen.getOutputContext().getCurrentName();
        Class<?> writeClass = Object.class;
        if (StringUtils.isNotBlank(annotationsResult.getWriteField())) {
            Field field = ReflectUtil.getField(gen.getCurrentValue().getClass(), annotationsResult.getWriteField());
            if (field != null) {
                writeField = annotationsResult.getWriteField();
                writeClass = field.getType();
            }
        }
        gen.writeFieldName(writeField);

        // 获取缓存KEY
        Object[] args = annotationsResult.getRemoteParams();
        String cacheKey = "" + Objects.hash(prefix, method, paramsHandler.getCacheKey(bindData, args));
        Object result = getCacheInfo(cacheKey);
        if (result != null) {
            log.info("{} cache 命中: {}", prefix, result);
            gen.writeObject(result);
            return;
        }

        StampedLock lock = lockMap.get(cacheKey, true, LockUtil::createStampLock);
        // 写锁避免同一业务ID重复查询
        long stamp = lock.writeLock();
        try {
            // 多参数组装
            Object[] objectParams = new Object[args.length + 1];
            objectParams[0] = paramsHandler.handleVal(bindData);
            for (int i = 0; i < args.length; i++) {
                objectParams[i + 1] = args[0];
            }

            // 请求翻译结果
            Object loadResult = ReflectUtil.invoke(loadService, method, objectParams);

            if (loadResult != null) {
                result = this.responseHandler.handle(this.loadServiceSourceClassName, method, loadResult, writeClass, objectParams);
                if (cacheSecond > 0) {
                    success.put(cacheKey, result);
                }
            } else {
                log.error("【{}】 翻译失败，未找到：{}", prefix, bindData);
                error.put(cacheKey, bindData);
                result = bindData;
            }

        } catch (Exception e) {
            log.error("【{}】翻译服务异常：{}", prefix, e);
            error.put(cacheKey, bindData);
            result = bindData;
        } finally {
            lock.unlockWrite(stamp);
        }
        gen.writeObject(result);
    }

    /**
     * 获取厍信息
     *
     * @param cacheKey 缓存的KEY
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
            Class<? extends ParamsHandler> paramsHandlerClass = load.paramsHandler();
            Class<? extends ResponseHandler> responseHandlerClass = load.responseHandler();
            String method = load.method();
            ParamsHandler paramsHandler = paramsHandlerClass.getDeclaredConstructor().newInstance();
            ResponseHandler responseHandler = responseHandlerClass.getDeclaredConstructor().newInstance();
            int cacheSecond = load.cacheSecond();
            // 额外参数处理
            AnnotationsResult annotationsResult = paramsHandler.handleAnnotation(property);
            return new LoadSerializer(bean, method, cacheSecond, annotationsResult, paramsHandler, responseHandler);
        }
        return prov.findNullValueSerializer(null);
    }
}


