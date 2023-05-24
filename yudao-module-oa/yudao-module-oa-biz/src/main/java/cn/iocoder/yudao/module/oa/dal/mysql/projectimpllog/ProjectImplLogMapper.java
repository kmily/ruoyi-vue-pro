package cn.iocoder.yudao.module.oa.dal.mysql.projectimpllog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.ProjectImplLogExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.ProjectImplLogPageReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpllog.ProjectImplLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 工程日志列表 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ProjectImplLogMapper extends BaseMapperX<ProjectImplLogDO> {

    default PageResult<ProjectImplLogDO> selectPage(ProjectImplLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProjectImplLogDO>()
                .eqIfPresent(ProjectImplLogDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ProjectImplLogDO::getImplStatus, reqVO.getImplStatus())
                .eqIfPresent(ProjectImplLogDO::getCreator, reqVO.getCreator())
                .orderByDesc(ProjectImplLogDO::getId));
    }

    default List<ProjectImplLogDO> selectList(ProjectImplLogExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectImplLogDO>()
                .eqIfPresent(ProjectImplLogDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ProjectImplLogDO::getImplStatus, reqVO.getImplStatus())
                .eqIfPresent(ProjectImplLogDO::getCreator, reqVO.getCreator())
                .orderByDesc(ProjectImplLogDO::getId));
    }

}
