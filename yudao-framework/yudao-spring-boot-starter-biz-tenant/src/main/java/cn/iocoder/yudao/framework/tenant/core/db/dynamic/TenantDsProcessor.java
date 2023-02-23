package cn.iocoder.yudao.framework.tenant.core.db.dynamic;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Objects;

/**
 * 基于 {@link TenantDS} 的数据源处理器
 *
 * @author 芋道源码
 */
public class TenantDsProcessor extends DsProcessor {

    @Override
    public boolean matches(String key) {
        return Objects.equals(key, TenantDS.KEY);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        Long tenantId = TenantContextHolder.getRequiredTenantId();
        return "tenant_" + tenantId + "_ds";
    }

}
