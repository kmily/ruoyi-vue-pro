package cn.iocoder.yudao.framework.tenant.core.redis;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.redis.core.TimeoutRedisCacheManager;
import cn.iocoder.yudao.framework.tenant.config.TenantProperties;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import jodd.io.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * 多租户的 {@link RedisCacheManager} 实现类
 *
 * 操作指定 name 的 {@link Cache} 时，自动拼接租户后缀，格式为 name + "::t" + tenantId + 后缀
 *
 * @author airhead
 */
@Slf4j
public class TenantRedisCacheManager extends TimeoutRedisCacheManager {

    /**
     * 多租户 Redis Key 的前缀，补充在原有 name 的 : 后面
     *
     * 原因：如果只补充租户编号，可读性较差
     */
    private static final String PREFIX = "t";

    private final TenantProperties tenantProperties;

    public TenantRedisCacheManager(RedisCacheWriter cacheWriter,
                                   RedisCacheConfiguration defaultCacheConfiguration,
                                   TenantProperties tenantProperties) {
        super(cacheWriter, defaultCacheConfiguration);
        this.tenantProperties = tenantProperties;
    }

    @Override
    public Cache getCache(String name) {
        // 如果不忽略多租户的 Cache，则自动拼接租户后缀
        if (!tenantProperties.getIgnoreCaches().contains(name)) {
            name = name + StrUtil.COLON + PREFIX + TenantContextHolder.getRequiredTenantId();
        }

        // 继续基于父方法
        return super.getCache(name);
    }

}
