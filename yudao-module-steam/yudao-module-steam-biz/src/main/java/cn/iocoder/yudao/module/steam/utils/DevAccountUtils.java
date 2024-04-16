package cn.iocoder.yudao.module.steam.utils;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;

import java.util.concurrent.Callable;


public class DevAccountUtils {

    /**
     * 使用指定租户，执行对应的逻辑
     *
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 当然，执行完成后，还是会恢复回去
     *
     * @param tenantId 租户编号
     * @param callable 逻辑
     */
    public static <V> V tenantExecute(Long tenantId, Callable<V> callable) {
        Long oldTenantId = TenantContextHolder.getTenantId();
        Boolean oldIgnore = TenantContextHolder.isIgnore();
        try {
            TenantContextHolder.setTenantId(tenantId);
            TenantContextHolder.setIgnore(false);
            // 执行逻辑
            return callable.call();
        }catch (ServiceException e){
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(-1,e.getMessage());
        } finally {
            TenantContextHolder.setTenantId(oldTenantId);
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }

}
