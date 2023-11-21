package cn.iocoder.yudao.module.system.service.organizationpackage;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.organizationpackage.OrganizationPackageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 机构套餐 Service 接口
 *
 * @author 芋道源码
 */
public interface OrganizationPackageService extends IService<OrganizationPackageDO>{

    /**
     * 创建机构套餐
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrganizationPackage(@Valid OrganizationPackageCreateReqVO createReqVO);

    /**
     * 更新机构套餐
     *
     * @param updateReqVO 更新信息
     */
    void updateOrganizationPackage(@Valid OrganizationPackageUpdateReqVO updateReqVO);

    /**
     * 删除机构套餐
     *
     * @param id 编号
     */
    void deleteOrganizationPackage(Long id);

    /**
     * 获得机构套餐
     *
     * @param id 编号
     * @return 机构套餐
     */
    OrganizationPackageDO getOrganizationPackage(Long id);

    /**
     * 获得机构套餐列表
     *
     * @param ids 编号
     * @return 机构套餐列表
     */
    List<OrganizationPackageDO> getOrganizationPackageList(Collection<Long> ids);

    /**
     * 获得机构套餐分页
     *
     * @param pageReqVO 分页查询
     * @return 机构套餐分页
     */
    PageResult<OrganizationPackageDO> getOrganizationPackagePage(OrganizationPackagePageReqVO pageReqVO);

    /**
     * 获得机构套餐列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 机构套餐列表
     */
    List<OrganizationPackageDO> getOrganizationPackageList(OrganizationPackageExportReqVO exportReqVO);

    /**
     * 查询默认
     */
    OrganizationPackageDO getOrganizationPackageDefault();
}
