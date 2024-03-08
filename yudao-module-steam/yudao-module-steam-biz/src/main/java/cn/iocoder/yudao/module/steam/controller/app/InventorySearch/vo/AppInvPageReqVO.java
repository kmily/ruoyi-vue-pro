package cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo;

import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import lombok.Data;

@Data
public class AppInvPageReqVO extends InvPageReqVO {

    private String iconUrl;
    private String marketName;

}
