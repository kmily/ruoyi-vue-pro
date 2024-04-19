package cn.iocoder.yudao.module.steam.job.script;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderSummaryDo;
import cn.iocoder.yudao.module.steam.dal.dataobject.bindipaddress.BindIpaddressDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderExtMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.apiorder.ApiOrderSummaryMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;
import cn.iocoder.yudao.module.steam.enums.PlatCodeEnum;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.SteamWeb;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;
import cn.iocoder.yudao.module.steam.service.fin.ApiThreeOrderService;
import cn.iocoder.yudao.module.steam.service.fin.UUOrderService;
import cn.iocoder.yudao.module.steam.service.steam.ConfirmAction;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.MobileConfList;
import cn.iocoder.yudao.module.steam.service.steam.TradeOfferInfo;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

/**
 * 订单统计
 */
@Slf4j
//@Component("V5LoginJob")
public class OrderSummaryJob implements JobHandler {
    @Resource
    private ApiOrderSummaryMapper apiOrderSummaryMapper;

    @Resource
    private ApiOrderMapper apiOrderMapper;
    @Resource
    private ApiOrderExtMapper apiOrderExtMapper;
    @Resource
    private SteamService steamService;
    @Resource
    private BindUserService  bindUserService;
    @Resource
    List<ApiThreeOrderService> apiThreeOrderServiceList;

    @Resource
    private ConfigService configService;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private SellingMapper sellingMapper;

