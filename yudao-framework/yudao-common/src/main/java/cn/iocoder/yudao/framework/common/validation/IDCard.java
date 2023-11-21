package cn.iocoder.yudao.framework.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author whycode
 * @title: IDCard
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2110:27
 */
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
        validatedBy = IDCardValidator.class
)
public @interface IDCard {

    String message() default "身份证号格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
