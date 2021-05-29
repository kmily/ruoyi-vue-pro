package cn.iocoder.dashboard.framework.validator.custom;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 校验处理辅助类
 *
 * @author zzf
 * @date 2021/4/15 9:49
 */
public class ValidateHandlerHelper {

    private static Set<ValidateAnnotationHandler<?>> handlerSet = new HashSet<>(10);

    static {
        ServiceLoader.load(ValidateAnnotationHandler.class).forEach(handlerSet::add);
    }

    /**
     * 校验指定字段值是否合法
     * <p>
     * 这里针对集合类型字段做递归校验
     * 实现深层对象参数值校验
     *
     * @param field        字段对象
     * @param paramObject  需要校验的字段所在的对象
     * @param rootBean     需要校验的方法所在的类对象
     * @param propertyPath 属性路径
     * @param <T>          需要校验的方法所在的类
     * @return 校验结果集
     */
    public static <T> Set<ConstraintViolation<T>> validate(Field field,
                                                           Object paramObject,
                                                           T rootBean,
                                                           String propertyPath) {
        Set<ConstraintViolation<T>> result = new HashSet<>();

        Object fieldValue;
        try {
            field.setAccessible(true);
            fieldValue = field.get(paramObject);
        } catch (IllegalAccessException e) {
            return result;
        }
        if (fieldValue instanceof Collection) {
            Collection<?> collectionFieldValue = (Collection<?>) fieldValue;
            if (CollectionUtil.isNotEmpty(collectionFieldValue)) {
                for (Object collectionElement : collectionFieldValue) {
                    Field[] fields = collectionElement.getClass().getDeclaredFields();
                    for (Field collectionFieldElementField : fields) {
                        result.addAll(
                                validate(
                                        collectionFieldElementField,
                                        collectionElement,
                                        rootBean,
                                        propertyPath + "." + collectionFieldElementField.getName()
                                )
                        );
                    }
                }
            }
        }

        CustomConstraintViolation<T> violation = validateField(field, paramObject, rootBean, propertyPath, fieldValue);
        if (violation != null) {
            result.add(violation);
        }
        return result;

    }


    /**
     * 校验字段值，如果值合法返回null，不合法返回消息内容
     *
     * @param field        需要校验的字段
     * @param targetObject 字段所在的对象
     * @return 值合法返回null，不合法返回消息内容
     */
    private static <T> CustomConstraintViolation<T> validateField(Field field,
                                                                  Object targetObject,
                                                                  T rootBean,
                                                                  String propertyPath,
                                                                  Object fieldValue) {
        Optional<ValidateAnnotationHandler<?>> handlerOptional =
                handlerSet.stream().filter(s -> s.isAnnotationPresent(field)).findFirst();
        if (handlerOptional.isPresent()) {
            String validate = handlerOptional.get().validate(field, targetObject);
            if (validate != null) {
                String fieldComment;
                if (field.isAnnotationPresent(ApiModelProperty.class)) {
                    ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                    fieldComment = annotation.value();
                } else {
                    fieldComment = field.getName();
                }
                String msg = fieldComment + validate;
                CustomConstraintDescriptor<?> customConstraintDescriptor =
                        new CustomConstraintDescriptor<>(field.getAnnotation(handlerOptional.get().getAnnotation()));
                return CustomConstraintViolation.of(rootBean, msg, propertyPath, fieldValue, customConstraintDescriptor);
            }
        }
        return null;
    }

}
