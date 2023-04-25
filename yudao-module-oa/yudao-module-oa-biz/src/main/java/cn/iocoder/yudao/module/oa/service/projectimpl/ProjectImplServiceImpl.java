package cn.iocoder.yudao.module.oa.service.projectimpl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpl.ProjectImplDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.projectimpl.ProjectImplConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.projectimpl.ProjectImplMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 工程实施列 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class ProjectImplServiceImpl implements ProjectImplService {

    @Resource
    private ProjectImplMapper projectImplMapper;

    @Override
    public Long createProjectImpl(ProjectImplCreateReqVO createReqVO) {
        // 插入
        ProjectImplDO projectImpl = ProjectImplConvert.INSTANCE.convert(createReqVO);
        projectImplMapper.insert(projectImpl);
        // 返回
        return projectImpl.getId();
    }

    @Override
    public void updateProjectImpl(ProjectImplUpdateReqVO updateReqVO) {
        // 校验存在
        validateProjectImplExists(updateReqVO.getId());
        // 更新
        ProjectImplDO updateObj = ProjectImplConvert.INSTANCE.convert(updateReqVO);
        projectImplMapper.updateById(updateObj);
    }

    @Override
    public void deleteProjectImpl(Long id) {
        // 校验存在
        validateProjectImplExists(id);
        // 删除
        projectImplMapper.deleteById(id);
    }

    private void validateProjectImplExists(Long id) {
        if (projectImplMapper.selectById(id) == null) {
            throw exception(PROJECT_IMPL_NOT_EXISTS);
        }
    }

    @Override
    public ProjectImplDO getProjectImpl(Long id) {
        return projectImplMapper.selectById(id);
    }

    @Override
    public List<ProjectImplDO> getProjectImplList(Collection<Long> ids) {
        return projectImplMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProjectImplDO> getProjectImplPage(ProjectImplPageReqVO pageReqVO) {
        return projectImplMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProjectImplDO> getProjectImplList(ProjectImplExportReqVO exportReqVO) {
        return projectImplMapper.selectList(exportReqVO);
    }

}
