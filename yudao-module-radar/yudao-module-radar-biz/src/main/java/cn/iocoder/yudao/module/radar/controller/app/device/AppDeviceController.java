package cn.iocoder.yudao.module.radar.controller.app.device;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.radar.controller.admin.banner.vo.BannerRespVO;
import cn.iocoder.yudao.module.radar.convert.banner.BannerConvert;
import cn.iocoder.yudao.module.radar.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.module.radar.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.module.radar.service.banner.BannerService;
import cn.iocoder.yudao.module.radar.service.device.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @author whycode
 * @title: AppDeviceController
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/910:48
 */

@Tag(name = "用户APP - 设备信息")
@RestController
@RequestMapping("/radar/device")
@Validated
public class AppDeviceController {

    @Resource
    private DeviceService deviceService;

    @GetMapping("/device-status")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @Operation(summary = "获得设备状态列表")
    @PreAuthenticated
    public CommonResult<List<DeviceStatusVO>> getDeviceStatusList(@RequestParam("ids") Collection<Long> ids) {

        List<DeviceStatusVO> deviceStatusVOS = deviceService.getDeviceStatusList(ids);

        return success(deviceStatusVOS);
    }

}
