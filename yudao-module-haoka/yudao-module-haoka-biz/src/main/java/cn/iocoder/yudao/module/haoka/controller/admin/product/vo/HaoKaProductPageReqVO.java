package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 产品/渠道分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HaoKaProductPageReqVO extends PageParam {

    @Schema(description = "运营商")
    private Integer operator;

    @Schema(description = "产品编码")
    private String sku;

    @Schema(description = "产品名称", example = "赵六")
    private String name;

    @Schema(description = "产品类型", example = "21014")
    private Long haokaProductTypeId;

    @Schema(description = "归属地")
    private Integer belongAreaCode;

    @Schema(description = "产品渠道", example = "6850")
    private Long haokaProductChannelId;

    @Schema(description = "产品限制", example = "31322")
    private Long haokaProductLimitId;

    @Schema(description = "身份证号码验证")
    private Integer idCardNumVerify;

    @Schema(description = "身份证图片验证")
    private Integer idCardImgVerify;

    @Schema(description = "生产地址")
    private String produceAddress;

    @Schema(description = "黑名单过滤")
    private Boolean needBlacklistFilter;

    @Schema(description = "是否启用库存限制")
    private Boolean enableStockLimit;

    @Schema(description = "库存数量")
    private Integer stockNum;

    @Schema(description = "库存报警数量")
    private Integer stockWarnNum;

    @Schema(description = "生产备注")
    private String produceRemarks;

    @Schema(description = "结算规则")
    private String settlementRules;

    @Schema(description = "预估收益")
    private String estimatedRevenue;

    @Schema(description = "上架")
    private Boolean onSale;

    @Schema(description = "是否顶置")
    private Boolean isTop;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}