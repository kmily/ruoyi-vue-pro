package cn.iocoder.yudao.module.system.api.tenant;

import cn.iocoder.yudao.module.infra.api.db.DataSourceConfigServiceApi;
import cn.iocoder.yudao.module.system.api.tenant.dto.TenantDataSourceConfigRespDTO;
import cn.iocoder.yudao.module.system.convert.tenant.TenantConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.system.service.tenant.TenantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 多租户的 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class TenantApiImpl implements TenantApi {

    @Resource
    private TenantService tenantService;

    @Resource
    private DataSourceConfigServiceApi dataSourceConfigServiceApi;

    @Override
    public List<Long> getTenantIdList() {
        return tenantService.getTenantIdList();
    }

    @Override
    public void validateTenant(Long id) {
        tenantService.validTenant(id);
    }

    @Override
    public TenantDataSourceConfigRespDTO getTenantDataSourceConfig(Long tenantId) {
        // 获得租户信息
        TenantDO tenant = tenantService.getTenant(tenantId);
        if (tenant == null) {
            return null;
        }
        // 获得租户的数据源配置
        return TenantConvert.INSTANCE.convert(
                dataSourceConfigServiceApi.getDataSourceConfig(tenant.getDataSourceConfigId()));
    }

}
