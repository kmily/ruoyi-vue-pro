package cn.iocoder.yudao.module.steam.controller.admin.othertemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 其他平台模板新增/修改 Request VO")
@Data
public class OtherTemplateSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5828")
    private Integer id;

    @Schema(description = "出售平台id")
    private Integer platformIdentity;

    @Schema(description = "外观")
    private String exterior;

    @Schema(description = "外观名称", example = "赵六")
    private String exteriorName;

    @Schema(description = "饰品id", example = "30878")
    private Integer itemId;

    @Schema(description = "饰品名称", example = "芋艿")
    private String itemName;

    @Schema(description = "marketHashName", example = "李四")
    private String marketHashName;

    @Schema(description = "自动发货在售最低价", example = "18930")
    private Double autoDeliverPrice;

    @Schema(description = "品质")
    private String quality;

    @Schema(description = "稀有度")
    private String rarity;

    @Schema(description = "steam类型", example = "1")
    private String type;

    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    private String imageUrl;

    @Schema(description = "自动发货在售数量")
    private Integer autoDeliverQuantity;

    @Schema(description = "品质颜色")
    private String qualityColor;

    @Schema(description = "品质名称", example = "张三")
    private String qualityName;

    @Schema(description = "稀有度颜色")
    private String rarityColor;

    @Schema(description = "稀有度名称", example = "张三")
    private String rarityName;

    @Schema(description = "短名称，去掉前缀", example = "李四")
    private String shortName;

    @Schema(description = "steam类型名称", example = "赵六")
    private String typeName;

}