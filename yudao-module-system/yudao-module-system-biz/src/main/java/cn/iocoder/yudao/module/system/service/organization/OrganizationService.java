package cn.iocoder.yudao.module.system.service.organization;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.organization.OrganizationDO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    /**
     *  根据套餐查询是否有在使用
     * @param packageId 套餐ID
     * @return
     */
    long selectByPackageId(Long packageId);

    /**
     * 根据套餐查询所有的机构
     * @param packageId 套餐编号
     * @return
     */
    List<OrganizationDO> getOrganizationListByPackageId(Long packageId);

    /**
     * 更新机构的权限菜单
     * @param id 机构编号
     * @param menuIds 权限
     */
    void updateOrganizationRoleMenu(Long id, Set<Long> menuIds);
}
