package cn.iocoder.yudao.module.pay.controller.admin.sign;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 支付宝安全发模式
 */

@RestController
@RequestMapping("/pay/")
public class AliPaySignController {


    @Resource
    private PayOrderService appService;

    /**
     * 用户授权协议签约
     */
    @GetMapping("/AliPayYHSQXYQY")
    public CommonResult<String>  AliPayYHSQXYQY(){
        return CommonResult.success(appService.AliPayYHSQXYQY());
    }



}
