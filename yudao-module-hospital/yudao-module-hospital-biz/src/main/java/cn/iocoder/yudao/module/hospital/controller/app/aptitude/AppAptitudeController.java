package cn.iocoder.yudao.module.hospital.controller.app.aptitude;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.hospital.controller.app.aptitude.vo.*;
import cn.iocoder.yudao.module.hospital.convert.aptitude.AptitudeConvert;
import cn.iocoder.yudao.module.hospital.dal.dataobject.aptitude.AptitudeDO;
import cn.iocoder.yudao.module.hospital.service.aptitude.AptitudeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "APP  - 资质信息")
@RestController
@RequestMapping("/hospital/aptitude")
@Validated
public class AppAptitudeController {

    @Resource
    private AptitudeService aptitudeService;

    @GetMapping("/list")
    @Operation(summary = "获得资质信息列表")
    @PreAuthenticated
    public CommonResult<List<AppAptitudeRespVO>> getAptitudeList() {
        List<AptitudeDO> list = aptitudeService.listAllEnable();
        return success(AptitudeConvert.INSTANCE.convertList03(list));
    }

}
