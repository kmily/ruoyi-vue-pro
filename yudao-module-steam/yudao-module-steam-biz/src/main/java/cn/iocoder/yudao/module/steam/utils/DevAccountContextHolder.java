package cn.iocoder.yudao.module.steam.utils;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import com.alibaba.ttl.TransmittableThreadLocal;

public class DevAccountContextHolder {

    /**
     * 当前租户编号
     */
    private static final ThreadLocal<DevAccountDO> devAccountDOThreadLocal = new TransmittableThreadLocal<>();


    /**
     * 获得租户编号
     *
     * @return 租户编号
     */
    public static DevAccountDO getDevAccount() {
        return devAccountDOThreadLocal.get();
    }


    /**
     * 获得租户编号。如果不存在，则抛出 NullPointerException 异常
     *
     * @return 租户编号
     */
    public static DevAccountDO getRequiredDevAccount() {
        DevAccountDO devAccountDO = getDevAccount();
        if (devAccountDO == null) {
            throw new ServiceException(-1,"DevAccountContextHolder 不存在");
        }
        return devAccountDO;
    }

    public static void setDevAccount(DevAccountDO devAccountDO) {
        devAccountDOThreadLocal.set(devAccountDO);
    }




    public static void clear() {
        devAccountDOThreadLocal.remove();
    }
}
