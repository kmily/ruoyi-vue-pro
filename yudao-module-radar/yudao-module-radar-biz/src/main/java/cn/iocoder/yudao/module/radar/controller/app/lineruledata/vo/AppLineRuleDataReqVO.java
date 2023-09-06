package cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @author whycode
 * @title: AppLineRuleDataReqVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/69:34
 */

@Schema(description = "用户 APP - 离回家数据 Response VO")
@Data
@ToString(callSuper = true)
public class AppLineRuleDataReqVO {

    @Schema(description = "设备ID", required = true, example = "1")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "开始时间", example = "[2023-09-01,2023-09-01]")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate[] queryDate;

}
