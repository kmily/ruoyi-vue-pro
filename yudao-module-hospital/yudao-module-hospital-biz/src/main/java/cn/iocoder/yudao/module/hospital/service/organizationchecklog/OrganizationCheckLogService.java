package cn.iocoder.yudao.module.hospital.service.organizationchecklog;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.hospital.controller.admin.organizationchecklog.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organizationchecklog.OrganizationCheckLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 组织审核记录 Service 接口
 *
 * @author 芋道源码
 */
public interface OrganizationCheckLogService {

    /**
     * 创建组织审核记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrganizationCheckLog(@Valid OrganizationCheckLogCreateReqVO createReqVO);


    /**
     * 获得组织审核记录列表
     *
     * @param ids 编号
     * @return 组织审核记录列表
     */
    List<OrganizationCheckLogDO> getOrganizationCheckLogList(Collection<Long> ids);

    /**
     * 获得组织审核记录分页
     *
     * @param pageReqVO 分页查询
     * @return 组织审核记录分页
     */
    PageResult<OrganizationCheckLogDO> getOrganizationCheckLogPage(OrganizationCheckLogPageReqVO pageReqVO);

    /**
     * 获得组织审核记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 组织审核记录列表
     */
    List<OrganizationCheckLogDO> getOrganizationCheckLogList(OrganizationCheckLogExportReqVO exportReqVO);

}
