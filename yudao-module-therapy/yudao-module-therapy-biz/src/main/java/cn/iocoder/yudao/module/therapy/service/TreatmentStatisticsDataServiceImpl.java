package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.TreatmentProgressRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.TreatmentPlanVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AnAnswerReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.*;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.USER_TROUBLE_CATEGORIES;

@Service
public class TreatmentStatisticsDataServiceImpl implements TreatmentStatisticsDataService {
    @Resource
    private TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    private TreatmentDayInstanceMapper treatmentDayInstanceMapper;

    @Resource
    private TreatmentUserProgressMapper treatmentUserProgressMapper;

    @Resource
    private SurveyService surveyService;

    @Resource
    private TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;

    @Resource
    private TreatmentFlowDayMapper treatmentFlowDayMapper;

    @Resource
    private TreatmentFlowMapper treatmentFlowMapper;



    @Override
    public Map<Long, List<String>> queryPsycoTroubleCategory(List<Long> treatmentInstanceIds) {
        List<TreatmentDayitemInstanceDO> dayitemInstanceDOS =  treatmentDayitemInstanceMapper.selectList(TreatmentDayitemInstanceDO::getId, treatmentInstanceIds);
        Map<Long, List<String>> res = new HashMap();
        for(TreatmentDayitemInstanceDO dayitemInstanceDO : dayitemInstanceDOS){
            JSONObject extAttr = dayitemInstanceDO.getExtAttrObj();
            List<String> troubles = (List<String>) extAttr.getOrDefault(USER_TROUBLE_CATEGORIES, new ArrayList<String>());
            res.put(dayitemInstanceDO.getId(), troubles);
        }
        return res;
    }

    @Override
    public TreatmentProgressRespVO getTreatmentProgress(Long treatmentInstanceId) {
        TreatmentInstanceDO treatmentInstanceDO = treatmentInstanceMapper.selectById(treatmentInstanceId);
        Long flowId = treatmentInstanceDO.getFlowId();
        List<TreatmentDayitemDetailDO> detailDOS = treatmentFlowDayitemMapper.getDayitemDetail(treatmentInstanceId, flowId );
        TreatmentProgressRespVO treatmentProgressRespVO = new TreatmentProgressRespVO();
        treatmentProgressRespVO.setDay_instances(new ArrayList<>());
        treatmentProgressRespVO.setProgress_percentage(getProgressPercentage(treatmentInstanceId));

        TreatmentProgressRespVO.DayInstanceVO dayInstanceVO = null;
        for (TreatmentDayitemDetailDO detailDO : detailDOS) {
            if(dayInstanceVO == null || dayInstanceVO.getFlow_day_index() != detailDO.getFlowDayIndex()){
                dayInstanceVO = new TreatmentProgressRespVO.DayInstanceVO();
                dayInstanceVO.setDayitem_instances(new ArrayList<>());
                treatmentProgressRespVO.addDayInstance(dayInstanceVO);
            }
            dayInstanceVO.setDay_instance_id(detailDO.getDayInstanceId());
            dayInstanceVO.setHas_break(detailDO.getHasBreak());
            dayInstanceVO.setName(detailDO.getFlowDayName());
            String dayStatus = TreatmentDayInstanceDO.StatusEnum.fromValue(detailDO.getDayInstanceStatus()).toString();
            dayInstanceVO.setStatus(dayStatus);
            dayInstanceVO.setFlow_day_index(detailDO.getFlowDayIndex());
            if(dayInstanceVO.isHas_break()){
                continue; // skip break day
            }
            TreatmentProgressRespVO.DayitemInstanceVO dayitemInstanceVO = convertToDayItemInstanceVO(detailDO);
            if(!dayitemInstanceVO.getItem_type().equals(TaskType.GUIDE_LANGUAGE.getCode())){
                dayInstanceVO.addDayItemInstance(dayitemInstanceVO);
            }
        }
        return treatmentProgressRespVO;
    }

    private TreatmentProgressRespVO.DayitemInstanceVO convertToDayItemInstanceVO(TreatmentDayitemDetailDO detailDO){
        TreatmentProgressRespVO.DayitemInstanceVO dayitemInstanceVO = new TreatmentProgressRespVO.DayitemInstanceVO();
        dayitemInstanceVO.setDayitem_instance_id(detailDO.getDayitemInstanceId());
        String itemType = TaskType.getByType(detailDO.getDayitemType()).getCode();
        String itemName = TaskType.getByType(detailDO.getDayitemType()).getTitle();
        dayitemInstanceVO.setItem_type(itemType);
        dayitemInstanceVO.setItem_name(itemName);
        String status = TreatmentDayitemInstanceDO.StatusEnum.fromValue(detailDO.getDayitemInstanceStatus()).toString();
        dayitemInstanceVO.setStatus(status);
        dayitemInstanceVO.setCreate_time(detailDO.getCreateTime());
        dayitemInstanceVO.setUpdate_time(detailDO.getUpdateTime());
        return dayitemInstanceVO;
    }