    @Override
    public String execute(String param) {

        Integer execute = TenantUtils.execute(1L, () -> {
            List<ApiOrderDO> apiOrderDOS = apiOrderMapper.selectList(new LambdaQueryWrapperX<ApiOrderDO>()
                    .eq(ApiOrderDO::getCancelReason, "订单被第三方取消")
                    .eq(ApiOrderDO::getPlatCode, PlatCodeEnum.IO661.getCode())
            );
            log.info("order{}",apiOrderDOS);
            int integer=0;
//            for (ApiOrderDO apiOrderDO:apiOrderDOS) {
//
//                ApiOrderSummaryDo apiOrderSummaryDo=new ApiOrderSummaryDo();
//                BeanUtils.copyProperties(apiOrderDO,apiOrderSummaryDo);
//                apiOrderSummaryMapper.insert(apiOrderSummaryDo);
//
//
//                integer++;
//            }
            for (ApiOrderSummaryDo apiOrderSummaryDo:apiOrderSummaryMapper.selectList()) {
                Optional<ApiOrderDO> first = apiOrderDOS.stream().filter(item -> item.getId().equals(apiOrderSummaryDo.getId())).findFirst();
                ApiOrderDO apiOrderDO = first.get();

                BeanUtils.copyProperties(apiOrderDO,apiOrderSummaryDo);
                ApiOrderExtDO apiOrderExtDO = apiOrderExtMapper.selectOne(new LambdaQueryWrapperX<ApiOrderExtDO>().eq(ApiOrderExtDO::getOrderId, apiOrderDO.getId()));
                SellingDO sellingDO = sellingMapper.selectById(Long.parseLong(apiOrderExtDO.getCommodityInfo()));
                apiOrderSummaryDo.setShortName(sellingDO.getShortName());

                apiOrderSummaryMapper.updateById(apiOrderSummaryDo);


            }
            for (ApiOrderSummaryDo apiOrderSummaryDo:apiOrderSummaryMapper.selectList(new LambdaQueryWrapperX<ApiOrderSummaryDo>().isNull(ApiOrderSummaryDo::getInvStatus))) {
                Optional<ApiOrderDO> first = apiOrderDOS.stream().filter(item -> item.getId().equals(apiOrderSummaryDo.getId())).findFirst();
                ApiOrderDO apiOrderDO = first.get();

                BeanUtils.copyProperties(apiOrderDO,apiOrderSummaryDo);
                ApiOrderExtDO apiOrderExtDO = apiOrderExtMapper.selectOne(new LambdaQueryWrapperX<ApiOrderExtDO>().eq(ApiOrderExtDO::getOrderId, apiOrderDO.getId()));

                TradeOfferInfo orderSimpleStatus = getOrderSimpleStatus(new LoginUser(), apiOrderDO, apiOrderExtDO);
                apiOrderSummaryDo.setInvText(JsonUtils.toJsonString(orderSimpleStatus));
                Integer tradeOfferState = orderSimpleStatus.getResponse().getOffer().getTradeOfferState();
////        Integer tradeOffInfoV2 = getTradeOffInfo(bindUser, String.valueOf(orderExt.getTradeOfferId()));
//        log.info("tradeOffInfoV2 {}",tradeOfferState);
//        //1,进行中，2完成，3作废
            if(tradeOfferState ==3){
                //打款
                apiOrderSummaryDo.setInvStatus("2");
//                return 2;
            }else if(tradeOfferState ==7){
                //买家取消
                apiOrderSummaryDo.setInvStatus("3");
//                return 3;
            }else if(tradeOfferState ==6){
                //卖家取消
                apiOrderSummaryDo.setInvStatus("3");
//                return 3;
            }else{

                apiOrderSummaryDo.setInvStatus("1");
//                return 1;
                //todo 12小时恢复
    //            LocalDateTime plus = orderExt.getCreateTime().plus(Duration.ofHours(12));
    //            if(LocalDateTime.now().compareTo(plus) > 0){
    //                //时间是否超出12小时，超出则退款
    //                return 3;
    ////                damagesCloseInvOrder(invOrderId);
    //            }
            }


                apiOrderSummaryMapper.updateById(apiOrderSummaryDo);


                integer++;
            }
            return integer;
        });
        return String.format("执行关闭成功 %s 个", execute);
    }
    private Optional<ApiThreeOrderService> getApiThreeByOrder(ApiOrderDO apiOrderDO){
        return apiThreeOrderServiceList.stream().filter(item->item.getPlatCode().getCode().equals(apiOrderDO.getPlatCode())).findFirst();
    }
    private Optional<ApiThreeOrderService> getApiThreeByPlatCode(PlatCodeEnum platCodeEnum){
        return apiThreeOrderServiceList.stream().filter(item-> Objects.equals(item.getPlatCode(), platCodeEnum)).findFirst();
    }
    public TradeOfferInfo getOrderSimpleStatus(LoginUser loginUser, ApiOrderDO masterOrder, ApiOrderExtDO orderExt) {
        BindUserDO bindUser = bindUserService.getBindUser(masterOrder.getSellBindUserId());

        SteamWeb steamWeb=new SteamWeb(configService,steamService.getBindUserIp(bindUser));
        if(steamWeb.checkLogin(bindUser)){
            if(steamWeb.getWebApiKey().isPresent()){
                bindUser.setApiKey(steamWeb.getWebApiKey().get());
            }
            bindUserService.changeBindUserCookie(new BindUserDO().setId(bindUser.getId()).setLoginCookie(steamWeb.getCookieString()).setApiKey(bindUser.getApiKey()));
        }
        //获取access_token
//        https://steamcommunity.com/profiles/76561199303124510/tradeoffers/
//            builder.form(post);
        HttpUtil.ProxyRequestVo.ProxyRequestVoBuilder builder = HttpUtil.ProxyRequestVo.builder();
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        builder.headers(header);
        builder.cookieString(steamWeb.getCookieString());
        builder.url("https://steamcommunity.com/profiles/:steamId/tradeoffers/");
//        builder.query(post);
        Map<String, String> pathvar = new HashMap<>();
        pathvar.put("steamId", bindUser.getSteamId());
        builder.pathVar(pathvar);
        Optional<BindIpaddressDO> bindUserIp = steamService.getBindUserIp(bindUser);
        HttpUtil.ProxyResponseVo proxyResponseVo = HttpUtil.sentToSteamByProxy(builder.build(),bindUserIp);
        log.info("{}",proxyResponseVo);
        String accessToken=null;
        if(Objects.nonNull(proxyResponseVo.getStatus()) && proxyResponseVo.getStatus()==200){
            Element application_config = Jsoup.parse(proxyResponseVo.getHtml()).body().getElementById("application_config");
            if(Objects.nonNull(application_config)){
                accessToken = application_config.attributes().get("data-loyalty_webapi_token");
            }
        }
        if(!StringUtils.hasText(accessToken)){
            log.info("获取accesstoken失败");
        }
        accessToken=accessToken.replaceAll("^\"","").replaceAll("\"$","");



        TradeOfferInfo tradeOffInfo = getTradeOffInfo(bindUser, String.valueOf(orderExt.getTradeOfferId()),accessToken);
        return tradeOffInfo;
    }
    private TradeOfferInfo getTradeOffInfo(BindUserDO bindUserDO,String tradeOfferId,String accessToken) {
        try{
            Optional<BindIpaddressDO> bindUserIp = steamService.getBindUserIp(bindUserDO);
            SteamWeb steamWeb=new SteamWeb(configService,bindUserIp);
            if(steamWeb.checkLogin(bindUserDO)){
                if(steamWeb.getWebApiKey().isPresent()){
                    bindUserDO.setApiKey(steamWeb.getWebApiKey().get());
                }
                bindUserService.changeBindUserCookie(new BindUserDO().setId(bindUserDO.getId()).setLoginCookie(steamWeb.getCookieString()).setApiKey(bindUserDO.getApiKey()));
            }

            Map<String,String> post=new HashMap<>();
            post.put("key",bindUserDO.getApiKey());
            post.put("tradeofferid",tradeOfferId);
            post.put("access_token",accessToken);
//            builder.form(post);
            HttpUtil.ProxyRequestVo.ProxyRequestVoBuilder builder = HttpUtil.ProxyRequestVo.builder();
            Map<String, String> header = new HashMap<>();
            header.put("Accept-Language", "zh-CN,zh;q=0.9");
            builder.headers(header);
            builder.url("https://api.steampowered.com/IEconService/GetTradeOffer/v1");
            builder.query(post);

            HttpUtil.ProxyResponseVo proxyResponseVo = HttpUtil.sentToSteamByProxy(builder.build(),bindUserIp);
            log.error("steam返回{}",proxyResponseVo);
            if(Objects.nonNull(proxyResponseVo.getStatus()) && proxyResponseVo.getStatus()==200){
                String html = proxyResponseVo.getHtml();
                return objectMapper.readValue(html, TradeOfferInfo.class);
            }else{
                throw new ServiceException(-1,"Steam openid 接口验证异常");
            }
        }catch (Exception e){
            log.error("解析出错原因{}",e.getMessage());
            throw new ServiceException(-1,"Steam openid1 接口验证异常");
        }
    }
}
