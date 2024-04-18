package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class IO661QueryOnSaleInfo {

    private String marketHashName;
    // 在售数量
    private Integer onSaleStock;
    // 在售最低价
    private Integer minSellPrice;

}
