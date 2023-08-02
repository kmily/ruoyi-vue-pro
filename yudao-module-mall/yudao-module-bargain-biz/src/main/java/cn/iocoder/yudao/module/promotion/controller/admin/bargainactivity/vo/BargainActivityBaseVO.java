package cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 砍价 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class BargainActivityBaseVO {

    @Schema(description = "砍价活动名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotNull(message = "砍价活动名称不能为空")
    private String name;

    @Schema(description = "砍价开启时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "砍价开启时间不能为空")
    private Integer startTime;

    @Schema(description = "砍价结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "砍价结束时间不能为空")
    private Integer endTime;

    @Schema(description = "砍价状态 0(关闭)  1(开启)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "砍价状态 0(关闭)  1(开启)不能为空")
    private Byte status;

    @Schema(description = "用户每次砍价的最大金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "28248")
    @NotNull(message = "用户每次砍价的最大金额不能为空")
    private Integer bargainMaxPrice;

    @Schema(description = "用户每次砍价的最小金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "24116")
    @NotNull(message = "用户每次砍价的最小金额不能为空")
    private Integer bargainMinPrice;

    @Schema(description = "用户帮砍的次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2980")
    @NotNull(message = "用户帮砍的次数不能为空")
    private Integer bargainCount;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "砍价有效时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime bargainEffectiveTime;

    @Schema(description = "砍价商品最低价", requiredMode = Schema.RequiredMode.REQUIRED, example = "3322")
    @NotNull(message = "砍价商品最低价不能为空")
    private BigDecimal minPrice;

    @Schema(description = "砍价起始金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "25514")
    @NotNull(message = "砍价起始金额不能为空")
    private Integer bargainFirstPrice;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "库存不能为空")
    private Integer stock;

    @Schema(description = "用户帮砍的次数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户帮砍的次数不能为空")
    private Integer peopleNum;

}
