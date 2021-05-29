package cn.iocoder.dashboard.framework.validator.custom;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.Contracts;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.validator.internal.util.logging.Messages.MESSAGES;

/**
 * 自定义参数校验器
 * <p>
 * 愿景：实现自动生成验证失败的消息
 * 1、字段名根据{@link ApiModelProperty#value()}注解获取
 * 2、状态根据{@link NotNull}、{@link javax.validation.constraints.NotEmpty}等生成
 * 3、拼接返回消息
 * <p>
 * 如果实现后则以后只需要加{@code @NotNull @NotEmpty}注解，不需要再一个个写{@link NotNull#message()}的值
 * <p>
 * 2021.4.15 此次实现是类似于多数据源的实现，通过{@link ValidatorProxy}代理
 * 默认校验类({@link org.hibernate.validator.internal.engine.ValidatorImpl})和自定义校验类(this)
 * · 该类只处理参数校验{@link this#validateParameters(Object, Method, Object[], Class[])}
 * · 其他校验都交给默认实现
 * · 因此这个类只实现了参数校验的方法
 *
 * @author zzf
 * @date 2021/4/8 14:09
 */
@Slf4j
public class CustomValidator implements Validator, ExecutableValidator {

    @Override
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validate(object, null, new Object[]{object}, groups);
    }

    @SneakyThrows
    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        return null;
    }

    @SneakyThrows
    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        return null;
    }

    @Override
    public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        return null;
    }

    @SneakyThrows
    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }

    @Override
    public ExecutableValidator forExecutables() {
        return this;
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateParameters(T object, Method method, Object[] parameterValues, Class<?>... groups) {
        return validate(object, method, parameterValues, groups);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateReturnValue(T object, Method method, Object returnValue, Class<?>... groups) {
        return null;
    }

    @SneakyThrows
    @Override
    public <T> Set<ConstraintViolation<T>> validateConstructorParameters(Constructor<? extends T> constructor, Object[] parameterValues, Class<?>... groups) {
        return null;
    }

    @SneakyThrows
    @Override
    public <T> Set<ConstraintViolation<T>> validateConstructorReturnValue(Constructor<? extends T> constructor, T createdObject, Class<?>... groups) {
        return null;
    }


    private <T> Set<ConstraintViolation<T>> validate(T object, Executable executable, Object[] parameterValues, Class<?>... groups) {
        try {
            log.warn("Object: {}, Executable: {}, parameterValues: {}, groups: {}",
                    JSON.toJSONString(object),
                    JSON.toJSONString(executable),
                    JSON.toJSONString(parameterValues),
                    JSON.toJSONString(groups)
            );
        } catch (Exception ignore) {
        }

        Set<ConstraintViolation<T>> result = new HashSet<>();
        sanityCheckGroups(groups);
        if (ArrayUtil.isNotEmpty(parameterValues)) {
            for (Object param : parameterValues) {
                Field[] fields = param.getClass().getDeclaredFields();
                for (Field field : fields) {
                    result.addAll(ValidateHandlerHelper.validate(field, param, object, field.getName()));
                }
            }
        }
        return result;
    }

    private void sanityCheckGroups(Class<?>[] groups) {
        Contracts.assertNotNull(groups, MESSAGES.groupMustNotBeNull());
        for (Class<?> clazz : groups) {
            if (clazz == null) {
                throw new IllegalArgumentException(MESSAGES.groupMustNotBeNull());
            }
        }
    }
}
