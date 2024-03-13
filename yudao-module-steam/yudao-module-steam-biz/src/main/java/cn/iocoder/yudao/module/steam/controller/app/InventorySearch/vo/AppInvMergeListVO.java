package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;

import lombok.Data;

import java.util.List;

@Data
public class AppInvMergeListVO {

    // 名称
    private String marketName;
    // 商品数量
    private Integer number;
    // 资产id
    private List<String> assetId;
}
