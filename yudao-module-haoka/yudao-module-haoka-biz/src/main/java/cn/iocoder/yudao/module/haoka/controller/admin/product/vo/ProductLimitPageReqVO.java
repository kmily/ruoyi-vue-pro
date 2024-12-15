package cn.iocoder.yudao.module.haoka.controller.admin.product.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 产品限制条件分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductLimitPageReqVO extends PageParam {

    @Schema(description = "产品类型名称", example = "王五")
    private String name;

    @Schema(description = "是否使用只发货地址")
    private Boolean useOnlySendArea;

    @Schema(description = "是否使用不发货地址")
    private Boolean useNotSendArea;

    @Schema(description = "是否使用身份证限制")
    private Boolean useCardLimit;

    @Schema(description = "最大年龄限制")
    private Integer ageMax;

    @Schema(description = "最小年龄限制")
    private Integer ageMin;

    @Schema(description = "单人开卡数量限制")
    private Integer personCardQuantityLimit;

    @Schema(description = "检测周期(月)")
    private Integer detectionCycle;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}