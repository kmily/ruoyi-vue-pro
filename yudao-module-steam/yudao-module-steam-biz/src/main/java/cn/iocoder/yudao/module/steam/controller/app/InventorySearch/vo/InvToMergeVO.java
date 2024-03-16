package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;

import lombok.Data;

/**
 * 合并库存查询入参
 */

@Data
public class InvToMergeVO {

    /**
     * 库存状态
     */
    private Integer transferStatus;

    /**
     * 可出售的相同商品数量
     */
    private Integer mergeNumber;

    /**
     * steamId
     */
    private String steamId;
}
