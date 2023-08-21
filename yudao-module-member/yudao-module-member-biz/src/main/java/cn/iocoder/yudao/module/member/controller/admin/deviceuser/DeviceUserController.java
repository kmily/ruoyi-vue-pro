package cn.iocoder.yudao.module.member.controller.admin.deviceuser;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.member.controller.admin.deviceuser.vo.AllFamilyDeviceVO;
import cn.iocoder.yudao.module.member.controller.app.deviceuser.vo.AppDeviceUserVO;
import cn.iocoder.yudao.module.member.controller.app.family.vo.FamilyExportReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import cn.iocoder.yudao.module.member.service.deviceuser.DeviceUserService;
import cn.iocoder.yudao.module.member.service.family.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @author whycode
 * @title: DeviceUserController
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1710:58
 */

@Tag(name = "用户 APP - 设备和用户绑定")
@RestController
@RequestMapping("/member/device-user")
@Validated
public class DeviceUserController {

    @Resource
    private DeviceUserService deviceUserService;

    @Resource
    private FamilyService familyService;

    @GetMapping("/family-devices")
    @Operation(summary = "获得用户设备信息")
    @Parameter(name = "userId", description = "用户ID", required = true, example = "123")
    @PreAuthenticated
    public CommonResult<List<AllFamilyDeviceVO>> getDeviceList(@RequestParam(value = "userId") Long userId) {
        List<FamilyDO> list = familyService.getFamilyList(new FamilyExportReqVO().setUserId(userId));
        List<AllFamilyDeviceVO> familyDeviceVOS = list.stream().map(item -> {
            List<AppDeviceUserVO> userVOS = deviceUserService.getDevicesOfFamily(item.getId(), null);
            return new AllFamilyDeviceVO()
                    .setDeviceUserVOS(userVOS)
                    .setId(item.getId())
                    .setName(item.getName());
        }).collect(Collectors.toList());
        return success(familyDeviceVOS);
    }

}
