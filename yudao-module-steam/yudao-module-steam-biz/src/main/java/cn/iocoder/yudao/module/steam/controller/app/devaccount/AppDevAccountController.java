package cn.iocoder.yudao.module.steam.controller.app.devaccount;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;

import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthSmsValidateReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.yudao.module.member.service.auth.MemberAuthService;
import cn.iocoder.yudao.module.steam.controller.app.devaccount.vo.AppDevAccountSaveReqVO;
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

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "steam后台 - 开放平台用户")
@RestController
@RequestMapping("/steam-app/dev-account")
@Validated
public class AppDevAccountController {

    @Resource
    private DevAccountService devAccountService;

    @Resource
    private MemberAuthService authService;

    @Resource
    private MemberUserMapper memberUserMapper;


//
//    @PostMapping("/create")
//    @Operation(summary = "创建开放平台用户")
//    public CommonResult<String> createDevAccount(@RequestBody @Valid AppDevAccountSaveReqVO reqVO) {
//        reqVO.setUserName("open_" + IdUtil.simpleUUID());
//        return success(devAccountService.apply(reqVO));
//    }

    @PostMapping("/update")
    @Operation(summary = "开放平台用户修改RSA")
    public CommonResult<String> createDevAccount1(@RequestBody @Valid AppAuthSmsValidateReqVO reqVO1,@RequestBody @Valid AppDevAccountSaveReqVO reqVO) {
        List<MemberUserDO> memberUserDOS = memberUserMapper.selectList(new LambdaQueryWrapperX<MemberUserDO>().eq(MemberUserDO::getId, getLoginUserId()));
        reqVO1.setMobile(memberUserDOS.get(0).getMobile());
        // 校验手机验证码
        authService.validateSmsCode(getLoginUserId(), reqVO1);
        reqVO.setUserName("open_" + IdUtil.simpleUUID());
        return success(devAccountService.apply(reqVO));
    }

    @GetMapping("/list")
    @Operation(summary = "开放平台列表")
    public CommonResult<List<DevAccountDO>> list() {

        return success(devAccountService.accountList());
    }

}