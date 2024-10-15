package cn.iocoder.yudao.module.trade.controller.admin.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import cn.binarywang.wx.miniapp.util.crypt.WxMaCryptUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderRespDTO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderQueryService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderUpdateService;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 微信小程序消息推送服务
 * <p>
 * 参考文档：<a href="https://developers.weixin.qq.com/miniprogram/dev/framework/server-ability/message-push.html">消息推送</a>
 */
@RestController
@RequestMapping("/trade/wx")
@Slf4j
public class wxMaMessageController {

    @Resource
    private WxMaService wxMaService;

    @Resource
    private TradeOrderQueryService tradeOrderQueryService;

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;

    @Resource
    private PayOrderApi payOrderApi;

    private WxMaMessageRouter router;

    @PostConstruct
    private void initRouter() {
        router = new WxMaMessageRouter(wxMaService);
        router
                .rule().async(false).event("trade_manage_order_settlement").handler(TradeManageOrderSettlementMessageHandler).end();

    }

    @RequestMapping("/message")
    @Operation(summary = "接收微信推送消息")
    @PermitAll
    public ResponseEntity<?> receiver(@RequestBody(required = false) String body, @RequestParam Map<String, String> params) {

        String signature = params.get("signature");
        String nonce = params.get("nonce");
        String timestamp = params.get("timestamp");
        // 检查消息签名
        if (!wxMaService.checkSignature(timestamp, nonce, signature)) {
            return ResponseEntity.ok("非法请求");
        }

        log.info("[wxMaMessageController][收到微信推送消息 请求参数({}) 请求体({})]", body, params);
        //开发者服务器验证请求
        if (params.containsKey("echostr")) {
            return ResponseEntity.ok(params.get("echostr"));
        }
        WxMaMessage message;
        Map<String, Object> context;
        if ("JSON".equals(wxMaService.getWxMaConfig().getMsgDataFormat())) {
            message = WxMaMessage.fromJson(body);
            //明文传输
            if (!params.containsKey("encrypt_type")) {
                context = JsonUtils.parseObject(body, new TypeReference<Map<String, Object>>() {
                });
            } else {//加密传输
                String decrypt = new WxMaCryptUtils(wxMaService.getWxMaConfig()).decrypt(message.getEncrypt());
                context = JsonUtils.parseObject(decrypt, new TypeReference<Map<String, Object>>() {
                });
            }
        } else {
            //明文传输
            if (!params.containsKey("encrypt_type")) {
                message = WxMaMessage.fromXml(body);
            } else {//加密传输
                String msgSignature = params.get("msg_signature");
                message = WxMaMessage.fromEncryptedXml(body, wxMaService.getWxMaConfig(), timestamp, nonce, msgSignature);
            }
            context = message.getAllFieldsMap();
        }
        router.route(message, context);
        return ResponseEntity.ok("");
    }

    /**
     * 处理trade_manage_order_settlement推送消息
     * <p>
     * 参考文档：<a href="https://developers.weixin.qq.com/miniprogram/dev/platform-capabilities/business-capabilities/order-shipping/order-shipping.html">小程序发货信息管理服务</a>
     */
    private final WxMaMessageHandler TradeManageOrderSettlementMessageHandler = new WxMaMessageHandler() {
        @Override
        public WxMaXmlOutMessage handle(WxMaMessage message, Map<String, Object> context, WxMaService wxMaService, WxSessionManager wxSessionManager) {
            log.info("[wxMessageHandler][收到(trade_manage_order_settlement)类型事件推送]");
            String transactionId = context.get("transaction_id").toString();
            //包含用户收货时间
            if (context.containsKey("confirm_receive_time")) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //确认收货时间
                String confirmReceiveTime = dateFormat.format(new Date(Long.parseLong(context.get("confirm_receive_time").toString()) * 1000));
                //收货方式(1-自动确认,2-手动确认)
                Integer confirmReceiveMethod = Integer.parseInt(context.get("confirm_receive_method").toString());

                //用交易单号获取支付订单
                log.info("[wxMessageHandler][收到确认收货信息 TransactionId({}) 收货时间({}) 收货方式({})]", transactionId, confirmReceiveTime, confirmReceiveMethod);

                System.out.println("进入处理收货:" + transactionId);
                PayOrderRespDTO payOrder = payOrderApi.getOrder(transactionId);
                //用支付订单获取交易订单
                TradeOrderDO tradeOrderDO = tradeOrderQueryService.getOrder(Long.parseLong(payOrder.getMerchantOrderId()));
                //复用会员收货逻辑
                tradeOrderUpdateService.receiveOrderByMember(tradeOrderDO.getUserId(), tradeOrderDO.getId());
                //TODO: 是否需要同步微信端的收货时间和系统的收货时间
            }

            return null;
        }
    };
}
