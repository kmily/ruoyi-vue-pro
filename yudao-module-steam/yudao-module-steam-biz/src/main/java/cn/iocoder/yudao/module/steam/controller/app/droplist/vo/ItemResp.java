package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ItemResp implements Serializable {
    private String itemName;
    private String shortName;
    private String marketHashName;
    private String itemId;
    private String imageUrl;
    private C5ItemInfo itemInfo;
}
