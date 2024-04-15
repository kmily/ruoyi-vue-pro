package cn.iocoder.yudao.module.steam.service.fin.vo;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@Data
public class ApiBuyItemRespVo implements Serializable {
    /**
     * 是否成功
     */
    private Boolean isSuccess;
    /**
     * 失败代码
     *  枚举 {@link cn.iocoder.yudao.module.steam.enums.OpenApiCode 对应的类}
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
