package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentInstanceDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.TreatmentInstanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TreatmentStatisticsDataServiceImpl implements TreatmentStatisticsDataService {
    @Resource
    private TreatmentInstanceMapper treatmentInstanceMapper;

    @Override
    public Map<Long, List<String>> queryPsycoTroubleCategory(List<Long> treatmentInstanceIds) {
        return null;
    }

//    @Override
//    public Map<Long, TreatmentInstanceDO.TreatmentStatus> queryTreatmentStatus(List<Long> treatmentInstanceIds) {
//        return null;
//    }

    @Override
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
