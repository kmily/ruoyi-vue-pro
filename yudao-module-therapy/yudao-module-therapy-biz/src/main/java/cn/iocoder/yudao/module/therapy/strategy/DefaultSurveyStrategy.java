package cn.iocoder.yudao.module.therapy.strategy;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyQuestionType;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.QUESTION_NOT_EXISTS_SURVEY;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 默认问卷策略实现
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

    @Override
    public void fillQuestion(SurveySaveReqVO vo) {
        vo.setQuestions(new ArrayList<>());
        JSONObject map1 = new JSONObject();
        map1.set("title", "特殊题标题");
        map1.set("type", SurveyQuestionType.SPACES.getCode());
        map1.set("field", IdUtil.fastSimpleUUID());
        map1.set("$required", false);
        vo.getQuestions().add(map1.toString());

        JSONObject map2 = new JSONObject();
        map2.set("title", "特殊题标题");
        map2.set("type", SurveyQuestionType.SPACES.getCode());
        map2.set("field", IdUtil.fastSimpleUUID());
        map2.set("$required", false);
        vo.getQuestions().add(map2.toString());

        JSONObject map3 = new JSONObject();
        map3.set("title", "特殊题标题");
        map3.set("type", SurveyQuestionType.SPACES.getCode());
        map3.set("field", IdUtil.fastSimpleUUID());
        map3.set("$required", false);
        vo.getQuestions().add(map3.toString());
    }

    @Override
    public void checkQuestionExistsSurvey(SubmitSurveyReqVO reqVO, List<QuestionDO> qst){

    }
}
