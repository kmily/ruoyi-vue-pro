package cn.iocoder.yudao.module.therapy.taskflow;

import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentSurveyMapper;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import cn.iocoder.yudao.module.therapy.service.TreatmentStatisticsDataService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.DAYITEM_INSTANCE_ID;

@Component
public class ScheduleChartFlow extends BaseFlow {
        @Resource
        TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

        @Resource
        SurveyService surveyService;

        @Resource
        TreatmentSurveyMapper treatmentSurveyMapper;

        @Resource
        private TreatmentStatisticsDataService treatmentStatisticsDataService;
        @Resource
        private TreatmentService treatmentService;


        public ScheduleChartFlow(ProcessEngine engine) {
            super(engine);
        }

        public String deploy(Long id, Map<String, Object> settings) {
            return super.deploy(id, "/mood_score_statistic_chart_flow.json", settings);
        }

        @Override
        public String getProcessName(Long id) {
            return "MOOD_SCORE_STATISTICS_CHART_FLOW-" + id;
        }

        @Override
        public void onFlowEnd(DelegateExecution execution) {
            Map variables = execution.getVariables();
            Long dayItemInstanceId = (Long) variables.get(DAYITEM_INSTANCE_ID);
            treatmentService.finishDayItemInstance(dayItemInstanceId);
        }
    }
