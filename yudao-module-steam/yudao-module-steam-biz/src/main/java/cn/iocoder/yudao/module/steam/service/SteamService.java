package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.BindUserPageReqVO;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;
import cn.iocoder.yudao.module.steam.service.steam.OpenApi;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
//
    private BindUserService bindUserService;
    @Autowired
    public void setBindUserService(BindUserService bindUserService) {
        this.bindUserService = bindUserService;
    }


    //    @Autowired
//    private SteamInvMapper steamInvMapper;
//
//    @Autowired

//
    public int bind(OpenApi openApi) throws Exception {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
//        Long userId = SecurityUtils.getUserId();
        verifyOpenApi(openApi);
        String steamId = getSteamId(openApi.getIdentity());
        BindUserPageReqVO bindUserPageReqVO=new BindUserPageReqVO();
        bindUserPageReqVO.setSteamId(steamId);
        bindUserPageReqVO.setUserId(userId);

        long count = bindUserService.getBindUserPage(bindUserPageReqVO).getTotal();
        if(count>0){
            throw new Exception("此帐号已经被绑定");
        }
        BindUserSaveReqVO bindUserSaveReqVO=new BindUserSaveReqVO();
        bindUserSaveReqVO.setUserId(userId);
        bindUserSaveReqVO.setSteamId(steamId);

        return bindUserService.createBindUser(bindUserSaveReqVO);
    }
    /**
     * 验证用户是否已经被绑定
     * @param openApi steam open返回的信息
     * @return
     * @throws UnsupportedEncodingException
     */
    public boolean verifyOpenApi(OpenApi openApi) throws Exception {
        try{
            ConfigDO configByKey = configService.getConfigByKey("steam.host");
            StringBuffer stringBuffer=new StringBuffer();
            stringBuffer.append("openid.ns=").append(URLEncoder.encode(openApi.getNs(),"UTF8"));
            stringBuffer.append("&").append("openid.mode=").append(URLEncoder.encode("check_authentication","UTF8"));
            stringBuffer.append("&").append("openid.op_endpoint=").append(URLEncoder.encode(openApi.getOpEndpoint(),"UTF8"));
            stringBuffer.append("&").append("openid.claimed_id=").append(URLEncoder.encode(openApi.getClaimedId(),"UTF8"));
            stringBuffer.append("&").append("openid.identity=").append(URLEncoder.encode(openApi.getIdentity(),"UTF8"));
            stringBuffer.append("&openid.return_to=").append(URLEncoder.encode(openApi.getReturnTo(),"utf-8"));
            stringBuffer.append("&").append("openid.response_nonce=").append(URLEncoder.encode(openApi.getResponseNonce(),"UTF8"));
            stringBuffer.append("&").append("openid.assoc_handle=").append(URLEncoder.encode(openApi.getAssocHandle(),"UTF8"));
            stringBuffer.append("&").append("openid.signed=").append(URLEncoder.encode(openApi.getSigned(),"UTF8"));
            stringBuffer.append("&").append("openid.sig=").append(URLEncoder.encode(openApi.getSig(),"UTF8"));
            HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
            builder.url(configByKey.getValue() + "/openid/login");
            builder.method(HttpUtil.Method.FORM);
            Map<String,String> post=new HashMap<>();

            builder.form(post);
            HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
            String html = sent.html();
//            String s = HttpUtils.sendSSLPost(configByKey.getValue() + "/openid/login", stringBuffer.toString());
            if(html.contains("is_valid:true")){
                return true;
            }else{
                throw new Exception("Steam openid 数据不正确");
            }
        }catch (Exception e){
            throw new Exception("Steam openid 接口验证异常");
        }
    }

    /**
     * 将identity转成 steamId
     * @param identity
     * @return
     */
    private String getSteamId(String identity){
        return identity.replace("https://steamcommunity.com/openid/id/","");
    }
//    public InventoryDto fetchInventory(String steamId, String appId){
//        String steamHost = configService.selectConfigByKey("steam.host");
//        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
//        builder.method(HttpUtil.Method.GET).url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=75");
//        Map<String,String> pathVar=new HashMap<>();
//        pathVar.put("steamId",steamId);
//        pathVar.put("app",appId);
//        builder.pathVar(pathVar);
//        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
//        InventoryDto json = sent.json(InventoryDto.class);
//        for (InventoryDto.AssetsDTO item:json.getAssets()) {
//            SteamInv steamInv=new SteamInv();
//            steamInv.setSteamId(steamId);
//            steamInv.setAppid(item.getAppid().longValue());
//            steamInv.setAssetid(item.getAssetid());
//            List<SteamInv> steamInvs = steamInvMapper.selectSteamInvList(steamInv);
//            if(steamInvs.stream().count()>0){
//                Optional<SteamInv> first = steamInvs.stream().findFirst();
//                SteamInv steamInv1 = first.get();
//                steamInv1.setAmount(item.getAmount());
//                steamInv1.setClassid(item.getClassid());
//                steamInv1.setUpdateTime(new Date());
//                steamInvMapper.updateSteamInv(steamInv1);
//            }else{
//                SteamInv steamInv1=new SteamInv();
//                steamInv1.setSteamId(steamId);
//                steamInv1.setAppid(item.getAppid().longValue());
//                steamInv1.setAssetid(item.getAssetid());
//                steamInv1.setAmount(item.getAmount());
//                steamInv1.setClassid(item.getClassid());
//                steamInv1.setCreateTime(new Date());
//                steamInv1.setUpdateTime(new Date());
//                steamInvMapper.insertSteamInv(steamInv1);
//            }
//        }
//        return json;
//
//    }


}
