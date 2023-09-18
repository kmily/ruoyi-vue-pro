package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @author whycode
 * @title: AppHealthDataReqVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/817:07
 */

@Schema(description = "用户 APP - 体征数据查询 Request VO")
@Data
@ToString(callSuper = true)
public class AppHealthDataReqVO {


    @Schema(description = "查询设备ID", required = true, example = "[1,2]")
    @NotNull(message = "查询设备不能为空")
    private Long deviceId;

    @Schema(description = "查询日期", example = "2023-08-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "查询日期", example = "2023-08-01")
    private String[] createTime;

}
