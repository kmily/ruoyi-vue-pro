package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;

import java.util.List;
import java.util.Map;

public class TreatmentStatisticsDataServiceImpl implements TreatmentStatisticsDataService {

    @Override
    public Map<Long, List<String>> queryPsycoTroubleCategory(List<Long> treatmentInstanceIds) {
        return null;
    }

    @Override
    public Map<Long, TreatmentInstanceDO.TreatmentStatus> queryTreatmentStatus(List<Long> treatmentInstanceIds) {
        return null;
    }
}
