package cn.iocoder.yudao.module.steam.controller.app.selling.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class GoodsAbrasionDTO {
    /**
     * itemInfo
     */
    private String itemInfo;
    /**
     * short_name
     */
    private String shortName;
    /**
     * marketHashName
     */
    private String marketHashName;
    /**
     * 外观选择
     */
    private String selExterior;
    /**
     * 出售价格单价分
     */
    private Integer price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodsAbrasionDTO that = (GoodsAbrasionDTO) o;
        return Objects.equals(selExterior, that.selExterior);
    }

}
