package cn.iocoder.yudao.module.steam.service.seltype;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.seltype.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelTypeDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selweapon.SelWeaponDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.seltype.SelTypeMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selweapon.SelWeaponMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 类型选择 Service 实现类
 *
 * @author glzaboy
 */
@Service
@Validated
public class SelTypeServiceImpl implements SelTypeService {

    @Resource
    private SelTypeMapper selTypeMapper;
    @Resource
    private SelWeaponMapper selWeaponMapper;

    @Override
    public Long createSelType(SelTypeSaveReqVO createReqVO) {
        // 插入
        SelTypeDO selType = BeanUtils.toBean(createReqVO, SelTypeDO.class);
        selTypeMapper.insert(selType);
        // 返回
        return selType.getId();
    }

    @Override
    public void updateSelType(SelTypeSaveReqVO updateReqVO) {
        // 校验存在
        validateSelTypeExists(updateReqVO.getId());
        // 更新
        SelTypeDO updateObj = BeanUtils.toBean(updateReqVO, SelTypeDO.class);
        selTypeMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSelType(Long id) {
        // 校验存在
        validateSelTypeExists(id);
        // 删除
        selTypeMapper.deleteById(id);

        // 删除子表
        deleteSelWeaponByTypeId(id);
    }

    private void validateSelTypeExists(Long id) {
        if (selTypeMapper.selectById(id) == null) {
            throw exception(SEL_TYPE_NOT_EXISTS);
        }
    }

    @Override
    public SelTypeDO getSelType(Long id) {
        return selTypeMapper.selectById(id);
    }

    @Override
    public PageResult<SelTypeDO> getSelTypePage(SelTypePageReqVO pageReqVO) {
        return selTypeMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（武器选择） ====================

    @Override
    public PageResult<SelWeaponDO> getSelWeaponPage(PageParam pageReqVO, Long typeId) {
        return selWeaponMapper.selectPage(pageReqVO, typeId);
    }

    @Override
    public Long createSelWeapon(SelWeaponDO selWeapon) {
        selWeaponMapper.insert(selWeapon);
        return selWeapon.getId();
    }

    @Override
    public void updateSelWeapon(SelWeaponDO selWeapon) {
        // 校验存在
        validateSelWeaponExists(selWeapon.getId());
        // 更新
        selWeaponMapper.updateById(selWeapon);
    }

    @Override
    public void deleteSelWeapon(Long id) {
        // 校验存在
        validateSelWeaponExists(id);
        // 删除
        selWeaponMapper.deleteById(id);
    }

    @Override
    public SelWeaponDO getSelWeapon(Long id) {
        return selWeaponMapper.selectById(id);
    }

    private void validateSelWeaponExists(Long id) {
        if (selWeaponMapper.selectById(id) == null) {
            throw exception(SEL_WEAPON_NOT_EXISTS);
        }
    }

    private void deleteSelWeaponByTypeId(Long typeId) {
        selWeaponMapper.deleteByTypeId(typeId);
    }

}