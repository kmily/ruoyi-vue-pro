package cn.iocoder.dashboard.framework.validator.custom;

import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>自定义约束描述符
 *
 * @author zzf
 * @date 2021/5/19
 */
public class CustomConstraintDescriptor<R extends Annotation> implements ConstraintDescriptor<R> {

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