package cn.iocoder.yudao.module.steam.service.fin.vo;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;


@Data
public class ApiBuyItemRespVo implements Serializable {
    /**
     * 是否成功
     */
    private Boolean isSuccess;
    /**
     * 失败代码
     * 代码参考 OpenApiCode
     */
    private ErrorCode errorCode;
    /**
     * 第三方单据号
     */
    private String orderNo;
    /**
     * 交易链接
     */
    private String tradeLink;
    /**
     * 交易单号
     */
    private String tradeOfferId;
}
