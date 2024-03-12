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

    //类型选择
    ErrorCode SEL_TYPE_NOT_EXISTS = new ErrorCode(1_100_003_001, "类型选择不存在");
    ErrorCode SEL_WEAPON_NOT_EXISTS = new ErrorCode(1_100_003_002, "武器选择不存在");

    ErrorCode SEL_RARITY_NOT_EXISTS = new ErrorCode(1_100_003_003, "品质选择不存在");

    ErrorCode SEL_EXTERIOR_NOT_EXISTS = new ErrorCode(1_100_003_01, "外观选择不存在");

    ErrorCode SEL_QUALITY_NOT_EXISTS = new ErrorCode(1_100_003_01, "类别选择不存在");
    //开放平台
    ErrorCode DEV_ACCOUNT_NOT_EXISTS = new ErrorCode(1_100_007_01, "开放平台用户不存在");
    ErrorCode DEV_ACCOUNT_DISABLE = new ErrorCode(1_100_007_02, "开放平台用户未审核");

    //库存信息
    ErrorCode INV_DESC_NOT_EXISTS = new ErrorCode(1_100_008_01, "库存信息详情不存在");
    ErrorCode INV_NOT_EXISTS = new ErrorCode(1_100_008_02, "steam用户库存储不存在");
    // ========== steam订单
    ErrorCode INV_ORDER_NOT_EXISTS = new ErrorCode(1_100_009_01, "steam订单不存在");
    // ========== 提现
    ErrorCode WITHDRAWAL_NOT_EXISTS = new ErrorCode(1_100_010_01, "提现不存在");
    ErrorCode WITHDRAWAL_AMOUNT_EXCEPT = new ErrorCode(1_100_010_02, "提现金额不正确");
    ErrorCode WITHDRAWAL_USER_EXCEPT = new ErrorCode(1_100_010_03, "用户不存在");
    ErrorCode WITHDRAWAL_CANNOTDO = new ErrorCode(1_100_010_03, "收款帐号不允许提现");

    ErrorCode WITHDRAWAL_AUDIT = new ErrorCode(1_100_010_04, "审核中");
    ErrorCode WITHDRAWAL_AUDIT_FAIL = new ErrorCode(1_100_010_04, "审核未通过");
    //---库存订单
    ErrorCode INVORDER_INV_NOT_FOUND = new ErrorCode(1_100_011_02, "库存不存在");
    ErrorCode INVORDER_ORDERED_EXCEPT = new ErrorCode(1_100_011_01, "此库存已经存在有效订单");
    ErrorCode INVORDER_USER_EXCEPT = new ErrorCode(1_100_011_01, "用户不存在");
    ErrorCode INVORDER_ORDERUSER_EXCEPT = new ErrorCode(1_100_011_01, "此库存已经在此用户了，无需要交易");
    ErrorCode INVORDER_BIND_STEAM_EXCEPT = new ErrorCode(1_100_011_01, "绑定steam账号异常");
    ErrorCode INVORDER_AMOUNT_EXCEPT = new ErrorCode(1_100_011_03, "金额不正确");
    ErrorCode INVORDER_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID = new ErrorCode(1_100_011_04, "订单不是【未支付】状态");

    ErrorCode INVORDER_ORDER_CASHED_CANNOTREFUND = new ErrorCode(1_100_011_04, "款项已打款给买家，不能退款");
    ErrorCode INVORDER_ORDER_NOT_FOUND = new ErrorCode(1_007_900_000, "订单不存在");
    ErrorCode INVORDER_ORDER_REFUND_USER_ERROR = new ErrorCode(1_007_900_005, "发起退款失败，只有订单发起人才能退款");
    ErrorCode INVORDER_ORDER_REFUND_FAIL_NOT_PAID = new ErrorCode(1_007_900_005, "发起退款失败，订单未支付");
    ErrorCode INVORDER_ORDER_REFUND_FAIL_REFUNDED = new ErrorCode(1_007_900_006, "发起退款失败，订单已退款");
    ErrorCode INVORDER_ORDER_UPDATE_PAID_FAIL_PAY_ORDER_ID_ERROR = new ErrorCode(1_007_900_002, "更新支付状态失败，支付单编号不匹配");
    ErrorCode INVORDER_ORDER_UPDATE_PAID_FAIL_PAY_ORDER_STATUS_NOT_SUCCESS = new ErrorCode(1_007_900_003, "示例订单更新支付状态失败，支付单状态不是【支付成功】状态");
    ErrorCode INVORDER_ORDER_UPDATE_PAID_FAIL_PAY_PRICE_NOT_MATCH = new ErrorCode(1_007_900_004, "更新支付状态失败，支付单金额不匹配");
    ErrorCode INVORDER_ORDER_TRANSFER_ALERY = new ErrorCode(1_007_900_006, "已发货不支持退货");

    ErrorCode INVORDER_ORDER_REFUND_FAIL_REFUND_ORDER_ID_ERROR = new ErrorCode(1_007_900_009, "发起退款失败，退款单编号不匹配");
    ErrorCode INVORDER_ORDER_REFUND_FAIL_REFUND_NOT_FOUND = new ErrorCode(1_007_900_007, "发起退款失败，退款订单不存在");
    ErrorCode INVORDER_ORDER_REFUND_FAIL_REFUND_PRICE_NOT_MATCH = new ErrorCode(1_007_900_010, "发起退款失败，退款单金额不匹配");
    ErrorCode INVORDER_ORDER_REFUND_FAIL_REFUND_NOT_SUCCESS = new ErrorCode(1_007_900_008, "发起退款失败，退款订单未退款成功");
    //UU订单
    ErrorCode UU_GOODS_ERR = new ErrorCode(1_008_001_01, "商品ID或模板ID或模板hashname,不能为空");
    ErrorCode UU_GOODS_NOT_FOUND = new ErrorCode(1_100_011_02, "库存不存在");
    ErrorCode UU_GOODS_ORDERED_EXCEPT = new ErrorCode(1_100_011_01, "此库存已经存在有效订单");
    ErrorCode UU_GOODS_AMOUNT_EXCEPT = new ErrorCode(1_100_011_03, "金额不正确");
    ErrorCode UU_GOODS_ORDER_UPDATE_PAID_STATUS_NOT_UNPAID = new ErrorCode(1_100_011_04, "订单不是【未支付】状态");
    ErrorCode UU_GOODS_ORDER_MIN_TIME = new ErrorCode(3, "创单成功后30min后卖家未发送交易报价才能取消");
    ErrorCode UU_GOODS_ORDER_CAN_NOT_CANCEL = new ErrorCode(3, "此状态下不支持取消");
    ErrorCode UU_GOODS_ORDER_TRANSFER_CASHED = new ErrorCode(1_100_011_05, "卖家已收款，不支付退款");
    // 饰品在售预览
    ErrorCode INV_PREVIEW_NOT_EXISTS = new ErrorCode(1_100_010_01, "饰品在售预览不存在");

    // ========== 在售饰品  ==========
    ErrorCode SELLING_NOT_EXISTS = new ErrorCode(1_200_010_01, "在售饰品不存在");
    // ========== 广告位 TODO 补充编号 ==========
    ErrorCode AD_BLOCK_NOT_EXISTS = new ErrorCode(1_100_011_01, "广告位不存在");
    // ========== 悠悠商品数据  ==========
    ErrorCode YOUYOU_TEMPLATE_NOT_EXISTS = new ErrorCode(1_300_010_01, "悠悠商品数据不存在");
    // ========== steam订单 TODO 补充编号 ==========
    ErrorCode YOUYOU_ORDER_NOT_EXISTS = new ErrorCode(1_100_012_01, "steam订单不存在");

    // ========== 悠悠商品列表 TODO 补充编号 ==========
    ErrorCode YOUYOU_COMMODITY_NOT_EXISTS = new ErrorCode(1_300_010_02, "悠悠商品列表不存在");
    // ========== 悠悠商品列表 TODO 补充编号 ==========
    ErrorCode YOUYOU_DETAILS_NOT_EXISTS = new ErrorCode(1_400_010_01, "请求明细不存在");

    // ========== 绑定用户IP地址池 TODO 补充编号 ==========
    ErrorCode BIND_IPADDRESS_NOT_EXISTS = new ErrorCode(1_500_010_01, "绑定用户IP地址池不存在");



}
