package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @author whycode
 * @title: AppHealthDataLogReqVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1513:52
 */

@Schema(description = "用户 APP - 睡眠数据 Request VO")
@Data
@ToString(callSuper = true)
public class AppHealthDataLogReqVO extends PageParam {

    @Schema(description = "设备ID", example = "1")
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    @Schema(description = "查询日期", example = "2023-09-01")
    @NotNull(message = "查询日期不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate queryDate;

}
