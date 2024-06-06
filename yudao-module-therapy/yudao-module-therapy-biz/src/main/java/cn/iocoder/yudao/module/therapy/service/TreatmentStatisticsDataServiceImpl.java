package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayInstanceMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentInstanceMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TreatmentStatisticsDataServiceImpl implements TreatmentStatisticsDataService {
    @Resource
    private TreatmentInstanceMapper treatmentInstanceMapper;

    @Resource
    private TreatmentDayitemInstanceMapper treatmentDayitemInstanceMapper;

    @Resource
    private TreatmentDayInstanceMapper treatmentDayInstanceMapper;


    @Override
    public Map<Long, List<String>> queryPsycoTroubleCategory(List<Long> treatmentInstanceIds) {
        List<TreatmentDayitemInstanceDO> dayitemInstanceDOS =  treatmentDayitemInstanceMapper.selectList(TreatmentDayitemInstanceDO::getId, treatmentInstanceIds);
        Map<Long, List<String>> res = new HashMap();
        for(TreatmentDayitemInstanceDO dayitemInstanceDO : dayitemInstanceDOS){
            JSONObject extAttr = dayitemInstanceDO.getExtAttrObj();
            List<String> troubles = (List<String>) extAttr.getOrDefault("psycoTroubleCategory", new ArrayList<String>());
            res.put(dayitemInstanceDO.getId(), troubles);
        }
        return res;
    }

//    @Override
//    public Map<Long, TreatmentInstanceDO.TreatmentStatus> queryTreatmentStatus(List<Long> treatmentInstanceIds) {
//        return null;
//    }

    @Override
    public List<TreatmentDayitemInstanceDO> queryTreatmentProgressDetail(Long treatmentInstanceId) {
//        List<TreatmentDayitemInstanceDO> dayitemInstanceDOS = treatmentDayitemInstanceMapper.selectList(TreatmentDayitemInstanceDO::getFlowInstanceId, treatmentInstanceId);
//        List<TreatmentDayInstanceDO> dayInstanceDOS = treatmentDayInstanceMapper.selectList(TreatmentDayInstanceDO::getFlowInstanceId, treatmentInstanceId);
        return null;

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


}
