package cn.iocoder.yudao.module.radar.job;

import cn.iocoder.yudao.framework.common.util.cache.CacheUtils;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.radar.api.device.DeviceApi;
import cn.iocoder.yudao.module.radar.api.device.dto.DeviceDTO;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author whycode
 * @title: DeviceCache
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/310:42
 */

@Component
public class DeviceCache {

    @Resource
    private DeviceApi deviceApi;

    private final LoadingCache<String, DeviceDTO> deviceCache = CacheUtils.buildAsyncReloadingCache(Duration.ofHours(1L), new CacheLoader<String, DeviceDTO>() {
        @NotNull
        @Override
        public DeviceDTO load(@NotNull String key) throws Exception {
            Boolean oldIgnore = TenantContextHolder.isIgnore();
            try {
                TenantContextHolder.setIgnore(true);
                // 执行逻辑
                return deviceApi.queryBySn(key);
            } finally {
                TenantContextHolder.setIgnore(oldIgnore);
            }
        }
    });


    public DeviceDTO getBySn(String sn){
        return deviceCache.getUnchecked(sn);
    }

}
