package cn.iocoder.yudao.module.system.dal.mysql.organizationpackage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.organizationpackage.OrganizationPackageDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo.*;
import org.apache.ibatis.annotations.Update;

/**
 * 机构套餐 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrganizationPackageMapper extends BaseMapperX<OrganizationPackageDO>,BaseMapper<OrganizationPackageDO> {

    default PageResult<OrganizationPackageDO> selectPage(OrganizationPackagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrganizationPackageDO>()
                .likeIfPresent(OrganizationPackageDO::getName, reqVO.getName())
                .eqIfPresent(OrganizationPackageDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OrganizationPackageDO::getRemark, reqVO.getRemark())
                .eqIfPresent(OrganizationPackageDO::getMenuIds, reqVO.getMenuIds())
                .eqIfPresent(OrganizationPackageDO::getIsDefault, reqVO.getIsDefault())
                .betweenIfPresent(OrganizationPackageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrganizationPackageDO::getId));
    }

    default List<OrganizationPackageDO> selectList(OrganizationPackageExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<OrganizationPackageDO>()
                .likeIfPresent(OrganizationPackageDO::getName, reqVO.getName())
                .eqIfPresent(OrganizationPackageDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OrganizationPackageDO::getRemark, reqVO.getRemark())
                .eqIfPresent(OrganizationPackageDO::getMenuIds, reqVO.getMenuIds())
                .eqIfPresent(OrganizationPackageDO::getIsDefault, reqVO.getIsDefault())
                .betweenIfPresent(OrganizationPackageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrganizationPackageDO::getId));
    }

}
