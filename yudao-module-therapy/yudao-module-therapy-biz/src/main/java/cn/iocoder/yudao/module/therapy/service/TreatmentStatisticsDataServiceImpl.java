package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayInstanceMapper;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentDayitemInstanceMapper;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreatmentStatisticsDataServiceImpl implements TreatmentStatisticsDataService {

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

    @Override
    public List<TreatmentDayitemInstanceDO> queryTreatmentProgressDetail(Long treatmentInstanceId){
//        List<TreatmentDayitemInstanceDO> dayitemInstanceDOS = treatmentDayitemInstanceMapper.selectList(TreatmentDayitemInstanceDO::getFlowInstanceId, treatmentInstanceId);
//        List<TreatmentDayInstanceDO> dayInstanceDOS = treatmentDayInstanceMapper.selectList(TreatmentDayInstanceDO::getFlowInstanceId, treatmentInstanceId);
        return null;
    }


}
