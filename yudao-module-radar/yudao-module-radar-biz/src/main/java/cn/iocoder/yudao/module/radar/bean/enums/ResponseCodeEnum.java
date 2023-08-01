package cn.iocoder.yudao.module.radar.bean.enums;

/**
 * @author whycode
 * @title: ResponseCodeEnum
 * @projectName blink-distributed
 * @description: TODO
 * @date 2023/5/1715:59
 */
public enum ResponseCodeEnum {
    /**
     * 成功
     */
    SUCCEED(0L, "Succeed"),
    /**
     * 通用错误
     */
    COMMON_ERROR(1L, "Common Error"),
    /**
     * 参数非法
     */
    INVALID_ARGUMENTS(2L, "Invalid Arguments"),
    /**
     * 用户无权限
     */
    NOT_AUTHORIZED(3L, "Not Authorized"),
    /**
     * 设备不支持
     */
    NOT_SUPPORTED(4L, "Not Supported"),
    /**
     * 用户状态异常
     */
    ABNORMAL_USER_STATUS(5L, "Abnormal User Status"),
    /**
     * 批量操作部分成功
     */
    PARTIALLY_SUCCEED(102L, "Partially Succeed"),
    /**
     * 视频流会话不存在
     */
    STREAM_SESSIONS_NOT_EXIST(201L, "Stream Sessions Not Exist"),
    /**
     * 视频流会话已存在
     */
    STREAM_SESSIONS_EXIST(202L, "Stream Sessions Exist"),
    /**
     * 视频流会话已达上限
     */
    STREAM_SESSIONS_UPPER_LIMIT(203L, "Stream Sessions Upper Limit");

    public long code;
    public String msg;

    ResponseCodeEnum(long code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
