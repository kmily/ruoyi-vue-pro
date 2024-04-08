package cn.iocoder.yudao.module.steam.dal.mysql.selling;

import lombok.Data;

import java.io.Serializable;

@Data
public class SellingHashSummary implements Serializable {
    String marketHashName;
    String iconUrl;
    String selQuality;
    String selExterior;
    String selRarity;
    Integer minSellPrice;
    Integer sellNum;
}
