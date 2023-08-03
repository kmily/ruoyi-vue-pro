package cn.iocoder.yudao.module.radar.service.arearuledata;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.radar.controller.admin.arearuledata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.arearuledata.AreaRuleDataDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.convert.arearuledata.AreaRuleDataConvert;
import cn.iocoder.yudao.module.radar.dal.mysql.arearuledata.AreaRuleDataMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
//import static cn.iocoder.yudao.module.radar.enums.ErrorCodeConstants.*;

/**
 * 区域数据 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AreaRuleDataServiceImpl implements AreaRuleDataService {

    @Resource
    private AreaRuleDataMapper areaRuleDataMapper;

    @Override
    public Long createAreaRuleData(AreaRuleDataCreateReqVO createReqVO) {
        // 插入
        AreaRuleDataDO areaRuleData = AreaRuleDataConvert.INSTANCE.convert(createReqVO);
        areaRuleDataMapper.insert(areaRuleData);
        // 返回
        return areaRuleData.getId();
    }

    @Override
    public void updateAreaRuleData(AreaRuleDataUpdateReqVO updateReqVO) {
        // 校验存在
        validateAreaRuleDataExists(updateReqVO.getId());
        // 更新
        AreaRuleDataDO updateObj = AreaRuleDataConvert.INSTANCE.convert(updateReqVO);
        areaRuleDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteAreaRuleData(Long id) {
        // 校验存在
        validateAreaRuleDataExists(id);
        // 删除
        areaRuleDataMapper.deleteById(id);
    }

    private void validateAreaRuleDataExists(Long id) {
        if (areaRuleDataMapper.selectById(id) == null) {
            //throw exception(AREA_RULE_DATA_NOT_EXISTS);
        }
    }

    @Override
    public AreaRuleDataDO getAreaRuleData(Long id) {
        return areaRuleDataMapper.selectById(id);
    }

    @Override
    public List<AreaRuleDataDO> getAreaRuleDataList(Collection<Long> ids) {
        return areaRuleDataMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<AreaRuleDataDO> getAreaRuleDataPage(AreaRuleDataPageReqVO pageReqVO) {
        return areaRuleDataMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AreaRuleDataDO> getAreaRuleDataList(AreaRuleDataExportReqVO exportReqVO) {
        return areaRuleDataMapper.selectList(exportReqVO);
    }

}
