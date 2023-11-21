package cn.iocoder.yudao.framework.common.validation;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author whycode
 * @title: IDCardValidator
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2110:27
 */
public class IDCardValidator implements ConstraintValidator<IDCard, String> {
    @Override
    public void initialize(IDCard constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果手机号为空，默认不校验，即校验通过
        if (StrUtil.isEmpty(value)) {
            return true;
        }
        return IdcardUtil.isValidCard(value);
    }
}
