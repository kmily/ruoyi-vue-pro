package cn.iocoder.yudao.module.steam.service.fin.c5.vo;

import java.io.Serializable;
import java.util.List;

public class C5ProductVo implements Serializable {
    /**
     * 游戏id，配合marketHashName,示例值(730)
     */
    private Integer appId;

    private List<String> marketHashNameList;
    public C5ProductVo(Integer appId,  List<String> marketHashNameList) {
        this.appId = appId;
        this.marketHashNameList = marketHashNameList;
    }
}
