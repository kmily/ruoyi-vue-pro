package cn.iocoder.yudao.module.oa.service.projectimpllog;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpllog.ProjectImplLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 工程日志列表 Service 接口
 *
 * @author 管理员
 */
public interface ProjectImplLogService {

    /**
     * 创建工程日志列表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProjectImplLog(@Valid ProjectImplLogCreateReqVO createReqVO);

    /**
     * 更新工程日志列表
     *
     * @param updateReqVO 更新信息
     */
    void updateProjectImplLog(@Valid ProjectImplLogUpdateReqVO updateReqVO);

    /**
     * 删除工程日志列表
     *
     * @param id 编号
     */
    void deleteProjectImplLog(Long id);

    /**
     * 获得工程日志列表
     *
     * @param id 编号
     * @return 工程日志列表
     */
    ProjectImplLogDO getProjectImplLog(Long id);

    /**
     * 获得工程日志列表列表
     *
     * @param ids 编号
     * @return 工程日志列表列表
     */
    List<ProjectImplLogDO> getProjectImplLogList(Collection<Long> ids);

    /**
     * 获得工程日志列表分页
     *
     * @param pageReqVO 分页查询
     * @return 工程日志列表分页
     */
    PageResult<ProjectImplLogDO> getProjectImplLogPage(ProjectImplLogPageReqVO pageReqVO);

    /**
     * 获得工程日志列表列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 工程日志列表列表
     */
    List<ProjectImplLogDO> getProjectImplLogList(ProjectImplLogExportReqVO exportReqVO);

}
