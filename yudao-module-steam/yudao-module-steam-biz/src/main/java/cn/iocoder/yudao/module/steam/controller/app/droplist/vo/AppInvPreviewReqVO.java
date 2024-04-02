package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppInvPreviewReqVO extends PageParam {
    private String selRarity;

    private String selQuality;

    private String selExterior;

    private String selItemset;

    @NotNull(message = "搜索词不能为空不能为空")
    private String itemName;

    @Schema(description = "最低价格", example = "1")
    private Integer minPrice;

    @Schema(description = "最高价格", example = "2")
    private  Integer maxPrice;

    @Schema(description = "排序字段autoPrice 传0,1,2 分别代表默认排序,升序，降序")
    private String sort;

    private String type;

}
