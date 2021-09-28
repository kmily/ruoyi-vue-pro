package cn.iocoder.yudao.framework.common.enums;

/**
 * @author syidong@aliyun.com
 * @date 2021/5/10 23:40
 */
public interface ConfigKeyConstants {

    /**
     * 是否开启apiLog key
     */
    String YUDAO_WEB_API_LOG_ENABLE_KEY = "yudao.web.api.log.enable";

    /**
     * 是否开启api error log  日志 key
     */
    String YUDAO_WEB_API_ERROR_LOG_ENABLE_KEY = "yudao.web.api.error.og.enable";

    /**
     * 是否开启operateLog key
     */
    String YUDAO_OPERATE_LOG_KEY = "yudao.operateLog.enable";

    /**
     * spring value Key
     */
    String SPRING_YUDAO_OPERATE_LOG_KEY = "${"+YUDAO_OPERATE_LOG_KEY+":true}";
}
