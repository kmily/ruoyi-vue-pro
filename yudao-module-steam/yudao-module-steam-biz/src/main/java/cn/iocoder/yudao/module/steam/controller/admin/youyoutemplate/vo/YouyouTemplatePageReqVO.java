package cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 悠悠商品模板分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class YouyouTemplatePageReqVO extends PageParam {

    @Schema(description = "武器全称", example = "印花 | Hello 法玛斯")
    private String name;

    @Schema(description = "武器英文全称", example = "Sticker | Hello FAMAS")
    private String hashName;

    @Schema(description = "类型编号", example = "106")
    private Integer typeId;

    @Schema(description = "类型名称", example = "印花")
    private String typeName;

    @Schema(description = "类型英文名称", example = "CSGO_Tool_Sticker")
    private String typeHashName;

    @Schema(description = "武器编号", example = "452")
    private Integer weaponId;

    @Schema(description = "武器名称", example = "压枪")
    private String weaponName;

    @Schema(description = "武器英文名称", example = "crate_sticker_pack_recoil_lootlist")
    private String weaponHashName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "图片地址", example = "https://www.iocoder.cn")
    private String iconUrl;

    @Schema(description = "在售最低价", example = "17990")
    private String minSellPrice;

    @Schema(description = "极速发货在售最低价", example = "12096")
    private String fastShippingMinSellPrice;

    @Schema(description = "模板参考价", example = "25408")
    private String referencePrice;

    @Schema(description = "外观", example = "赵六")
    private String exteriorName;

    @Schema(description = "稀有度", example = "张三")
    private String rarityName;

    @Schema(description = "品质", example = "李四")
    private String qualityName;

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28997")
    @ExcelProperty("模板ID")
    private Integer templateId;

    @Schema(description = "在售数量")
    private String sellNum;

}