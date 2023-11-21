package cn.iocoder.yudao.module.system.service.organizationpackage;

import cn.iocoder.yudao.module.system.dal.dataobject.organization.OrganizationDO;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.system.service.organization.OrganizationService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.organizationpackage.OrganizationPackageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.system.convert.organizationpackage.OrganizationPackageConvert;
import cn.iocoder.yudao.module.system.dal.mysql.organizationpackage.OrganizationPackageMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 机构套餐 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OrganizationPackageServiceImpl extends ServiceImpl<OrganizationPackageMapper, OrganizationPackageDO> implements OrganizationPackageService {

    @Resource
    private OrganizationPackageMapper organizationPackageMapper;

    @Resource
    @Lazy
    private OrganizationService organizationService;

    @Override
    public Long createOrganizationPackage(OrganizationPackageCreateReqVO createReqVO) {
        // 插入
        OrganizationPackageDO organizationPackage = OrganizationPackageConvert.INSTANCE.convert(createReqVO);

        // 如果新增为默认则更新其他默认为非默认
        if(Boolean.TRUE.equals(createReqVO.getIsDefault())){
            updateNoDefault();
        }
        organizationPackageMapper.insert(organizationPackage);
        // 返回
        return organizationPackage.getId();
    }

    @Override
    public void updateOrganizationPackage(OrganizationPackageUpdateReqVO updateReqVO) {
        // 校验存在
        OrganizationPackageDO packageDO = validateOrganizationPackageExists(updateReqVO.getId());
        // 更新
        OrganizationPackageDO updateObj = OrganizationPackageConvert.INSTANCE.convert(updateReqVO);
        // 如果新增为默认则更新其他默认为非默认
        if(Boolean.TRUE.equals(updateReqVO.getIsDefault())){
            updateNoDefault();
        }
        organizationPackageMapper.updateById(updateObj);

        // 如果菜单发生变化，则修改每个租户的菜单
        if (!CollUtil.isEqualList(packageDO.getMenuIds(), updateReqVO.getMenuIds())) {
            List<OrganizationDO> organizations = organizationService.getOrganizationListByPackageId(packageDO.getId());
            organizations.forEach(organization -> organizationService.updateOrganizationRoleMenu(organization.getId(), updateReqVO.getMenuIds()));
        }

    }

    private void updateNoDefault(){
        organizationPackageMapper.update(new OrganizationPackageDO(), new LambdaUpdateWrapper<OrganizationPackageDO>()
                .set(OrganizationPackageDO::getIsDefault, true));
    }

    @Override
    public void deleteOrganizationPackage(Long id) {
        // 校验存在
        validateOrganizationPackageExists(id);
        long count = organizationService.selectByPackageId(id);
        if(count > 0){
            throw exception(ORGANIZATION_PACKAGE_USED);
        }
        // 删除
        organizationPackageMapper.deleteById(id);
    }

    private OrganizationPackageDO validateOrganizationPackageExists(Long id) {
        OrganizationPackageDO packageDO = organizationPackageMapper.selectById(id);
        if ( packageDO == null) {
            throw exception(ORGANIZATION_PACKAGE_NOT_EXISTS);
        }
        return packageDO;
    }

    @Override
    public OrganizationPackageDO getOrganizationPackage(Long id) {
        return organizationPackageMapper.selectById(id);
    }

    @Override
    public List<OrganizationPackageDO> getOrganizationPackageList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return organizationPackageMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<OrganizationPackageDO> getOrganizationPackagePage(OrganizationPackagePageReqVO pageReqVO) {
        return organizationPackageMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OrganizationPackageDO> getOrganizationPackageList(OrganizationPackageExportReqVO exportReqVO) {
        return organizationPackageMapper.selectList(exportReqVO);
    }

    @Override
    public OrganizationPackageDO getOrganizationPackageDefault() {
        List<OrganizationPackageDO> packageDOList = organizationPackageMapper.selectList(OrganizationPackageDO::getIsDefault, true);

        if(CollUtil.isNotEmpty(packageDOList)){
            return packageDOList.get(0);
        }else{
            return null;
        }
    }

}
