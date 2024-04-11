package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import cn.iocoder.yudao.module.steam.controller.app.selling.vo.SellingReqVo;
import lombok.Data;

/**
 * 根据marketHashName查询在售饰品信息(支持批量查询)
 */
@Data
public class V5queryOnSaleInfoReqVO {

    private String[] templateHashNameList;
    private String merchantKey;
}
