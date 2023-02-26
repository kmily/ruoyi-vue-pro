package cn.iocoder.yudao.framework.tenant.core.service;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;

import java.util.List;

/**
 * Tenant 框架 Service 接口，定义获取租户信息
 *
 * @author 芋道源码
 */
public interface TenantFrameworkService {

    /**
     * 获得所有租户
     *
     * @return 租户编号数组
     */
    List<Long> getTenantIds();

    /**
     * 校验租户是否合法
     *
     * @param id 租户编号
     */
    void validTenant(Long id);

    /**
     * 获得租户对应的数据源配置
     *
     * @param id 租户编号
     * @return 数据源配置
     */
    DataSourceProperty getDataSourceProperty(Long id);

}
