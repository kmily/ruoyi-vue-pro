package cn.iocoder.yudao.module.scan.enums;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * scan 错误码 Core 枚举类
 *
 * scan 系统，使用 1-010-000-000 段
 */
public interface ErrorCodeConstants {
    /***
     * ========== device 模块 1-010-000-000 ==========
     */
    ErrorCode DEVICE_NOT_EXISTS = new ErrorCode(1010000000, "设备不存在");
    ErrorCode DEVICE_BIND_EXISTS = new ErrorCode(1010010000, "设备已经绑定");

    /***
     * ========== app_version 模块 1-010-001-000 ==========
     */
    ErrorCode APP_VERSION_NOT_EXISTS = new ErrorCode(1010001000, "应用版本记录不存在");


    /***
     * ========== report 模块 1-010-002-000 ==========
     */
    ErrorCode REPORT_NOT_EXISTS = new ErrorCode(1010002000, "报告不存在");
    ErrorCode REPORT_GENERTOR_FAILURE = new ErrorCode(1010002001, "报告生成失败");

    /***
     * ========== users 模块 1-010-003-000 ==========
     */
    ErrorCode USERS_NOT_EXISTS = new ErrorCode(1010003000 , "扫描用户不存在");
    ErrorCode USERS_HAS_REGISTER = new ErrorCode(1010003001 , "已经注册");

    /***
     * ========== shape_solution 模块 1-010-004-000 ==========
     */
    ErrorCode SHAPE_SOLUTION_NOT_EXISTS = new ErrorCode(1010004000 , "体型解决方案不存在");

    /***
     * ========== shape 模块 1-010-005-000 ==========
     */
    ErrorCode SHAPE_NOT_EXISTS = new ErrorCode(1010005000 , "体型不存在");
}
