package cn.iocoder.yudao.module.therapy.controller.app.vo;

import cn.iocoder.yudao.module.therapy.controller.vo.SetAppointmentTimeReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: TODO
 **/

@Schema(description = "用户 APP - 设置预约时间 Request VO")
@Data
public class AppMemberUserSetAppointmentTimeReqVO extends SetAppointmentTimeReqVO {

}
