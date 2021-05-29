package cn.iocoder.dashboard.framework.validator.custom;

import lombok.Setter;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

/**
 * <p>校验不通过的返回值对象
 *
 * @author zzf
 * @date 2021/5/19
 * <T> 需要校验的方法所在的类
 */
public class CustomConstraintViolation<T> implements ConstraintViolation<T> {

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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * 校验字段值是否合法
     * @param rootBean 需要校验的方法所在的类对象
     * @param msg 校验错误信息
     * @param path 属性路径
     * @param invalidValue 校验失败的属性值
     * @param constraintDescriptor CustomConstraintDescriptor
     * @param <T> 泛型
     * @return CustomConstraintViolation
     */
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

}