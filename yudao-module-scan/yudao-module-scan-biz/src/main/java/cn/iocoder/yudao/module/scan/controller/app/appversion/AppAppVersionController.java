package cn.iocoder.yudao.module.scan.controller.app.appversion;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.scan.controller.app.appversion.vo.*;
import cn.iocoder.yudao.module.scan.convert.appversion.AppVersionConvert;
import cn.iocoder.yudao.module.scan.dal.dataobject.appversion.AppVersionDO;
import cn.iocoder.yudao.module.scan.service.appversion.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Api(tags = "扫描APP - 应用版本记录")
@RestController
@RequestMapping("/scan/app-version")
@Validated
public class AppAppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @GetMapping("/getlastversion")
    @ApiOperation("获得应用版本记录")
    public CommonResult<AppVersionDO> getLastVersion() {
        AppVersionDO appVersion = appVersionService.getNewVersion();
        return success(appVersion);
    }
}
