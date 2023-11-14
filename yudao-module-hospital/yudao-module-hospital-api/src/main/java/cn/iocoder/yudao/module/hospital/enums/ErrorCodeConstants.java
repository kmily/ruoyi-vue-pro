package cn.iocoder.yudao.module.hospital.enums;// TODO 待办：请将下面的错误码复制到 yudao-module-hospital-api 模块的 ErrorCodeConstants 类中。注意，请给“TODO 补充编号”设置一个错误码编号！！！
// ========== 组织机构 TODO 补充编号 ==========


import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    ErrorCode ORGANIZATION_NOT_EXISTS = new ErrorCode(100010, "机构不存在");

    ErrorCode STORE_NAME_EXIST_ERROR = new ErrorCode(100011, "机构名称已存在");

    ErrorCode STORE_APPLY_DOUBLE_ERROR = new ErrorCode(100012, "已经拥有机构!");
}