    public Map<Long, TreatmentInstanceDO> queryLatestTreatmentInstanceId(List<Long> userIds) {
        Map<Long, TreatmentInstanceDO> map = new HashMap<>();
        for (Long userId : userIds) {
            TreatmentInstanceDO instanceDO = treatmentInstanceMapper.getLatestByUserId(userId);
            if (Objects.nonNull(instanceDO)) {
                map.put(userId, instanceDO);
            }
        }
        return map;
    }

    public List<String>  queryUserGoals(Long userId){
//        {"goals":[{"goal":"目标1","title":"问题1"},{"goal":"目标2","title":"问题2"},{"goal":"目标3","title":"问题3"}]}
        SubmitSurveyReqVO data =  surveyService.getGoalMotive(userId);
        List<AnAnswerReqVO> answers = data.getQstList();
        List<String> goals = new ArrayList<>();
        for(AnAnswerReqVO answer : answers){
            if(answer.getQstCode().equals("set_goal_qst")){
                List<Map<String, String>> goalAnswerList = (List<Map<String, String>>) answer.getAnswer().get("goals");
                for(Map<String, String> goalAnswer : goalAnswerList){
                    goals.add(goalAnswer.get("goal"));
                }
            }
        }
        return goals;
    }

    public List<String>  queryUserTroubles(TreatmentDayitemInstanceDO dayitemInstanceDO){
        Long userId = dayitemInstanceDO.getUserId();
        String flowCode = treatmentFlowMapper.getFlowCodeByDayitemInstanceId(dayitemInstanceDO.getId());
        TreatmentDayitemInstanceDO treatmentDayitemInstanceDO =
                treatmentDayitemInstanceMapper.queryUserGoalAndMotiveInstance(
                        userId,
                        TaskType.PROBLEM_GOAL_MOTIVE.getType(),
                        flowCode
                );
        if (treatmentDayitemInstanceDO == null) {
            return new ArrayList<>();
        }
        if(treatmentDayitemInstanceDO.getExtAttrObj() == null){
            return new ArrayList<>();
        }
        return (List<String>) treatmentDayitemInstanceDO.getExtAttrObj().getOrDefault("trouble_categories", new ArrayList<>());
    }


    @Override
    public TreatmentPlanVO getTreatmentProgressAndPlan(Long treatmentInstanceId) {
        TreatmentInstanceDO treatmentInstanceDO = treatmentInstanceMapper.selectById(treatmentInstanceId);
        Long flowId = treatmentInstanceDO.getFlowId();
        List<TreatmentDayitemDetailDO> detailDOS = treatmentFlowDayitemMapper.getDayitemDetail(treatmentInstanceId, flowId );
        TreatmentPlanVO treatmentPlanVO = new TreatmentPlanVO();
        treatmentPlanVO.setGroups(new ArrayList<>());
        treatmentPlanVO.setProgress_percentage(getProgressPercentage(treatmentInstanceId));
        int weekDays = 7;
        int dayIndex = 0;
        TreatmentProgressRespVO.DayInstanceVO dayInstanceVO = null;
        for (TreatmentDayitemDetailDO detailDO : detailDOS) {
            if(dayInstanceVO == null || dayInstanceVO.getFlow_day_index() != detailDO.getFlowDayIndex()){
                TreatmentPlanVO.GroupData groupData;
                if(dayIndex % weekDays == 0){
                    groupData = new TreatmentPlanVO.GroupData();
                    groupData.setIndex(dayIndex / weekDays);
                    groupData.setDay_instances(new ArrayList<>());
                    treatmentPlanVO.getGroups().add(groupData);
                }else{
                    groupData = treatmentPlanVO.getGroups().get(treatmentPlanVO.getGroups().size() - 1);
                }
                dayInstanceVO = new TreatmentProgressRespVO.DayInstanceVO();
                dayInstanceVO.setDayitem_instances(new ArrayList<>());
                groupData.addDayInstance(dayInstanceVO);
                dayIndex += 1;
            }
            dayInstanceVO.setDay_instance_id(detailDO.getDayInstanceId());
            dayInstanceVO.setHas_break(detailDO.getHasBreak());
            dayInstanceVO.setName(detailDO.getFlowDayName());
            String dayStatus = TreatmentDayInstanceDO.StatusEnum.fromValue(detailDO.getDayInstanceStatus()).toString();
            dayInstanceVO.setStatus(dayStatus);
            dayInstanceVO.setFlow_day_index(detailDO.getFlowDayIndex());
            if(dayInstanceVO.isHas_break()){
                continue; // skip break day
            }
            TreatmentProgressRespVO.DayitemInstanceVO dayitemInstanceVO = convertToDayItemInstanceVO(detailDO);
            if(!dayitemInstanceVO.getItem_type().equals(TaskType.GUIDE_LANGUAGE.getCode())){
                dayInstanceVO.addDayItemInstance(dayitemInstanceVO);
            }
        }
        return treatmentPlanVO;
    }


