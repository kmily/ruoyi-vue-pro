package cn.iocoder.yudao.module.therapy.validation;

import cn.iocoder.yudao.framework.common.validation.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = MobileValidator.class
)
public @interface Question {

    String message() default "题目列表不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
