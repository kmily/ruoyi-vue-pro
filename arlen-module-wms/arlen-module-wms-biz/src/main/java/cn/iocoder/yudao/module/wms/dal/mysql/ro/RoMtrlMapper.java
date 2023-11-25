package cn.iocoder.yudao.module.wms.dal.mysql.ro;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoMtrlDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收料单物料明细 Mapper
 *
 * @author Arlen
 */
@Mapper
public interface RoMtrlMapper extends BaseMapperX<RoMtrlDO> {

    default List<RoMtrlDO> selectListByRoId(String roId) {
        return selectList(RoMtrlDO::getRoId, roId);
    }

    default int deleteByRoId(String roId) {
        return delete(RoMtrlDO::getRoId, roId);
    }

}