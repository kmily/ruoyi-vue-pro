package cn.iocoder.yudao.module.steam.service.fin.vo;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiCommodityRespVo implements Serializable {
    /**
     * 是否成功
     */
    private Boolean isSuccess;
    /**
     * 失败代码
     * 代码参考 OpenApiCode
     *  枚举 {@link cn.iocoder.yudao.module.steam.enums.OpenApiCode 对应的类}
     */
    private ErrorCode errorCode;
    /**
     * 商品价格
     * 单位分
     */
    private Integer price;
    /**
     * 平台
     */
    private PlatCodeEnum platCode;
}
