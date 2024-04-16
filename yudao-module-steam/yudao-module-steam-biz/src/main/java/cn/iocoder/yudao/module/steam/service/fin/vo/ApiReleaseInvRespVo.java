package cn.iocoder.yudao.module.steam.service.fin.vo;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;


@Data
public class ApiReleaseInvRespVo implements Serializable {
    /**
     * 是否成功
     */
    private Boolean isSuccess;
    /**
     * 失败代码
     * 代码参考 OpenApiCode
     */
    private ErrorCode errorCode;
}
