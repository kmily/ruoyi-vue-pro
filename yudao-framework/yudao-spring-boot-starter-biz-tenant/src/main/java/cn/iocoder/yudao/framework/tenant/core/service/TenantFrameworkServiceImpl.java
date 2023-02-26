package cn.iocoder.yudao.framework.tenant.core.service;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.cache.CacheUtils;
import cn.iocoder.yudao.module.infra.api.db.dto.DataSourceConfigRespDTO;
import cn.iocoder.yudao.module.system.api.tenant.TenantApi;
import cn.iocoder.yudao.module.system.api.tenant.dto.TenantDataSourceConfigRespDTO;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.List;

/**
 * Tenant 框架 Service 实现类
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
public class TenantFrameworkServiceImpl implements TenantFrameworkService {

    private static final ServiceException SERVICE_EXCEPTION_NULL = new ServiceException();

    private final TenantApi tenantApi;

    /**
     * 针对 {@link #getTenantIds()} 的缓存
     */
    private final LoadingCache<Object, List<Long>> getTenantIdsCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // 过期时间 1 分钟
            new CacheLoader<Object, List<Long>>() {

                @Override
                public List<Long> load(Object key) {
                    return tenantApi.getTenantIdList();
                }

            });

    /**
     * 针对 {@link #validTenant(Long)} 的缓存
     */
    private final LoadingCache<Long, ServiceException> validTenantCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // 过期时间 1 分钟
            new CacheLoader<Long, ServiceException>() {

                @Override
                public ServiceException load(Long id) {
                    try {
                        tenantApi.validateTenant(id);
                        return SERVICE_EXCEPTION_NULL;
                    } catch (ServiceException ex) {
                        return ex;
                    }
                }

            });

    /**
     * 针对 {@link #getDataSourceProperty(Long)} 的缓存
     */
    private final LoadingCache<Long, DataSourceProperty> dataSourcePropertyCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // 过期时间 1 分钟
            new CacheLoader<Long, DataSourceProperty>() {

                @Override
                public DataSourceProperty load(Long id) {
                    // 获得租户对应的数据源配置
                    TenantDataSourceConfigRespDTO dataSourceConfig = tenantApi.getTenantDataSourceConfig(id);
                    if (dataSourceConfig == null) {
                        return null;
                    }
                    // 转换成 dynamic-datasource 配置
                    return new DataSourceProperty()
                            .setPoolName(dataSourceConfig.getName()).setUrl(dataSourceConfig.getUrl())
                            .setUsername(dataSourceConfig.getUsername()).setPassword(dataSourceConfig.getPassword());
                }

            });

    @Override
    @SneakyThrows
    public List<Long> getTenantIds() {
        return getTenantIdsCache.get(Boolean.TRUE);
    }

    @Override
    public void validTenant(Long id) {
        ServiceException serviceException = validTenantCache.getUnchecked(id);
        if (serviceException != SERVICE_EXCEPTION_NULL) {
            throw serviceException;
        }
    }

    @Override
    @SneakyThrows
    public DataSourceProperty getDataSourceProperty(Long id) {
        return dataSourcePropertyCache.get(id);
    }

}
