package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.TreatmentProgressRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AnAnswerReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.*;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayInstanceMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentFlowDayitemMapper;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentInstanceMapper;
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
    private SurveyService surveyService;

    @Resource
    private TreatmentFlowDayitemMapper treatmentFlowDayitemMapper;


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
        //TODO fix this;
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


}
