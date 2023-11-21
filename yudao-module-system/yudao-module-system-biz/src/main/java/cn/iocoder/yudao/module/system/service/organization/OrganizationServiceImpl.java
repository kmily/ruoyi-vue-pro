package cn.iocoder.yudao.module.system.service.organization;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.system.controller.admin.organization.vo.*;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.tenant.vo.tenant.TenantCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import cn.iocoder.yudao.module.system.convert.organization.OrganizationConvert;
import cn.iocoder.yudao.module.system.convert.tenant.TenantConvert;
import cn.iocoder.yudao.module.system.convert.user.UserConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.organization.OrganizationDO;
import cn.iocoder.yudao.module.system.dal.dataobject.organizationpackage.OrganizationPackageDO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantPackageDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.organization.OrganizationMapper;
import cn.iocoder.yudao.module.system.enums.permission.RoleCodeEnum;
import cn.iocoder.yudao.module.system.enums.permission.RoleTypeEnum;
import cn.iocoder.yudao.module.system.service.organizationpackage.OrganizationPackageService;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import cn.iocoder.yudao.module.system.service.permission.RoleService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.organization.OrganizationStatusEnum.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;
import static java.util.Collections.singleton;

/**
 * 组织机构 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationDO> implements OrganizationService {

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Resource
    @Lazy
    private OrganizationPackageService organizationPackageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrganization(OrganizationCreateReqVO createReqVO) {
        // 插入
        OrganizationDO organization = OrganizationConvert.INSTANCE.convert(createReqVO);
        organization.setDisable(OPEN.value());

        OrganizationDO organizationDO = this.organizationMapper.selectOne(OrganizationDO::getName, organization.getName());
        if(organizationDO != null){
            throw exception(ORGANIZATION_NAME_EXIST_ERROR);
        }
        OrganizationPackageDO packageDO = organizationPackageService.getOrganizationPackageDefault();
        if(packageDO == null){
            throw exception(ORGANIZATION_PACKAGE_NOT_EXISTS);
        }
        organization.setPackageId(packageDO.getId());
        organizationMapper.insert(organization);
        Long roleId = createRole(packageDO);
        Long userId = createUser(roleId, organization.getId(), createReqVO);
        organizationMapper.updateById(new OrganizationDO().setUserId(userId).setId(organization.getId()));
        // 返回
        return organization.getId();
    }


    private Long createRole(OrganizationPackageDO organizationPackage) {
        RoleDO roleDO = roleService.getRoleByCode(RoleCodeEnum.STORE_ADMIN.getCode());
        if(Objects.nonNull(roleDO)){
            return roleDO.getId();
        }
        // 创建角色
        RoleCreateReqVO reqVO = new RoleCreateReqVO();
        reqVO.setName(RoleCodeEnum.STORE_ADMIN.getName()).setCode(RoleCodeEnum.STORE_ADMIN.getCode())
                .setSort(0).setRemark("系统自动生成");
        Long roleId = roleService.createRole(reqVO, RoleTypeEnum.SYSTEM.getType());
        // 分配权限
        permissionService.assignRoleMenu(roleId, organizationPackage.getMenuIds());
        return roleId;
    }

    private Long createUser(Long roleId, Long orgId, OrganizationCreateReqVO createReqVO) {

        UserCreateReqVO userCreateReqVO = new UserCreateReqVO();
        userCreateReqVO.setPassword(createReqVO.getPassword())
                .setEmail(createReqVO.getEmail())
                .setMobile(createReqVO.getMobile())
                .setUsername(createReqVO.getUsername())
                .setNickname(createReqVO.getUserName())
                .setOrgId(orgId);

        // 创建用户
        Long userId = adminUserService.createUser(userCreateReqVO);
        // 分配角色
        permissionService.assignUserRole(userId, singleton(roleId));
        return userId;
    }

    @Override
    public void updateOrganization(OrganizationUpdateReqVO updateReqVO) {
        // 校验存在
        validateOrganizationExists(updateReqVO.getId());
        // 更新
        OrganizationDO updateObj = OrganizationConvert.INSTANCE.convert(updateReqVO);
        organizationMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrganization(Long id) {
        // 校验存在
        validateOrganizationExists(id);
        // 删除
        organizationMapper.deleteById(id);
    }

    private void validateOrganizationExists(Long id) {
        if (organizationMapper.selectById(id) == null) {
            throw exception(ORGANIZATION_NOT_EXISTS);
        }
    }

    @Override
    public OrganizationDO getOrganization(Long id) {
        return organizationMapper.selectById(id);
    }

    @Override
    public List<OrganizationDO> getOrganizationList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return organizationMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<OrganizationDO> getOrganizationPage(OrganizationPageReqVO pageReqVO) {
        return organizationMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OrganizationDO> getOrganizationList(OrganizationExportReqVO exportReqVO) {
        return organizationMapper.selectList(exportReqVO);
    }

    @Override
    public void closeOrganization(Long id) {
        // 校验存在
        validateOrganizationExists(id);
        organizationMapper.updateById(new OrganizationDO().setDisable(CLOSED.value()).setId(id));
    }

    @Override
    public void openOrganization(Long id) {
        // 校验存在
        validateOrganizationExists(id);
        organizationMapper.updateById(new OrganizationDO().setDisable(OPEN.value()).setId(id));
    }

    @Override
    public void auditOrganization(OrganizationAuditVO auditVO) {
        OrganizationDO organization = this.getOrganization(auditVO.getId());
        if(organization == null){
            throw exception(ORGANIZATION_NOT_EXISTS);
        }
        Integer status = auditVO.getStatus();
        AdminUserDO user = adminUserService.getUser(SecurityFrameworkUtils.getLoginUserId());
        String checkStatus;
        if(status == 1){
            // 审通过
            checkStatus = OPEN.value();
        }else{
            checkStatus = REFUSED.value();
        }

        organizationMapper.updateById(new OrganizationDO()
                .setId(auditVO.getId())
                .setCheckName(user.getNickname())
                .setCheckStatus(checkStatus)
                .setDisable(checkStatus)
                .setOpinion(auditVO.getOpinion())
                .setCheckTime(LocalDateTime.now()));
    }

    @Override
    public long selectByPackageId(Long packageId) {
        return organizationMapper.selectCount(OrganizationDO::getPackageId, packageId);
    }

    @Override
    public List<OrganizationDO> getOrganizationListByPackageId(Long packageId) {
        return organizationMapper.selectList(OrganizationDO::getPackageId, packageId);
    }

    @Override
    public void updateOrganizationRoleMenu(Long orgId, Set<Long> menuIds) {
        TenantUtils.executeIgnore(() -> {
            // 获得所有角色
            RoleDO storeAdmin = roleService.getRoleByCode(RoleCodeEnum.STORE_ADMIN.getCode());
            List<RoleDO> roles = roleService.getRoleListByOrgId(orgId);
//            roles.forEach(role -> Assert.isTrue(orgId.equals(role.getOrgId()), "角色({}/{}) 租户不匹配",
//                    role.getId(), role.getTenantId(), orgId)); // 兜底校验

            roles.add(storeAdmin);
            // 重新分配每个角色的权限
            roles.forEach(role -> {
                // 如果是租户管理员，重新分配其权限为租户套餐的权限
                if (Objects.equals(role.getCode(), RoleCodeEnum.STORE_ADMIN.getCode())) {
                    permissionService.assignRoleMenu(role.getId(), menuIds);
                    log.info("[updateTenantRoleMenu][租户管理员({}/{}) 的权限修改为({})]", role.getId(), role.getTenantId(), menuIds);
                    return;
                }
                // 如果是其他角色，则去掉超过套餐的权限
                Set<Long> roleMenuIds = permissionService.getRoleMenuListByRoleId(role.getId());
                roleMenuIds = CollUtil.intersectionDistinct(roleMenuIds, menuIds);
                permissionService.assignRoleMenu(role.getId(), roleMenuIds);
                log.info("[updateTenantRoleMenu][角色({}/{}) 的权限修改为({})]", role.getId(), role.getTenantId(), roleMenuIds);
            });
        });
    }

}
