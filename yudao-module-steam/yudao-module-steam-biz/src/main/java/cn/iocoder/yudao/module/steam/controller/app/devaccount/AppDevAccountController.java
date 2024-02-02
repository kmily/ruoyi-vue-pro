package cn.iocoder.yudao.module.steam.controller.app.devaccount;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo.DevAccountSaveReqVO;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 开放平台用户")
@RestController
@RequestMapping("/steam/dev-account")
@Validated
public class AppDevAccountController {

    @Resource
    private DevAccountService devAccountService;

    @PostMapping("/create")
    @Operation(summary = "创建开放平台用户")
    @PreAuthorize("@ss.hasPermission('steam:dev-account:create')")
    public CommonResult<Long> createDevAccount(@Valid @RequestBody DevAccountSaveReqVO createReqVO) {
        return success(devAccountService.apply(createReqVO));
    }
}