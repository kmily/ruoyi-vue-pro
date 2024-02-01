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

    //外观选择
    ErrorCode SEL_EXTERIOR_NOT_EXISTS = new ErrorCode(1_100_002_00, "外观选择不存在");

    //类别选择
    ErrorCode SEL_QUALITY_NOT_EXISTS = new ErrorCode(1_100_003_00, "类别选择不存在");


}
