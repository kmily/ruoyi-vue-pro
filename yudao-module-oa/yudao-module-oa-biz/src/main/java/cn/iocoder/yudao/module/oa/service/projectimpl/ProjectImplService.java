package cn.iocoder.yudao.module.oa.service.projectimpl;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.projectimpl.ProjectImplDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 工程实施列 Service 接口
 *
 * @author 管理员
 */
public interface ProjectImplService {

    /**
     * 创建工程实施列
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProjectImpl(@Valid ProjectImplCreateReqVO createReqVO);

    /**
     * 更新工程实施列
     *
     * @param updateReqVO 更新信息
     */
    void updateProjectImpl(@Valid ProjectImplUpdateReqVO updateReqVO);

    /**
     * 删除工程实施列
     *
     * @param id 编号
     */
    void deleteProjectImpl(Long id);

    /**
     * 获得工程实施列
     *
     * @param id 编号
     * @return 工程实施列
     */
    ProjectImplDO getProjectImpl(Long id);

    /**
     * 获得工程实施列列表
     *
     * @param ids 编号
     * @return 工程实施列列表
     */
    List<ProjectImplDO> getProjectImplList(Collection<Long> ids);

    /**
     * 获得工程实施列分页
     *
     * @param pageReqVO 分页查询
     * @return 工程实施列分页
     */
    PageResult<ProjectImplDO> getProjectImplPage(ProjectImplPageReqVO pageReqVO);

    /**
     * 获得工程实施列列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 工程实施列列表
     */
    List<ProjectImplDO> getProjectImplList(ProjectImplExportReqVO exportReqVO);

}
