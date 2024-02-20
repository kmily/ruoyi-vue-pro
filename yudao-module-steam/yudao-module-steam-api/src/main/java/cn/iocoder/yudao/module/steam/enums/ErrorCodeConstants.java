package cn.iocoder.yudao.module.steam.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * CRM 错误码枚举类
 * <p>
 * crm 系统，使用 1-020-000-000 段
 */
public interface ErrorCodeConstants {
    ErrorCode BIND_USER_NOT_EXISTS = new ErrorCode(1_100_001_001, " steam用户绑定不存在");
    //收藏品选择
    ErrorCode SEL_ITEMSET_NOT_EXISTS = new ErrorCode(1_100_002_001, "收藏品选择不存在");
    ErrorCode SEL_ITEMSET_EXITS_CHILDREN = new ErrorCode(1_100_002_002, "存在存在子收藏品选择，无法删除");
    ErrorCode SEL_ITEMSET_PARENT_NOT_EXITS = new ErrorCode(1_102_001_003,"父级收藏品选择不存在");
    ErrorCode SEL_ITEMSET_PARENT_ERROR = new ErrorCode(1_100_002_004, "不能设置自己为父收藏品选择");
    ErrorCode SEL_ITEMSET_INTERNAL_NAME_DUPLICATE = new ErrorCode(1_100_002_005, "已经存在该英文名称的收藏品选择");
    ErrorCode SEL_ITEMSET_PARENT_IS_CHILD = new ErrorCode(1_100_002_006, "不能设置自己的子SelItemset为父SelItemset");

    // 下拉选择菜单不存在
    ErrorCode CLASS_CHOOSE_NOT_EXISTS = new ErrorCode(1_100_003, "下拉选择菜单不存在");
    //类型选择
    ErrorCode SEL_TYPE_NOT_EXISTS = new ErrorCode(1_100_003_001, "类型选择不存在");
    ErrorCode SEL_WEAPON_NOT_EXISTS = new ErrorCode(1_100_003_002, "武器选择不存在");

    ErrorCode SEL_RARITY_NOT_EXISTS = new ErrorCode(1_100_003_003, "品质选择不存在");

    ErrorCode SEL_EXTERIOR_NOT_EXISTS = new ErrorCode(1_100_003_01, "外观选择不存在");

    ErrorCode SEL_QUALITY_NOT_EXISTS = new ErrorCode(1_100_003_01, "类别选择不存在");
    //开放平台
    ErrorCode DEV_ACCOUNT_NOT_EXISTS = new ErrorCode(1_100_007_01, "开放平台用户不存在");

    //库存信息
    ErrorCode INV_DESC_NOT_EXISTS = new ErrorCode(1_100_008_01, "库存信息详情不存在");
    ErrorCode INV_NOT_EXISTS = new ErrorCode(1_100_008_02, "steam用户库存储不存在");
    // ========== steam订单
    ErrorCode INV_ORDER_NOT_EXISTS = new ErrorCode(1_100_009_01, "steam订单不存在");
    // ========== 提现
    ErrorCode WITHDRAWAL_NOT_EXISTS = new ErrorCode(1_100_010_01, "提现不存在");
}
