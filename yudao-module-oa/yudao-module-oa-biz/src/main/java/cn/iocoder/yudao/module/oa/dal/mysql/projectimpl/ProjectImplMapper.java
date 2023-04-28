package cn.iocoder.yudao.module.oa.dal.mysql.projectimpl;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpl.ProjectImplDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo.*;

/**
 * 工程实施列 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ProjectImplMapper extends BaseMapperX<ProjectImplDO> {

    default PageResult<ProjectImplDO> selectPage(ProjectImplPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectImplDO>()
                .eqIfPresent(ProjectImplDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ProjectImplDO::getImplScope, reqVO.getImplScope())
                .orderByDesc(ProjectImplDO::getId));
    }

    default List<ProjectImplDO> selectList(ProjectImplExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectImplDO>()
                .eqIfPresent(ProjectImplDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ProjectImplDO::getImplScope, reqVO.getImplScope())
                .orderByDesc(ProjectImplDO::getId));
    }

}
