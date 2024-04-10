package cn.iocoder.yudao.module.pay.controller.admin.notify;


import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.pay.core.client.PayClient;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import cn.iocoder.yudao.module.pay.service.notify.AliDefaultNotifyService;
import com.alipay.api.AlipayApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.CHANNEL_NOT_FOUND;

@Tag(name = "支付宝回调相关")
@RestController
@RequestMapping("/alipay/notify")
@Validated
@Slf4j
public class AliNotifyController {
    @Resource
    private AliDefaultNotifyService aliDefaultNotifyService;

    @Resource
    private PayChannelService channelService;



    @RequestMapping(value = "/default/{channelId}")
    @Operation(summary = "支付宝统一回调接口")
    @PermitAll
    @OperateLog(enable = false) // 回调地址，无需记录操作日志
    public String notifyDefault(@PathVariable("channelId") Long channelId,
                                @RequestParam(required = false) Map<String, String> params,
                                @RequestBody(required = false) String body) throws AlipayApiException {
        log.info("[notifyDefault][channelId({}) 回调数据({}/{})]", channelId, params, body);
        // 1. 校验支付渠道是否存在
        PayClient payClient = channelService.getPayClient(channelId);
        if (payClient == null) {
            log.error("[notifyCallback][渠道编号({}) 找不到对应的支付客户端]", channelId);
            throw exception(CHANNEL_NOT_FOUND);
        }
        params=payClient.doParseNotify(params,body);
        // 2. 解析通知数据
        log.info("test:{}",params);
        aliDefaultNotifyService.notifyDefault(channelId, params);
        return "success";
    }
}
