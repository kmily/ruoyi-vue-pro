package cn.iocoder.yudao.module.therapy.controller.admin.vo;

import cn.iocoder.yudao.module.therapy.controller.vo.SetAppointmentTimeReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 设置预约时间 VO
 **/

@Schema(description = "管理后台 - 设置预约时间 Request VO")
@Data
public class AdminSetAppointmentTimeVO extends SetAppointmentTimeReqVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "用户编号不能为空")
    private Long userId;
}
