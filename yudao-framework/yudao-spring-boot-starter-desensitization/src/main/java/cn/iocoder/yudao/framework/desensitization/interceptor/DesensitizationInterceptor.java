package cn.iocoder.yudao.framework.desensitization.interceptor;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ReflectUtil;
import cn.iocoder.yudao.framework.desensitization.core.annotation.Desensitization;
import cn.iocoder.yudao.framework.desensitization.core.handler.DesensitizationHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.Collection;

/**
 * 脱敏处理拦截器，在查询结果时，对脱敏字段进行脱敏处理
 *
 * @author VampireAchao
 * @since 2022/10/6 11:24
 */
@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
public class DesensitizationInterceptor implements Interceptor {

    /**
     * 主要逻辑
     *
     * @param entity 实体类
     */
    private void processDesensitization(Object entity) {
        for (Field field : ReflectUtil.getFields(entity.getClass())) {
            if (!field.isAnnotationPresent(Desensitization.class)) {
                continue;
            }
            Desensitization desensitization = field.getAnnotation(Desensitization.class);
            Object fieldValue = ReflectUtil.getFieldValue(entity, field);
            Opt.ofBlankAble(fieldValue).map(Object::toString).ifPresent(value -> {
                DesensitizationHandler desensitizationHandler = ReflectUtil.newInstance(desensitization.handler());
                String newValue = desensitizationHandler.apply(value, desensitization);
                ReflectUtil.setFieldValue(entity, field, newValue);
            });
        }
    }

    /**
     * 针对列表查询进行脱敏处理
     *
     * @param invocation invocation
     * @return 脱敏后的结果
     * @throws Throwable 异常
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        if (Collection.class.isAssignableFrom(proceed.getClass())) {
            Collection<?> collection = (Collection<?>) proceed;
            collection.forEach(this::processDesensitization);
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
