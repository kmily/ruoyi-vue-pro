package cn.iocoder.yudao.module.steam.utils;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;

import java.util.concurrent.Callable;


public class DevAccountUtils {

    /**
     * 使用指定租户，执行对应的逻辑
     *
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 当然，执行完成后，还是会恢复回去
     *
     * @param devAccountDO 租户编号
     * @param runnable 逻辑
     */
    public static void execute(DevAccountDO devAccountDO, Runnable runnable) {
        try {
            DevAccountContextHolder.setDevAccount(devAccountDO);
            // 执行逻辑
            runnable.run();
        } finally {
            DevAccountContextHolder.clear();
        }
    }

    /**
     * 使用指定租户，执行对应的逻辑
     *
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 当然，执行完成后，还是会恢复回去
     *
     * @param devAccountDO 租户编号
     * @param callable 逻辑
     */
    public static <V> V execute(DevAccountDO devAccountDO, Callable<V> callable) {
        try {
            DevAccountContextHolder.setDevAccount(devAccountDO);
            // 执行逻辑
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DevAccountContextHolder.clear();
        }
    }
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
            throw e;
        } catch (Exception e) {
            throw new ServiceException(-1,e.getMessage());
        } finally {
            TenantContextHolder.setTenantId(oldTenantId);
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }

}
