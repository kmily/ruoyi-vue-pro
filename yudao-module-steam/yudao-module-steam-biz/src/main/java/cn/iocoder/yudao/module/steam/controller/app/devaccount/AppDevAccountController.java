package cn.iocoder.yudao.module.steam.controller.app.devaccount;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo.DevAccountSaveReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "steam后台 - 开放平台用户")
@RestController
@RequestMapping("/steam-app/dev-account")
@Validated
public class AppDevAccountController {

    @Resource
    private DevAccountService devAccountService;

    @PostMapping("/create")
    @Operation(summary = "创建开放平台用户")
//    @PreAuthorize("@ss.hasPermission('steam:dev-account:create')")
    public CommonResult<Long> createDevAccount(@Valid @RequestBody DevAccountSaveReqVO createReqVO) {
        return success(devAccountService.apply(createReqVO));
    }
    @GetMapping("/list")
    @Operation(summary = "开放平台列表")
//    @PreAuthorize("@ss.hasPermission('steam:dev-account:create')")
    public CommonResult<List<DevAccountDO>> list() {
        return success(devAccountService.accountList());
    }
}