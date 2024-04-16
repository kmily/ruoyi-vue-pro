package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import cn.iocoder.yudao.module.steam.controller.app.selling.vo.SellingReqVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 根据marketHashName查询在售饰品信息(支持批量查询)
 */
@Data
public class V5queryOnSaleInfoReqVO implements Serializable {

    private List<String> templateHashNameList;
    private String merchantKey;

    public V5queryOnSaleInfoReqVO(List<String> marketHashNameList,String merchantKey) {
        this.merchantKey = merchantKey;
        this.templateHashNameList = marketHashNameList;
    }

    public V5queryOnSaleInfoReqVO() {
    }
}
