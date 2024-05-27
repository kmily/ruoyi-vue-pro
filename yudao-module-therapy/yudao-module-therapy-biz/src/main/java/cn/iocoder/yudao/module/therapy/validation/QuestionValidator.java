package cn.iocoder.yudao.module.therapy.validation;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyQstSaveReqVO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class QuestionValidator implements ConstraintValidator<Question, List<SurveyQstSaveReqVO>> {
    @Override
    public boolean isValid(List<SurveyQstSaveReqVO> surveyQstSaveReqVOS, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
