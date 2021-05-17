package cn.iocoder.dashboard.framework.validator.custom.handler;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.*;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 校验处理辅助类
 *
 * @author zzf
 * @date 2021/4/15 9:49
 */
public class ValidateHandlerHelper {

    private static final Set<ValidateAnnotationHandler<?>> handlerSet = new HashSet<>(10);

    /**
     * 初始化注册校验处理类
     */
    static {
        handlerSet.add(new NotNullHandler());
        handlerSet.add(new NotEmptyHandler());
        handlerSet.add(new NotBlankHandler());
        handlerSet.add(new MaxHandler());
        handlerSet.add(new MinHandler());
        handlerSet.add(new SizeHandler());
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


    /**
     * 校验不通过的返回值对象
     *
     * @param <T> 需要校验的方法所在的类
     */
    public static class CustomConstraintViolation<T> implements ConstraintViolation<T> {

        public static <T> CustomConstraintViolation<T> of(T rootBean,
                                                          String msg,
                                                          String path,
                                                          Object invalidValue,
                                                          ConstraintDescriptor<?> constraintDescriptor) {
            CustomConstraintViolation<T> constraintViolation = new CustomConstraintViolation<>();
            constraintViolation.setMessage(msg);
            constraintViolation.setRootBean(rootBean);
            constraintViolation.setPropertyPath(PathImpl.createPathFromString(path));
            constraintViolation.setInvalidValue(invalidValue);
            constraintViolation.setConstraintDescriptor(constraintDescriptor);
            return constraintViolation;
        }

        @Setter
        private String message;

        @Setter
        private T rootBean;

        @Setter
        private Path propertyPath;

        @Setter
        private Object invalidValue;

        @Setter
        private ConstraintDescriptor<?> constraintDescriptor;


        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public T getRootBean() {
            return rootBean;
        }

        @Override
        public Class<T> getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return propertyPath;
        }

        @Override
        public Object getInvalidValue() {
            return invalidValue;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return constraintDescriptor;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }

    public static class CustomConstraintDescriptor<R extends Annotation> implements ConstraintDescriptor<R> {

        private final R annotation;

        public CustomConstraintDescriptor(R annotation) {
            this.annotation = annotation;
        }

        @Override
        public R getAnnotation() {
            return annotation;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public Set<Class<?>> getGroups() {
            return null;
        }

        @Override
        public Set<Class<? extends Payload>> getPayload() {
            return null;
        }

        @Override
        public ConstraintTarget getValidationAppliesTo() {
            return null;
        }

        @Override
        public List<Class<? extends ConstraintValidator<R, ?>>> getConstraintValidatorClasses() {
            return null;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return new HashMap<>();
        }

        @Override
        public Set<ConstraintDescriptor<?>> getComposingConstraints() {
            return null;
        }

        @Override
        public boolean isReportAsSingleViolation() {
            return false;
        }

        @Override
        public ValidateUnwrappedValue getValueUnwrapping() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }

}
