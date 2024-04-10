package cn.iocoder.yudao.module.steam.service.fin.vo;

import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;

import java.io.Serializable;

public class ApiCommodityRespVo implements Serializable {
    /**
     * 商品价格
     */
    private Integer price;
    /**
     * 平台
     */
    private PlatCodeEnum platCode;
}
