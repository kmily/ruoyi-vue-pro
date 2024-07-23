package cn.iocoder.yudao.module.therapy.taskflow.servicetasks;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TTMainInsertedStepDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TTMainInsertedStepMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;


@Component("scaleFlowSystemResponse")
public class ScaleFlowSystemResponse implements org.flowable.engine.delegate.JavaDelegate {

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    private TreatmentService treatmentService;

    @Resource
    private SurveyService surveyService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int getScoreByCode(DelegateExecution execution, String code){
        String key = "survey_instance_id+" + code;
        Long id = (Long) execution.getVariable(key);
        SurveyAnswerDO answerDO = surveyService.getAnswerDO(id);
        try {
            JsonNode jsonNode =  objectMapper.readTree(answerDO.getReprot());
            Map report = (Map) objectMapper.convertValue(jsonNode, Map.class);
            return (int) report.get("score");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


//（若PHQ-9得分<5分、GAD-7得分<5分、ISI得分<7分，输出：
//□：经过评估，目前没有发现你存在情绪和睡眠方面的问题，暂时不需要心理健康治疗，请继续保持呀！如果你感觉自己的状态变差，或者想要对自己的心理健康进行监测，可以随时使用哦~
//                结束治疗进程。
//
//        若PHQ-9得分≥5分或GAD-7得分≥5分或ISI≥7分，输出：
//□：经过评估，你目前可能遭遇了一定的心理困扰，让XXX（智能治疗师名称）来和你一起寻找改善的方法吧~
//                用户：好哒
//）

    public void execute(DelegateExecution execution) {
        Long dayItemInstanceId = (Long) execution.getVariable(DAYITEM_INSTANCE_ID);
        TreatmentDayitemInstanceDO dayitemInstanceDO = treatmentDayitemInstanceMapper.selectById(dayItemInstanceId);
        int scorePh9 = getScoreByCode(execution, "phq9_scale");
        int scoreGad7 = getScoreByCode(execution, "gad7_scale");
        int scoreIsi = getScoreByCode(execution, "isi_scale");
        String content;
        if (scorePh9 >= 5 || scoreGad7 >= 5 || scoreIsi >= 7){
            content = "经过评估，你目前可能遭遇了一定的心理困扰，让diudiu来和你一起寻找改善的方法吧~";
            treatmentService.addGuideLanguageStep(dayitemInstanceDO.getUserId(), dayitemInstanceDO.getFlowInstanceId(),
                    content);
            String userContent = "好哒";
            treatmentService.addGuideLanguageStepTypeUser(dayitemInstanceDO.getUserId(), dayitemInstanceDO.getFlowInstanceId(),
                    userContent);
        }else{
            content = "经过评估，目前没有发现你存在情绪和睡眠方面的问题，暂时不需要心理健康治疗，请继续保持呀！如果你感觉自己的状态变差，或者想要对自己的心理健康进行监测，可以随时使用哦~";
            treatmentService.addGuideLanguageStep(dayitemInstanceDO.getUserId(), dayitemInstanceDO.getFlowInstanceId(),
                    content);
        }
    }
}
