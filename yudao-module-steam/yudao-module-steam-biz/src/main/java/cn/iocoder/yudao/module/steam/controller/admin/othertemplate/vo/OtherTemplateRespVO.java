package cn.iocoder.yudao.module.steam.controller.admin.othertemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 其他平台模板 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OtherTemplateRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5828")
    @ExcelProperty("id")
    private Integer id;

    @Schema(description = "出售平台id")
    @ExcelProperty("出售平台id")
    private Integer platformIdentity;

    @Schema(description = "外观")
    @ExcelProperty("外观")
    private String exterior;

    @Schema(description = "外观名称", example = "赵六")
    @ExcelProperty("外观名称")
    private String exteriorName;

    @Schema(description = "饰品id", example = "30878")
    @ExcelProperty("饰品id")
    private Integer itemId;

    @Schema(description = "饰品名称", example = "芋艿")
    @ExcelProperty("饰品名称")
    private String itemName;

    @Schema(description = "marketHashName", example = "李四")
    @ExcelProperty("marketHashName")
    private String marketHashName;

    @Schema(description = "自动发货在售最低价", example = "18930")
    @ExcelProperty("自动发货在售最低价")
    private Double autoDeliverPrice;

    @Schema(description = "品质")
    @ExcelProperty("品质")
    private String quality;

    @Schema(description = "稀有度")
    @ExcelProperty("稀有度")
    private String rarity;

    @Schema(description = "steam类型", example = "1")
    @ExcelProperty("steam类型")
    private String type;

    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    @ExcelProperty("图片地址")
    private String imageUrl;

    @Schema(description = "自动发货在售数量")
    @ExcelProperty("自动发货在售数量")
    private Integer autoDeliverQuantity;

    @Schema(description = "品质颜色")
    @ExcelProperty("品质颜色")
    private String qualityColor;

    @Schema(description = "品质名称", example = "张三")
    @ExcelProperty("品质名称")
    private String qualityName;

    @Schema(description = "稀有度颜色")
    @ExcelProperty("稀有度颜色")
    private String rarityColor;

    @Schema(description = "稀有度名称", example = "张三")
    @ExcelProperty("稀有度名称")
    private String rarityName;

    @Schema(description = "短名称，去掉前缀", example = "李四")
    @ExcelProperty("短名称，去掉前缀")
    private String shortName;

    @Schema(description = "steam类型名称", example = "赵六")
    @ExcelProperty("steam类型名称")
    private String typeName;

}