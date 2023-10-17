package cn.iocoder.yudao.module.product.service.unit;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.product.controller.admin.unit.vo.*;
import cn.iocoder.yudao.module.product.dal.dataobject.unit.UnitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.product.convert.unit.UnitConvert;
import cn.iocoder.yudao.module.product.dal.mysql.unit.UnitMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.*;

/**
 * 计量单位 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class UnitServiceImpl implements UnitService {

    @Resource
    private UnitMapper unitMapper;

    @Override
    public Long createUnit(UnitCreateReqVO createReqVO) {
        // 插入
        UnitDO unit = UnitConvert.INSTANCE.convert(createReqVO);
        unitMapper.insert(unit);
        // 返回
        return unit.getId();
    }

    @Override
    public void updateUnit(UnitUpdateReqVO updateReqVO) {
        // 校验存在
        validateUnitExists(updateReqVO.getId());
        // 更新
        UnitDO updateObj = UnitConvert.INSTANCE.convert(updateReqVO);
        unitMapper.updateById(updateObj);
    }

    @Override
    public void deleteUnit(Long id) {
        // 校验存在
        validateUnitExists(id);
        // 删除
        unitMapper.deleteById(id);
    }

    private void validateUnitExists(Long id) {
        if (unitMapper.selectById(id) == null) {
            throw exception(UNIT_NOT_EXISTS);
        }
    }

    @Override
    public UnitDO getUnit(Long id) {
        return unitMapper.selectById(id);
    }

    @Override
    public List<UnitDO> getUnitList(Collection<Long> ids) {
        return unitMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<UnitDO> getUnitPage(UnitPageReqVO pageReqVO) {
        return unitMapper.selectPage(pageReqVO);
    }

    @Override
    public List<UnitDO> getUnitList(UnitExportReqVO exportReqVO) {
        return unitMapper.selectList(exportReqVO);
    }

}
