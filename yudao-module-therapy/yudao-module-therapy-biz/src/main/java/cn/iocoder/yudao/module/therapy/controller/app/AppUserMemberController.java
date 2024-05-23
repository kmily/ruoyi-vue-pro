package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.therapy.controller.app.VO.AppMemberUserSetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.service.UserService;
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
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: TODO
 **/

@Tag(name = "用户 APP - 用户个人中心")
@RestController
@RequestMapping("/treatment/member/user")
@Validated
@Slf4j
public class AppUserMemberController {

    @Resource
    private UserService userService;

    @PostMapping("/set-appointment-time")
    @Operation(summary = "设置预约时间")
    public CommonResult<Boolean> setAppointmentTime(@RequestBody @Valid AppMemberUserSetAppointmentTimeReqVO reqVO) {
        userService.setAppointmentTime(getLoginUserId(),reqVO);
        return success(true);
    }
}
