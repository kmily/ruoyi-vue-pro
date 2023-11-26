package cn.iocoder.yudao.module.db.dal.mysql.material;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.db.dal.dataobject.material.MaterialDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.db.controller.admin.material.vo.*;

/**
 * 物料 Mapper
 *
 * @author Arlen
 */
@Mapper
public interface MaterialMapper extends BaseMapperX<MaterialDO> {

    default PageResult<MaterialDO> selectPage(MaterialPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaterialDO>()
                .eqIfPresent(MaterialDO::getOrgId, reqVO.getOrgId())
                .eqIfPresent(MaterialDO::getErpMtrlId, reqVO.getErpMtrlId())
                .eqIfPresent(MaterialDO::getModel, reqVO.getModel())
                .eqIfPresent(MaterialDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(MaterialDO::getStorelocId, reqVO.getStorelocId())
                .eqIfPresent(MaterialDO::getQtySkMin, reqVO.getQtySkMin())
                .eqIfPresent(MaterialDO::getQtySkMax, reqVO.getQtySkMax())
                .eqIfPresent(MaterialDO::getQtySkWarn, reqVO.getQtySkWarn())
                .eqIfPresent(MaterialDO::getRem, reqVO.getRem())
                .eqIfPresent(MaterialDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(MaterialDO::getCheckTime, reqVO.getCheckTime())
                .eqIfPresent(MaterialDO::getUnitId, reqVO.getUnitId())
                .eqIfPresent(MaterialDO::getChecker, reqVO.getChecker())
                .betweenIfPresent(MaterialDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialDO::getCode, reqVO.getCode())
                .likeIfPresent(MaterialDO::getName, reqVO.getName())
                .eqIfPresent(MaterialDO::getType, reqVO.getType())
                .eqIfPresent(MaterialDO::getAbc, reqVO.getAbc())
                .orderByDesc(MaterialDO::getId));
    }

}