package cn.iocoder.yudao.module.therapy.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 设置用户预约时间 VO
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetAppointmentTimeReqVO {
    @Schema(description = "预约时间，格式 yyyy-MM-dd",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-05-23")
    @NotNull(message = "预约时间不能为空")
    private Date appointmentDate;

    @Schema(description = "预约时间段,字典:booking_time",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Min(value = 1,message = "预约时段不对")
    private Integer appointmentTimeRange;

}
