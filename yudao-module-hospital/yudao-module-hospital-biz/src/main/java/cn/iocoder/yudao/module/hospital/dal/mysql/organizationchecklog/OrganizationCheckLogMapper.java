package cn.iocoder.yudao.module.hospital.dal.mysql.organizationchecklog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organizationchecklog.OrganizationCheckLogDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog.vo.*;

/**
 * 组织审核记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrganizationCheckLogMapper extends BaseMapperX<OrganizationCheckLogDO> {

    default PageResult<OrganizationCheckLogDO> selectPage(OrganizationCheckLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrganizationCheckLogDO>()
                .eqIfPresent(OrganizationCheckLogDO::getOrgId, reqVO.getOrgId())
                .likeIfPresent(OrganizationCheckLogDO::getCheckName, reqVO.getCheckName())
                .betweenIfPresent(OrganizationCheckLogDO::getCheckTime, reqVO.getCheckTime())
                .eqIfPresent(OrganizationCheckLogDO::getOpinion, reqVO.getOpinion())
                .eqIfPresent(OrganizationCheckLogDO::getCheckStatus, reqVO.getCheckStatus())
                .betweenIfPresent(OrganizationCheckLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrganizationCheckLogDO::getId));
    }

    default List<OrganizationCheckLogDO> selectList(OrganizationCheckLogExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<OrganizationCheckLogDO>()
                .eqIfPresent(OrganizationCheckLogDO::getOrgId, reqVO.getOrgId())
                .likeIfPresent(OrganizationCheckLogDO::getCheckName, reqVO.getCheckName())
                .betweenIfPresent(OrganizationCheckLogDO::getCheckTime, reqVO.getCheckTime())
                .eqIfPresent(OrganizationCheckLogDO::getOpinion, reqVO.getOpinion())
                .eqIfPresent(OrganizationCheckLogDO::getCheckStatus, reqVO.getCheckStatus())
                .betweenIfPresent(OrganizationCheckLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrganizationCheckLogDO::getId));
    }

}
