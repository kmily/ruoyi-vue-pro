package cn.iocoder.yudao.module.steam.controller.app.alipay;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.service.alipay.PayClientService;
import cn.iocoder.yudao.module.steam.controller.app.alipay.vo.CreateIsvVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "获取下拉选择信息")
@RestController
@RequestMapping("alipay")
@Validated
public class AppAlipayController {
    @Resource
    private PayClientService payClientService;

    @PostMapping("/createIsv")
    @Operation(summary = "ISV邀约即授权页面创建")
    @PreAuthenticated
    public CommonResult<String> createIsv(@RequestBody @Valid CreateIsvVo req){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        String isv = payClientService.createIsv(req,loginUser);
        return success(isv);
    }
    @PostMapping("/payNotify")
    @Operation(summary = "ISV邀约即授权页面创建")
    @PermitAll
    public CommonResult<String> payNotify(HttpServletRequest httpServletRequest, @RequestParam("msg_method") String msgMethod, @RequestParam(value = "notify_type",required = false) String notifyType, @RequestParam(value = "biz_content",required = false) String bizContent ){
        payClientService.payNotify(httpServletRequest,msgMethod,notifyType);
        return success("12");
    }
}
