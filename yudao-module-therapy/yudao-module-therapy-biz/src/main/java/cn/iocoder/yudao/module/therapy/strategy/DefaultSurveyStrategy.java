package cn.iocoder.yudao.module.therapy.strategy;

import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.QUESTION_NOT_EXISTS_SURVEY;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 量表问卷策略实现
 */
@Component
public class DefaultSurveyStrategy extends AbstractStrategy implements SurveyStrategy {

    @Override
    public void validationReqVO(SurveySaveReqVO vo) {
//        super.validationReqVO(vo);
    }

    @Override
    public void checkLoseQuestion(SubmitSurveyReqVO reqVO, List<QuestionDO> qst) {
//        //可以部分提交,但提交的题必须在此问卷当中
//        Set<Long> qst1Set = reqVO.getQstList().stream().map(p -> p.getId()).collect(Collectors.toSet());
//        Set<Long> qst2Set = qst.stream().filter(k -> k.isRequired()).map(k -> k.getId()).collect(Collectors.toSet());
//        qst1Set.removeAll(qst2Set);
//        if (qst1Set.size() > 0) {
//            throw exception(QUESTION_NOT_EXISTS_SURVEY);
//        }
    }

}
