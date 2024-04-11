package cn.iocoder.yudao.module.steam.service.fin.vo;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;


@Data
public class ApiOrderCancelRespVo implements Serializable {
    /**
     * 是否成功
     */
    private Boolean isSuccess;
    /**
     * 失败代码
     *  枚举 {@link cn.iocoder.yudao.module.steam.enums.OpenApiCode 对应的类}
     */
    private ErrorCode errorCode;
}
