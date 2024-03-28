package cn.iocoder.yudao.module.steam.mq;

import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodeityService;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodityReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.CommodityList;
import cn.iocoder.yudao.module.steam.service.fin.UUOrderService;
import cn.iocoder.yudao.module.steam.service.uu.UUService;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyReq;
import cn.iocoder.yudao.module.steam.service.youyoutemplate.UUTemplateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@RabbitListener(queues = "steam_commodity")
public class NotifyCommodityConsumer {
    @Resource
    private UUTemplateService uuTemplateService;

    @Resource
    private ApiUUCommodeityService apiUUCommodeityService;

    @Resource
    private UUService uuService;
    @RabbitHandler // 重点：添加 @RabbitHandler 注解，实现消息的消费
    public void onMessage(Integer integer) {
        log.info("[onMessage][消息内容({})]", integer);
        ApiUUCommodityReqVO reqVo = new ApiUUCommodityReqVO();
        reqVo.setPage(1);
        reqVo.setPageSize(50);
        reqVo.setTemplateId(String.valueOf(integer));
        ApiResult<CommodityList> commodityList = uuService.getCommodityList(reqVo);
        try {
            apiUUCommodeityService.insertGoodsQuery(commodityList, integer);
        } catch (JsonProcessingException e) {
            log.error("[onMessage][处理消息({}) 失败]",commodityList,e);
        }
    }
}
