package cn.iocoder.yudao.module.system.api.tenant;

import cn.iocoder.yudao.module.system.api.tenant.dto.TenantDataSourceConfigRespDTO;

import java.util.List;

/**
 * 多租户的 API 接口
 *
 * @author 芋道源码
 */
public interface TenantApi {

    /**
     * 获得所有租户
     *
     * @return 租户编号数组
     */
    List<Long> getTenantIdList();

    /**
     * 校验租户是否合法
     *
     * @param id 租户编号
     */
    void validateTenant(Long id);

    /**
     * 获得租户的数据源配置
     *
     * @param tenantId 租户编号
     * @return 数据源配置
     */
    TenantDataSourceConfigRespDTO getTenantDataSourceConfig(Long tenantId);

}
