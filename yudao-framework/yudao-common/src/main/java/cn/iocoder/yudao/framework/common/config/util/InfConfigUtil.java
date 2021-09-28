package cn.iocoder.yudao.framework.common.config.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.config.enums.InfConfigEnum;

import java.util.HashMap;

import static cn.iocoder.yudao.framework.common.config.enums.InfConfigEnum.*;

/**
 * @author syidong@aliyun.com
 * @date 2021/5/13 0:26
 */
public class InfConfigUtil {

    private static final HashMap<String, InfConfigEnum> INIT_CONFIG = new HashMap<>();

    static {
        INIT_CONFIG.put(YUDAO_WEB_API_LOG_ENABLE_CONFIG.getKey(),YUDAO_WEB_API_LOG_ENABLE_CONFIG);
        INIT_CONFIG.put(YUDAO_WEB_API_ERROR_LOG_ENABLE_CONFIG.getKey(),YUDAO_WEB_API_ERROR_LOG_ENABLE_CONFIG);
        INIT_CONFIG.put(YUDAO_OPERATE_LOG_ENABLE_CONFIG.getKey(),YUDAO_OPERATE_LOG_ENABLE_CONFIG);
    }

    public static InfConfigEnum getInitConfigEnum(String key) {
        return INIT_CONFIG.get(key);
    }

    public static boolean isEnable(String key) {
        String enable = SpringUtil.getProperty(key);
        return Boolean.parseBoolean(StrUtil.isBlank(enable)?Boolean.TRUE.toString():enable);
    }

    public static boolean apiLogEnable() {
        return isEnable(YUDAO_WEB_API_LOG_ENABLE_CONFIG.getKey());
    }

    public static boolean apiErrorLogEnable() {
        return isEnable(YUDAO_WEB_API_ERROR_LOG_ENABLE_CONFIG.getKey());
    }

    public static boolean operateLogEnable() {
        return isEnable(YUDAO_OPERATE_LOG_ENABLE_CONFIG.getKey());
    }
}
