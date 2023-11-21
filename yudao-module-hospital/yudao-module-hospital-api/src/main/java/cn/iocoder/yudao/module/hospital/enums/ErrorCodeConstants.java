package cn.iocoder.yudao.module.hospital.enums;// TODO 待办：请将下面的错误码复制到 yudao-module-hospital-api 模块的 ErrorCodeConstants 类中。注意，请给“TODO 补充编号”设置一个错误码编号！！！
// ========== 组织机构 TODO 补充编号 ==========


import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    ErrorCode ORGANIZATION_NOT_EXISTS = new ErrorCode(100010, "机构不存在");

    ErrorCode STORE_NAME_EXIST_ERROR = new ErrorCode(100011, "机构名称已存在");

    ErrorCode STORE_APPLY_DOUBLE_ERROR = new ErrorCode(100012, "已经拥有机构!");

    ErrorCode STORE_CLOSE_ERROR = new ErrorCode(100013, "店铺关闭，请联系管理员");


    /**   医护任务 */
    ErrorCode MEDICAL_CARE_NOT_EXISTS = new ErrorCode(200010, "用户不存在");
    ErrorCode MEDICAL_CARE_HAS_EXISTS = new ErrorCode(200011, "用户已存在");
    ErrorCode MEDICAL_CARE_CREATE_FAIL = new ErrorCode(200012, "用户创建失败!");


    /** 医院菜单 */
    ErrorCode MENU_NOT_EXISTS = new ErrorCode(300010, "菜单不存在");

    /** 资质信息 */
    ErrorCode APTITUDE_NOT_EXISTS = new ErrorCode(400010, "资质信息不存在");
    ErrorCode APTITUDE_NOT_ENABLE = new ErrorCode(400011, "资质未启用");
    ErrorCode CARE_APTITUDE_NOT_EXISTS = new ErrorCode(500010, "资质信息不存在");
}
