package cn.iocoder.yudao.module.steam.dal.mysql.selweapon;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.selweapon.SelWeaponDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 武器选择 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SelWeaponMapper extends BaseMapperX<SelWeaponDO> {

    default PageResult<SelWeaponDO> selectPage(PageParam reqVO, Long typeId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SelWeaponDO>()
            .eq(SelWeaponDO::getTypeId, typeId)
            .orderByDesc(SelWeaponDO::getId));
    }

    default int deleteByTypeId(Long typeId) {
        return delete(SelWeaponDO::getTypeId, typeId);
    }

}