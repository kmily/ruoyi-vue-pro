package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;

import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import lombok.Data;
@Data
public class AppInvMergeToSellPageReqVO {

    // 图片
    private String iconUrl;
    // 名称
    private String marketName;
    // 状态
    private Integer status;
    // 发货状态
    private Integer transferStatus;
    // 唯一实例id
    private String assetId;
    // 商品数量
    private Integer number;
    // 价格
    private Integer price;

}
