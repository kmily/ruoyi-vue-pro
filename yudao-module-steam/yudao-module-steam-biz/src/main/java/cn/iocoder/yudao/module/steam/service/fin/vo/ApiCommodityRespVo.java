package cn.iocoder.yudao.module.steam.service.fin.vo;

import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiCommodityRespVo implements Serializable {
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
