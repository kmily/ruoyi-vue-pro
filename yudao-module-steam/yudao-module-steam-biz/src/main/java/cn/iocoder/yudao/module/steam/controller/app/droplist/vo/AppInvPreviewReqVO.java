package cn.iocoder.yudao.module.steam.controller.app.droplist.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppInvPreviewReqVO extends PageParam {
//    @NotNull(message = "品质不能为空")
    private String selRarity;
    @Schema(description = "最高价格", example = "2")
//    @NotNull(message = "类别不能为空")
    private String selQuality;
    @Schema(description = "最低价格", example = "1")
//    @NotNull(message = "外观不能为空")
    private String selExterior;
//    @NotNull(message = "收藏品不能为空")
    private String selItemset;
    @NotNull(message = "搜索词不能为空不能为空")
    private String itemName;
}
