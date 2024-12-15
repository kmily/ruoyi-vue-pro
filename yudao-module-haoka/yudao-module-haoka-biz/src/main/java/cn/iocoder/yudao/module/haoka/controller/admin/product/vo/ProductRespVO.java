package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 产品/渠道 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductRespVO {

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3973")
    @ExcelProperty("产品ID")
    private Long id;

    @Schema(description = "运营商", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "运营商", converter = DictConvert.class)
    @DictFormat("haoka_operator") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer operator;

    @Schema(description = "产品编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("产品编码")
    private String sku;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("产品名称")
    private String name;

    @Schema(description = "产品类型", example = "21014")
    @ExcelProperty("产品类型")
    private Long haokaProductTypeId;

    @Schema(description = "归属地", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("归属地")
    private Integer belongAreaCode;

    @Schema(description = "产品渠道", example = "6850")
    @ExcelProperty("产品渠道")
    private Long haokaProductChannelId;

    @Schema(description = "产品限制", requiredMode = Schema.RequiredMode.REQUIRED, example = "31322")
    @ExcelProperty("产品限制")
    private Long haokaProductLimitId;

    @Schema(description = "身份证号码验证", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "身份证号码验证", converter = DictConvert.class)
    @DictFormat("id_card_num_verify") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer idCardNumVerify;

    @Schema(description = "身份证图片验证", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "身份证图片验证", converter = DictConvert.class)
    @DictFormat("id_card_img_verify") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer idCardImgVerify;

    @Schema(description = "生产地址")
    @ExcelProperty("生产地址")
    private String produceAddress;

    @Schema(description = "黑名单过滤", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("黑名单过滤")
    private Boolean needBlacklistFilter;

    @Schema(description = "是否启用库存限制")
    @ExcelProperty("是否启用库存限制")
    private Boolean enableStockLimit;

    @Schema(description = "库存数量")
    @ExcelProperty("库存数量")
    private Integer stockNum;

    @Schema(description = "库存报警数量")
    @ExcelProperty("库存报警数量")
    private Integer stockWarnNum;

    @Schema(description = "生产备注")
    @ExcelProperty("生产备注")
    private String produceRemarks;

    @Schema(description = "结算规则")
    @ExcelProperty("结算规则")
    private String settlementRules;

    @Schema(description = "预估收益")
    @ExcelProperty("预估收益")
    private String estimatedRevenue;

    @Schema(description = "上架")
    @ExcelProperty("上架")
    private Boolean onSale;

    @Schema(description = "是否顶置")
    @ExcelProperty("是否顶置")
    private Boolean isTop;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}