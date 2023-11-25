package cn.iocoder.yudao.module.wms.dal.mysql.ro;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wms.dal.dataobject.ro.RoDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wms.controller.admin.ro.vo.*;

/**
 * 收料单 Mapper
 *
 * @author Arlen
 */
@Mapper
public interface RoMapper extends BaseMapperX<RoDO> {

    default PageResult<RoDO> selectPage(RoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RoDO>()
                .eqIfPresent(RoDO::getRoCode, reqVO.getRoCode())
                .eqIfPresent(RoDO::getRoType, reqVO.getRoType())
                .eqIfPresent(RoDO::getRoStatus, reqVO.getRoStatus())
                .eqIfPresent(RoDO::getIsUrgent, reqVO.getIsUrgent())
                .eqIfPresent(RoDO::getAsn, reqVO.getAsn())
                .eqIfPresent(RoDO::getSupId, reqVO.getSupId())
                .eqIfPresent(RoDO::getCustId, reqVO.getCustId())
                .eqIfPresent(RoDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(RoDO::getEmpId, reqVO.getEmpId())
                .eqIfPresent(RoDO::getRosrcType, reqVO.getRosrcType())
                .eqIfPresent(RoDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(RoDO::getCheckTime, reqVO.getCheckTime())
                .eqIfPresent(RoDO::getClosed, reqVO.getClosed())
                .eqIfPresent(RoDO::getOrgId, reqVO.getOrgId())
                .eqIfPresent(RoDO::getChecker, reqVO.getChecker())
                .eqIfPresent(RoDO::getCloser, reqVO.getCloser())
                .betweenIfPresent(RoDO::getCreateTime, reqVO.getCreateTime())
                );
    }

}