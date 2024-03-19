package cn.iocoder.yudao.module.steam.controller.app.alipay;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.steam.service.alipay.PayClientService;
import cn.iocoder.yudao.module.steam.service.alipay.vo.CreateIsvVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
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
    @PermitAll
    public CommonResult<String> createIsv(@RequestBody @Valid CreateIsvVo req){
        String isv = payClientService.createIsv(req);
        return success(isv);
    }
}
