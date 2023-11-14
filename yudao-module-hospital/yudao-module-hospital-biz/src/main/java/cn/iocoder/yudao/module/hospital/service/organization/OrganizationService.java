package cn.iocoder.yudao.module.hospital.service.organization;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.hospital.controller.admin.organization.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.organization.OrganizationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 组织机构 Service 接口
 *
 * @author 芋道源码
 */
public interface OrganizationService extends IService<OrganizationDO> {

    /**
     * 创建组织机构
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrganization(@Valid OrganizationCreateReqVO createReqVO);

    /**
     * 更新组织机构
     *
     * @param updateReqVO 更新信息
     */
    void updateOrganization(@Valid OrganizationUpdateReqVO updateReqVO);

    /**
     * 删除组织机构
     *
     * @param id 编号
     */
    void deleteOrganization(Long id);

    /**
     * 获得组织机构
     *
     * @param id 编号
     * @return 组织机构
     */
    OrganizationDO getOrganization(Long id);

    /**
     * 获得组织机构列表
     *
     * @param ids 编号
     * @return 组织机构列表
     */
    List<OrganizationDO> getOrganizationList(Collection<Long> ids);

    /**
     * 获得组织机构分页
     *
     * @param pageReqVO 分页查询
     * @return 组织机构分页
     */
    PageResult<OrganizationDO> getOrganizationPage(OrganizationPageReqVO pageReqVO);

    /**
     * 获得组织机构列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 组织机构列表
     */
    List<OrganizationDO> getOrganizationList(OrganizationExportReqVO exportReqVO);

    /**
     * 关闭组织机构
     * @param id 编号
     */
    void closeOrganization(Long id);

    /**
     * 开启机构
     * @param id 编号
     */
    void openOrganization(Long id);

    /**
     * 机构审核
     * @param auditVO 审核参数
     */
    void auditOrganization(OrganizationAuditVO auditVO);
}
