package cn.iocoder.dashboard.framework.validator.custom;

import cn.hutool.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 校验注解处理抽象接口
 * <p>
 * ### 实现类要使用起来需要注册到{@link ValidateHandlerHelper}中 ###
 *
 * @author zzf
 * @date 2021/4/15 9:22
 */
public interface ValidateAnnotationHandler<T extends Annotation> {

    /**
     * 获取实现类的具体的注解类对象
     * @return T
     */
    Class<T> getAnnotation();


    /**
     * 判断参数字段是否存在该注解
     *
     * @param field 字段对象
     * @return 是否存在该注解
     */
    default Boolean isAnnotationPresent(Field field) {
        return field.isAnnotationPresent(getAnnotation());
    }

    /**
     * 校验字段值，如果值合法返回null，不合法返回消息内容
     *
     * @param field        需要校验的字段
     * @param targetObject 字段所在的对象
     * @return 值合法返回null，不合法返回消息内容
     */
    default String validate(Field field, Object targetObject) {
        T annotation = field.getAnnotation(getAnnotation());
        return validate(annotation, ReflectUtil.getFieldValue(targetObject, field));
    }


    /**
     * 校验字段值，如果值合法返回null，不合法返回消息内容
     * <p>
     * 这里之所以没有直接返回boolean而是直接返回msg
     * 是为了适配如{@link javax.validation.constraints.Max}{@link javax.validation.constraints.Min}等
     * 消息中需要获取注解属性值的注解
     *
     * @param validateAnnotation 标记的注解
     * @param fieldValue         字段值
     * @return 如果值合法返回null，不合法返回消息内容
     */
    String validate(T validateAnnotation, Object fieldValue);


}
