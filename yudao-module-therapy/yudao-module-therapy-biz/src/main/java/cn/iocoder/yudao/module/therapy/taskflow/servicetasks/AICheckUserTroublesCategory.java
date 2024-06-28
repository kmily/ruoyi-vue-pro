package cn.iocoder.yudao.module.therapy.taskflow.servicetasks;

import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.PsycoTroubleEnum;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AnAnswerReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.service.AIChatService;
import cn.iocoder.yudao.module.therapy.service.DayitemInstanceService;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import liquibase.pro.packaged.R;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.*;


@Component("aICheckUserTroublesCategory")
public class AICheckUserTroublesCategory implements org.flowable.engine.delegate.JavaDelegate {

    @Resource
    private AIChatService aiChatService;

    @Resource
    private DayitemInstanceService dayitemInstanceService;

    @Resource
    private SurveyService surveyService;

    private List<String> llmResultToPsycoCategories(String result){
        List<String> categories = new ArrayList<>();
        for(PsycoTroubleEnum psycoTroubleEnum : PsycoTroubleEnum.values()){
            if(result.contains(psycoTroubleEnum.getCategory())){
                categories.add(psycoTroubleEnum.getCategory());
            }
        }
        return categories;
    }

    private SubmitSurveyReqVO composeSubmitSurveyReqVO(DelegateExecution execution, List<String> troublesCategory){
        SubmitSurveyReqVO vo = new SubmitSurveyReqVO();
        vo.setId((Long) execution.getVariable("survey_instance_id"));
        vo.setSurveyType(SURVEY_SOURCE_TYPE);
        AnAnswerReqVO anAnswerReqVO = new AnAnswerReqVO();
        JSONObject answer = new JSONObject();
        answer.put(USER_TROUBLE_CATEGORIES, troublesCategory);
        anAnswerReqVO.setAnswer(answer);
        anAnswerReqVO.setQstCode("primary_troubles");
        List<AnAnswerReqVO> qstList = new ArrayList<>();
        qstList.add(anAnswerReqVO);
        vo.setQstList(qstList);
        return vo;
    }

    public void execute(DelegateExecution execution) {
        List<String> userTroubles = (List<String>) execution.getVariable("troubles");
        execution.setVariable("primary_troubles_is_set", false);
        if (userTroubles == null) {
            return;
        }
        System.out.println("User troubles: " + userTroubles);
        List<String> troublesCategory  = aiChatService.teenProblemClassificationV2(String.join(",", userTroubles));
        if(troublesCategory.size() > 0 ){
            execution.setVariable("primary_troubles_is_set", true);
            Long dayitemInstanceId = (Long) execution.getVariable(DAYITEM_INSTANCE_ID);
            dayitemInstanceService.updateDayitemInstanceExtAttr(dayitemInstanceId, USER_TROUBLE_CATEGORIES, troublesCategory);
            SubmitSurveyReqVO submitReqVO = composeSubmitSurveyReqVO(execution, troublesCategory);
            surveyService.submitSurveyForFlow(submitReqVO);
        }
    }
}

