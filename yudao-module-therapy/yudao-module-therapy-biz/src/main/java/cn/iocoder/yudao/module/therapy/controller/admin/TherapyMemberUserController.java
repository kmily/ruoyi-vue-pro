package cn.iocoder.yudao.module.therapy.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.AdminSetAppointmentTimeVO;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 管理后台 - 患者管理
 **/

@Tag(name = "管理后台 - 患者管理")
@RestController
@RequestMapping("/therapy/member/user")
@Validated
@Slf4j
public class TherapyMemberUserController {


    @Resource
    private TreatmentService treatmentService;

    @PostMapping("/set-appointment-time")
    @Operation(summary = "设置预约时间")
    public CommonResult<Boolean> setAppointmentTime(@RequestBody @Valid AdminSetAppointmentTimeVO reqVO) {
        treatmentService.setAppointmentTime(reqVO.getUserId(),reqVO);
        return success(true);
    }
}
