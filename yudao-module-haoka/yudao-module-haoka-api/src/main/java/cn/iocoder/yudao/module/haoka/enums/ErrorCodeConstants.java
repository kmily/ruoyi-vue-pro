package cn.iocoder.yudao.module.haoka.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * haoka 错误码枚举类
 * <p>
 * haoka 系统，使用 1-910-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== Demo 1_910_001_000 ==========
    ErrorCode DEMO_NOT_EXISTS = new ErrorCode(1_910_001_001, "好卡案例不存在");

    // ========== Product 1_801_001_000 ==========
    ErrorCode SUPERIOR_API_NOT_EXISTS = new ErrorCode(1_801_001_001, "上游API接口不存在");
    ErrorCode SUPERIOR_API_DEV_CONFIG_NOT_EXISTS = new ErrorCode(1_801_001_002, "上游API接口开发配置不存在");
    ErrorCode SUPERIOR_API_SKU_CONFIG_NOT_EXISTS = new ErrorCode(1_801_001_003, "上游API接口SKU要求配置不存在");
    ErrorCode SUPERIOR_PRODUCT_CONFIG_NOT_EXISTS = new ErrorCode(1_801_001_004, "产品对接上游配置不存在");

}
