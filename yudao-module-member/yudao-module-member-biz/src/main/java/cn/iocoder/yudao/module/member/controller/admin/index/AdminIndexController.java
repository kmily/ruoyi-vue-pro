package cn.iocoder.yudao.module.member.controller.admin.index;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.controller.admin.index.vo.AdminIndexVO;
import cn.iocoder.yudao.module.member.controller.app.user.vo.AppUserInfoRespVO;
import cn.iocoder.yudao.module.member.convert.user.UserConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.devicenotice.DeviceNoticeService;
import cn.iocoder.yudao.module.member.service.deviceuser.DeviceUserService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @author whycode
 * @title: AdminIndexController
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/3110:09
 */

@Tag(name = "管理后台 - 首页")
@RestController
@RequestMapping("/member/index")
@Validated
public class AdminIndexController {


    @Resource
    private MemberUserService userService;

    @Resource
    private DeviceNoticeService noticeService;

    @Resource
    private DeviceUserService deviceUserService;

    @GetMapping("/get")
    @Operation(summary = "查询首页统计")
    public CommonResult<AdminIndexVO> getUser() {
        AdminIndexVO adminIndexVO = new AdminIndexVO();
        Long peoples = userService.selectCount();
        Long message = noticeService.selectCount();
        Long devices = deviceUserService.selectCount();
        adminIndexVO.setDevices(devices)
                .setMessages(message)
                .setPeoples(peoples);
        return success(adminIndexVO);
    }
}
