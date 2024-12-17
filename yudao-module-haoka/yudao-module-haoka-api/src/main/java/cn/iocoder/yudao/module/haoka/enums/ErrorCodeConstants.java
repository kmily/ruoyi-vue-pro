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
    ErrorCode PRODUCT_NOT_EXISTS = new ErrorCode(1_801_001_005, "产品/渠道不存在");
    ErrorCode PRODUCT_CHANNEL_NOT_EXISTS = new ErrorCode(1_801_001_005, "产品的渠道不存在");
    ErrorCode PRODUCT_LIMIT_CARD_NOT_EXISTS = new ErrorCode(1_801_001_005, "产品身份证限制不存在");
    ErrorCode PRODUCT_LIMIT_NOT_EXISTS = new ErrorCode(1_801_001_005, "产品限制条件不存在");
    ErrorCode PRODUCT_LIMIT_AREA_NOT_EXISTS = new ErrorCode(1_801_001_005, "产品区域配置不存在");
    ErrorCode PRODUCT_TYPE_NOT_EXISTS = new ErrorCode(1_801_001_005, "产品类型不存在");


    ErrorCode HAO_KA_PRODUCT_NOT_EXISTS = new ErrorCode(1_801_001_005, "产品/渠道不存在");

    ErrorCode  ON_SALE_PRODUCT_NOT_EXISTS = new ErrorCode(1_801_001_021, "在售产品不存在");
}
