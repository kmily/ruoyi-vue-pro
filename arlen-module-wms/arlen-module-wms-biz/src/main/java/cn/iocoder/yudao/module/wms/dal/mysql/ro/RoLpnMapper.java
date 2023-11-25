package cn.iocoder.yudao.module.wms.dal.mysql.ro;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoLpnDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收料单LPN明细 Mapper
 *
 * @author Arlen
 */
@Mapper
public interface RoLpnMapper extends BaseMapperX<RoLpnDO> {

    default List<RoLpnDO> selectListByRoId(String roId) {
        return selectList(RoLpnDO::getRoId, roId);
    }

    default int deleteByRoId(String roId) {
        return delete(RoLpnDO::getRoId, roId);
    }

}