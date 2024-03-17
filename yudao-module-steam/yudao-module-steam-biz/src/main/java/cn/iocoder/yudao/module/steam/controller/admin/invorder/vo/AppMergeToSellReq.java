package cn.iocoder.yudao.module.steam.controller.admin.invorder.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AppMergeToSellReq implements Serializable {
    /**
     * steamId
     */
    @NotNull(message = "SteamId必须输入")
    private String steamId;

    private Integer searchType;
}
