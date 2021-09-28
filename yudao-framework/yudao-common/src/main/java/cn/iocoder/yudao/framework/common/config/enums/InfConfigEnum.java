package cn.iocoder.yudao.framework.common.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author syidong@aliyun.com
 * @date 2021/5/12 22:57
 */
@AllArgsConstructor
@Getter
public enum InfConfigEnum {

    YUDAO_WEB_API_LOG_ENABLE_CONFIG("是否开启apiLog key"
            ,"yudao.web.api.log.enable"
            ,Boolean.TRUE.toString()
            ,ConfigGroupConstants.CONFIG_LOG_SWITCH_KEY_GROUP
            ,InfConfigTypeEnum.SYSTEM.getType()
            ,Boolean.FALSE
            ,"api log 是否开启配置")
    ,YUDAO_WEB_API_ERROR_LOG_ENABLE_CONFIG("是否开启api error log  日志 key"
            ,"yudao.web.api.error.log.enable"
            ,Boolean.TRUE.toString()
            ,ConfigGroupConstants.CONFIG_LOG_SWITCH_KEY_GROUP
            ,InfConfigTypeEnum.SYSTEM.getType()
            ,Boolean.FALSE
            ,"api error log 是否开启配置")
    ,YUDAO_OPERATE_LOG_ENABLE_CONFIG("是否开启operateLog key"
            ,"yudao.operate.log.enable"
            ,Boolean.TRUE.toString()
            ,ConfigGroupConstants.CONFIG_LOG_SWITCH_KEY_GROUP
            ,InfConfigTypeEnum.SYSTEM.getType()
            ,Boolean.FALSE
            ,"api error log 是否开启配置");

    private final String name;

    private final String key;

    private final String value;

    private final String group;

    private Integer type;

    private Boolean sensitive;

    private String remark;
}
