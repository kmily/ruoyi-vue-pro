package cn.iocoder.yudao.module.trade.service.order.handler;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.pay.api.order.dto.PayOrderRespDTO;
import cn.iocoder.yudao.module.system.api.social.SocialClientApi;
import cn.iocoder.yudao.module.system.api.social.dto.SocialWxaUploadOrderShippingInfoDTO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.enums.delivery.DeliveryTypeEnum;
import cn.iocoder.yudao.module.trade.service.delivery.DeliveryExpressService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 电商类目的微信小程序需要上传发货信息
 * 微信小程序开发环境下的订单不能用来发货。只有小程序正式版才会有发货，所以体验版无法调通，提示订单不存在。注意别踩坑。
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "yudao.trade.order", value = "upload-wx-mini-app", matchIfMissing = false)
public class TradeWxMiniAppDeliveryHandler implements TradeOrderHandler {
    @Resource
    private PayOrderApi payOrderApi;
    @Resource
    private SocialClientApi socialClientApi;
    @Resource
    private DeliveryExpressService expressService;

    @Override
    public void afterDeliveryOrder(TradeOrderDO order) {
        if (!"wx_lite".equals(order.getPayChannelCode())) {
            return;
        }
        PayOrderRespDTO payOrder = payOrderApi.getOrder(order.getPayOrderId());
        SocialWxaUploadOrderShippingInfoDTO reqDTO = new SocialWxaUploadOrderShippingInfoDTO();
        reqDTO.setChannelOrderNo(payOrder.getChannelOrderNo());
        reqDTO.setChannelUserId(payOrder.getChannelUserId());
        reqDTO.setItemDesc(payOrder.getSubject());
        reqDTO.setReceiverContact(order.getReceiverMobile());
        boolean isExpressDelivery = DeliveryTypeEnum.EXPRESS.getType().equals(order.getDeliveryType()) && StrUtil.isNotEmpty(order.getLogisticsNo());
        reqDTO.setLogisticsType(isExpressDelivery ? 1 : 4);
        reqDTO.setLogisticsNo(order.getLogisticsNo());
        reqDTO.setExpressCompany(isExpressDelivery ? expressService.getDeliveryExpress(order.getLogisticsId()).getCode() : "");
        socialClientApi.uploadWxaOrderShippingInfo(UserTypeEnum.ADMIN.getValue(), reqDTO);
    }
}
