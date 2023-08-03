package cn.iocoder.yudao.module.radar.service.healthdata;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.convert.healthdata.HealthDataConvert;
import cn.iocoder.yudao.module.radar.dal.mysql.healthdata.HealthDataMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
//import static cn.iocoder.yudao.module.radar.enums.ErrorCodeConstants.*;

/**
 * 体征数据 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class HealthDataServiceImpl implements HealthDataService {

    @Resource
    private HealthDataMapper healthDataMapper;

    @Override
    public Long createHealthData(HealthDataCreateReqVO createReqVO) {
        // 插入
        HealthDataDO healthData = HealthDataConvert.INSTANCE.convert(createReqVO);
        healthDataMapper.insert(healthData);
        // 返回
        return healthData.getId();
    }

    @Override
    public void updateHealthData(HealthDataUpdateReqVO updateReqVO) {
        // 校验存在
        validateHealthDataExists(updateReqVO.getId());
        // 更新
        HealthDataDO updateObj = HealthDataConvert.INSTANCE.convert(updateReqVO);
        healthDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteHealthData(Long id) {
        // 校验存在
        validateHealthDataExists(id);
        // 删除
        healthDataMapper.deleteById(id);
    }

    private void validateHealthDataExists(Long id) {
        if (healthDataMapper.selectById(id) == null) {
            //throw exception(HEALTH_DATA_NOT_EXISTS);
        }
    }

    @Override
    public HealthDataDO getHealthData(Long id) {
        return healthDataMapper.selectById(id);
    }

    @Override
    public List<HealthDataDO> getHealthDataList(Collection<Long> ids) {
        return healthDataMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<HealthDataDO> getHealthDataPage(HealthDataPageReqVO pageReqVO) {
        return healthDataMapper.selectPage(pageReqVO);
    }

    @Override
    public List<HealthDataDO> getHealthDataList(HealthDataExportReqVO exportReqVO) {
        return healthDataMapper.selectList(exportReqVO);
    }

}
