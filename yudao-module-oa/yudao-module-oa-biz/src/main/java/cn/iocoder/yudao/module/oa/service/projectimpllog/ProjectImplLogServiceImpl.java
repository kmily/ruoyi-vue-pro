package cn.iocoder.yudao.module.oa.service.projectimpllog;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpllog.ProjectImplLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.projectimpllog.ProjectImplLogConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.projectimpllog.ProjectImplLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 工程日志列表 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class ProjectImplLogServiceImpl implements ProjectImplLogService {

    @Resource
    private ProjectImplLogMapper projectImplLogMapper;

    @Override
    public Long createProjectImplLog(ProjectImplLogCreateReqVO createReqVO) {
        // 插入
        ProjectImplLogDO projectImplLog = ProjectImplLogConvert.INSTANCE.convert(createReqVO);
        projectImplLogMapper.insert(projectImplLog);
        // 返回
        return projectImplLog.getId();
    }

    @Override
    public void updateProjectImplLog(ProjectImplLogUpdateReqVO updateReqVO) {
        // 校验存在
        validateProjectImplLogExists(updateReqVO.getId());
        // 更新
        ProjectImplLogDO updateObj = ProjectImplLogConvert.INSTANCE.convert(updateReqVO);
        projectImplLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteProjectImplLog(Long id) {
        // 校验存在
        validateProjectImplLogExists(id);
        // 删除
        projectImplLogMapper.deleteById(id);
    }

    private void validateProjectImplLogExists(Long id) {
        if (projectImplLogMapper.selectById(id) == null) {
            throw exception(PROJECT_IMPL_LOG_NOT_EXISTS);
        }
    }

    @Override
    public ProjectImplLogDO getProjectImplLog(Long id) {
        return projectImplLogMapper.selectById(id);
    }

    @Override
    public List<ProjectImplLogDO> getProjectImplLogList(Collection<Long> ids) {
        return projectImplLogMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ProjectImplLogDO> getProjectImplLogPage(ProjectImplLogPageReqVO pageReqVO) {
        return projectImplLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProjectImplLogDO> getProjectImplLogList(ProjectImplLogExportReqVO exportReqVO) {
        return projectImplLogMapper.selectList(exportReqVO);
    }

}
