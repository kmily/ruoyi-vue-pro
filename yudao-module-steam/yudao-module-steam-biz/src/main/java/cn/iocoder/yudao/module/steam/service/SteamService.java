package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.pay.dal.dataobject.order.PayOrderDO;
import cn.iocoder.yudao.module.pay.enums.order.PayOrderStatusEnum;
import cn.iocoder.yudao.module.pay.service.order.PayOrderService;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.binduser.vo.AppUnBindUserReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewExtService;
import cn.iocoder.yudao.module.steam.service.steam.*;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;

/**
 * Steam相关接口
 * 自定义
 */
@Service
@Slf4j
public class SteamService {

    private ConfigService configService;
    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }
    @Resource
    private BindUserMapper bindUserMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Resource
    private InvOrderMapper invOrderMapper;

    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private InvMapper invMapper;
    @Resource
    private SteamInvService steamInvService;

    private PaySteamOrderService paySteamOrderService;

    @Autowired
    public void setPaySteamOrderService(PaySteamOrderService paySteamOrderService) {
        this.paySteamOrderService = paySteamOrderService;
    }

    /**
     * 帐号绑定
     * @param openApi
     * @return
     */
    public int bind(OpenApi openApi) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        // 校验OpenAPI  if
        verifyOpenApi(openApi);
        String steamId = getSteamId(openApi.getIdentity());
        List<BindUserDO> bindUserDOS = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eqIfPresent(BindUserDO::getUserId, loginUser.getId())
                .eqIfPresent(BindUserDO::getSteamId, steamId)
                .eqIfPresent(BindUserDO::getUserType, loginUser.getUserType())
                .orderByDesc(BindUserDO::getId));

        if(bindUserDOS.size()>0){
            throw new ServiceException(-1,"此帐号已经被绑定");
        }
        BindUserDO bindUserDO=new BindUserDO().setUserId(loginUser.getId()).setUserType(loginUser.getUserType())
                .setSteamId(steamId)/*.setLoginCookie(openApi.getResponseNonce()*/;

        return bindUserMapper.insert(bindUserDO);
    }
    /**
     * 帐号绑定
     * @param reqVO 解绑ID
     * @return
     */
    public int unBind(AppUnBindUserReqVO reqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        BindUserDO bindUserDO = bindUserMapper.selectById(reqVO.getBindId());
        if(!bindUserDO.getUserId().equals(loginUser.getId())){
            throw new ServiceException(-1,"无权限操作");
        }
        if(!bindUserDO.getUserType().equals(loginUser.getUserType())){
            throw new ServiceException(-1,"无权限操作");
        }
        int i = bindUserMapper.deleteById(bindUserDO.getId());
        if(i<0){
            throw new ServiceException(-1,"操作失败");
        }
        return i;
    }
    public List<BindUserDO> steamList(){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();

        List<BindUserDO> bindUserDOS = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eqIfPresent(BindUserDO::getUserId, loginUser.getId())
                .orderByDesc(BindUserDO::getId));
        for(BindUserDO bindUserDO:bindUserDOS){
            bindUserDO.setSteamPassword(Objects.isNull(bindUserDO.getSteamPassword())?"0":"1");
            bindUserDO.setMaFile(null);
        }
        return bindUserDOS;
    }
    public void bindMaFile(byte[] maFileJsonByte,String password,Integer bindUserId) throws JsonProcessingException {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        BindUserDO bindUserDO = bindUserMapper.selectById(bindUserId);
        if(Objects.isNull(bindUserDO)){
            throw new ServiceException(-1,"绑定失败，请检查后再试。");
        }
        if(!bindUserDO.getUserId().equals(loginUser.getId())){
            throw new ServiceException(-1,"没有权限操作。");
        }
        if(!bindUserDO.getUserType().equals(loginUser.getUserType())){
            throw new ServiceException(-1,"没有权限操作。");
        }
        SteamMaFile steamMaFile;
        try {
            steamMaFile = objectMapper.readValue(maFileJsonByte, SteamMaFile.class);
        } catch (IOException e) {
            log.error("读取maFile失败{}",e);
            throw new ServiceException(-1,"读取maFile失败，请检查后再试。");
        }
        SteamWeb steamWeb=new SteamWeb(configService);
        steamWeb.login(password,steamMaFile);
        steamWeb.initTradeUrl();
        Optional<String> steamIdOptional = steamWeb.getSteamId();
        if(!steamIdOptional.isPresent()){
            throw new ServiceException(-1,"绑定用户失败原因 无法检测steam帐号密码");
        }
        if (!steamIdOptional.get().equals(bindUserDO.getSteamId())) {
            throw new ServiceException(-1,"ma文件和绑定的steam文件不一致，请确认后再次操作。");
        }
        bindUserDO.setSteamPassword(password);
        bindUserDO.setMaFile(steamMaFile);
        bindUserDO.setTradeUrl(steamWeb.getTreadUrl().get());
        if(steamWeb.getSteamName().isPresent()) {
            bindUserDO.setSteamName(steamWeb.getSteamName().get());
        }
        if(steamWeb.getSteamAvatar().isPresent()){
            bindUserDO.setAvatarUrl(steamWeb.getSteamAvatar().get());
        }
        // 用户修改了密码，需要重新绑定ma文件
        InvPageReqVO invPageReqVO = new InvPageReqVO();
        invPageReqVO.setSteamId(bindUserDO.getSteamId());
        invPageReqVO.setUserId(bindUserDO.getUserId());
        // 删除之前绑定的所有库存
        if(!(invMapper.selectPage(invPageReqVO)).getList().isEmpty()){
            invMapper.delete(new QueryWrapper<InvDO>().eq("steam_id",bindUserDO.getSteamId()).eq("user_id",bindUserDO.getUserId()));
        }
        bindUserMapper.updateById(bindUserDO);
        steamInvService.FistGetInventory(bindUserDO.getId(), "730");

    }

    /**
     * 验证用户是否已经被绑定
     * @param openApi steam open返回的信息
     * @return true 成功  false 失败
     * @throws UnsupportedEncodingException 异常
     */
    public boolean verifyOpenApi(OpenApi openApi) {
        try{
            ConfigDO configByKey = configService.getConfigByKey("steam.host");

//            HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
//            builder.url(configByKey.getValue() + "/openid/login");
//            builder.method(HttpUtil.Method.FORM);
            Map<String,String> post=new HashMap<>();
            post.put("openid.ns",openApi.getNs());
            post.put("openid.mode","check_authentication");
            post.put("openid.op_endpoint",openApi.getOpEndpoint());
            post.put("openid.claimed_id",openApi.getClaimedId());
            post.put("openid.identity",openApi.getIdentity());
            post.put("openid.return_to",openApi.getReturnTo());
            post.put("openid.response_nonce",openApi.getResponseNonce());
            post.put("openid.assoc_handle",openApi.getAssocHandle());
            post.put("openid.signed",openApi.getSigned());
            post.put("openid.sig",openApi.getSig());
//            builder.form(post);
            HttpUtil.ProxyRequestVo.ProxyRequestVoBuilder builder = HttpUtil.ProxyRequestVo.builder();
            builder.url(configByKey.getValue() + "/openid/login");
            builder.form(post);

            HttpUtil.ProxyResponseVo proxyResponseVo = HttpUtil.sentToSteamByProxy(builder.build());
            log.error("steam返回{}",proxyResponseVo);
            if(Objects.nonNull(proxyResponseVo.getStatus()) && proxyResponseVo.getStatus()==200){
                String html = proxyResponseVo.getHtml();
                if(html.contains("is_valid:true")){
                    return true;
                }else{
                    throw new ServiceException(-1,"Steam openid 数据不正确");
                }
            }else{
                throw new ServiceException(-1,"Steam openid 接口验证异常");
            }
        }catch (Exception e){
            log.error("解析出错原因{}",e);
            throw new ServiceException(-1,"Steam openid1 接口验证异常");
        }
    }

    /**
     * 将identity转成 steamId
     * @param identity openId里返回的steam信息
     * @return steamId
     */
    private String getSteamId(String identity){
        return identity.replace("https://steamcommunity.com/openid/id/","");
    }

    /**
     * 发货
     * @param invOrderDO
     */
    @Async
    public void tradeAsset(InvOrderDO invOrderDO){
        SellingDO sellingDO = sellingMapper.selectById(invOrderDO.getSellId());
        TransferMsg transferMsg=new TransferMsg();
        try{
            if (CommonStatusEnum.isDisable(sellingDO.getStatus())) {
                throw new ServiceException(-1,"库存已经更新无法进行发货。");
            }
            if (PayOrderStatusEnum.isClosed(sellingDO.getStatus())) {
                throw new ServiceException(-1,"已关闭无法进行发货。");
            }
            if (PayOrderStatusEnum.REFUND.getStatus().equals(sellingDO.getStatus())) {
                throw new ServiceException(-1,"已退款无法进行发货。");
            }
            Optional<BindUserDO> first = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                    .eq(BindUserDO::getUserId, invOrderDO.getUserId())
                    .eq(BindUserDO::getUserType, invOrderDO.getUserType())
                    .eq(BindUserDO::getSteamId, invOrderDO.getSteamId())
            ).stream().findFirst();
            if(!first.isPresent()){
                throw new ServiceException(-1,"收货方绑定关系失败无法发货。");
            }
            BindUserDO bindUserDO = first.get();
            //收货方tradeUrl
            String tradeUrl = bindUserDO.getTradeUrl();
//        SteamWeb steamWeb=new SteamWeb(configService);
//        steamWeb.login(bindUserDO.getSteamPassword(),bindUserDO.getMaFile());
//        steamWeb.initTradeUrl();
//        if(!steamWeb.getTreadUrl().isPresent()){
//
//        }
//        steamWeb.checkTradeUrl(steamWeb.getTreadUrl().get());
            BindUserDO bindUserDO1 = bindUserMapper.selectById(sellingDO.getBindUserId());
            //发货
            SteamWeb steamWeb=new SteamWeb(configService);
            steamWeb.login(bindUserDO1.getSteamPassword(),bindUserDO1.getMaFile());
            SteamInvDto steamInvDto=new SteamInvDto();
            steamInvDto.setAmount(sellingDO.getAmount());
            steamInvDto.setAssetid(sellingDO.getAssetid());
            steamInvDto.setClassid(sellingDO.getClassid());
            steamInvDto.setContextid(sellingDO.getContextid());
            steamInvDto.setInstanceid(sellingDO.getInstanceid());
            steamInvDto.setAppid(sellingDO.getAppid());
            SteamTradeOfferResult trade = steamWeb.trade(steamInvDto, tradeUrl);
            log.info("发货信息{}",trade);
            transferMsg.setTradeofferid(trade.getTradeofferid());
            invOrderDO.setTransferStatus(0);
            invOrderDO.setTransferStatus(InvTransferStatusEnum.TransferFINISH.getStatus());
            sellingDO.setTransferStatus(InvTransferStatusEnum.TransferFINISH.getStatus());
        }catch (ServiceException  e){
            log.error("发送失败原因{}",e.getMessage());
            transferMsg.setMsg(e.getMessage());
            invOrderDO.setTransferStatus(-1);
            invOrderDO.setTransferStatus(InvTransferStatusEnum.TransferERROR.getStatus());
            sellingDO.setTransferStatus(InvTransferStatusEnum.TransferERROR.getStatus());
        }
        invOrderDO.setTransferText(transferMsg);
        invOrderMapper.updateById(invOrderDO);
        sellingMapper.updateById(sellingDO);
    }

    /**
     * 订单超时关闭时自动同步数据
     * @return
     */
    public Integer autoCloseInvOrder(){
        List<InvOrderDO> invOrderDOS = invOrderMapper.selectList(new LambdaQueryWrapperX<InvOrderDO>()
                .eq(InvOrderDO::getPayStatus, false)
                .neIfPresent(InvOrderDO::getTransferStatus, InvTransferStatusEnum.CLOSE.getStatus())
        );
        log.info("invorder{}",invOrderDOS);
        Integer integer=0;
        for (InvOrderDO invOrderDO:invOrderDOS) {
            paySteamOrderService.closeInvOrder(invOrderDO.getId());
            integer++;
        }
        return integer;
    }

    /**
     * 检测交易链接格式要求格式合法
     * @param tradeUrl
     * @return
     */
    public Boolean checkTradeUrlFormat(String tradeUrl) {
        if(Objects.isNull(tradeUrl)){
            throw new ServiceException(-1,"交易链接不合法");
        }
        SteamWeb steamWeb=new SteamWeb(configService);
        URI uri = URI.create(tradeUrl);
        String query = uri.getQuery();

        try {
            if(!tradeUrl.startsWith("https://steamcommunity.com/tradeoffer/new/")){
                throw new ServiceException(-1,"交易链接不合法");
            }
            Map<String, String> query1 = steamWeb.parseQuery(query);
            if(Objects.isNull(query1.get("partner"))){
                throw new ServiceException(-1,"交易链接不合法");
            }
            if(Objects.isNull(query1.get("token"))){
                throw new ServiceException(-1,"交易链接不合法");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ServiceException(-1,"交易链接不合法");
        }
        return true;
    }

}
