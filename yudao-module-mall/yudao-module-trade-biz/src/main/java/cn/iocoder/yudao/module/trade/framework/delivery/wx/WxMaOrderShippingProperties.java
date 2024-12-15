package cn.iocoder.yudao.module.trade.framework.delivery.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class WxMaOrderShippingProperties {

    @Resource
    private WxMaService wxMaService;

    @Resource
    private WxMaProperties wxMaProperties;

    /**
     * 是否开启微信小程序发货信息管理
     */
    private Boolean isMaTradeManaged = false;

    /**
     * 是否使用微信小程序物流查询插件
     */
    @Value("${wx.miniapp.plugin.logistics}")
    private Boolean useMaLogisticsPlugin;

    /**
     * 订单详情路径
     */
    @Value("${wx.miniapp.plugin.order-detail-path}")
    private String orderDetailPath;

    /**
     * 判断是否开启微信小程序发货信息管理
     */
    @PostConstruct
    private void checkMpTradeManaged(){
        try {
            if (StrUtil.isNotBlank(wxMaProperties.getAppid()))
                isMaTradeManaged = wxMaService.getWxMaOrderShippingService().isTradeManaged(wxMaProperties.getAppid()).getTradeManaged();
        }catch (WxErrorException e){
            throw new WxRuntimeException(e.getError().getErrorMsg());
        }
    }

}