    @Override
    public int getProgressPercentage(Long treatmentInstanceId){
        TreatmentUserProgressDO treatmentUserProgressDO =  treatmentUserProgressMapper.selectOne(TreatmentUserProgressDO::getTreatmentInstanceId, treatmentInstanceId);
        if (Objects.isNull(treatmentUserProgressDO)) {
            return 0;
        }
        TreatmentDayInstanceDO treatmentDayInstanceDO = treatmentDayInstanceMapper.selectById(treatmentUserProgressDO.getDayInstanceId());
        int addition = 0; // if completed, add 1
        if (treatmentDayInstanceDO.getStatus() == TreatmentDayInstanceDO.StatusEnum.COMPLETED.getValue()) {
            addition = 1;
        }

        TreatmentFlowDayDO treatmentFlowDayDO = treatmentFlowDayMapper.selectById(treatmentDayInstanceDO.getDayId());
        int totalDays = treatmentFlowDayMapper.getDaysCount(treatmentFlowDayDO.getFlowId());
        if(totalDays == 0){
            return 0;
        }
        int count = treatmentFlowDayDO.getSequence() + addition;
        return (int) ( count / (float) totalDays  * 100);
    }

    @Override
    public int getDayProgressPercentage(Long treatmentInstanceId){
        TreatmentInstanceDO treatmentInstanceDO = treatmentInstanceMapper.selectById(treatmentInstanceId);
        TreatmentUserProgressDO treatmentUserProgressDO =  treatmentUserProgressMapper.getUserCurrentProgress(
                treatmentInstanceDO.getUserId(),
                treatmentInstanceDO.getId());
        TreatmentFlowDayDO treatmentFlowDayDO = treatmentFlowDayMapper.selectByDayInstanceId(treatmentUserProgressDO.getDayInstanceId());
        List<TreatmentDayitemDetailDO> detailDOS =  treatmentFlowDayitemMapper.getDayitemDetailOfDay(treatmentInstanceDO.getUserId(),
                treatmentInstanceId,
                treatmentFlowDayDO.getId());
        int totalCount = 0;
        int completeCount = 0;
        for(TreatmentDayitemDetailDO detailDO: detailDOS){
            if(detailDO.getDayitemType() == TaskType.GUIDE_LANGUAGE.getType()){
                continue;
            }
            totalCount += 1;
            if(detailDO.getDayitemInstanceStatus() == TreatmentDayitemInstanceDO.StatusEnum.COMPLETED.getValue()){
                completeCount += 1;
            }
        }
        if(totalCount == 0){
            return 0;
        }
        return (int)(( completeCount / (float) totalCount) * 100);
    }

    @Override
    public Map<String, Integer> getTreatmentUserCount(String startDate){
        Map<String, Integer> result = new HashMap<>();
        String flowCode = "main";
        //初步评估数据
        TreatmentFlowDO flowDO = treatmentFlowMapper.selectOne(TreatmentFlowDO::getCode, flowCode);
        TreatmentFlowDayitemDO flowDayitemDO = treatmentFlowDayitemMapper.getPriorEvaluation(flowDO.getId(), TaskType.SCALE.getType());
        assert flowDayitemDO != null;
        int finishedEvaluationCount = treatmentDayitemInstanceMapper.countByFinishedDayitemId(flowDayitemDO.getId());
        result.put("finishedEvaluationCount", finishedEvaluationCount); //完成初步评估的用户人次
        int inTreatmentCount = treatmentInstanceMapper.countInTreatment(flowDO.getId());
        result.put("inTreatmentCount", inTreatmentCount); // 正在治疗的用户人次
        int finishedTreatmentCount = treatmentInstanceMapper.countFinishedTreatment(flowDO.getId());
        result.put("finishedTreatmentCount", finishedTreatmentCount); // 已经完成的用户人次
        return result;
    }

}
