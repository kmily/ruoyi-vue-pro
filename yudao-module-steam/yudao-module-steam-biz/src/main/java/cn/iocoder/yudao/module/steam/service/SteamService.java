package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.BindUserPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.BindUserSaveReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.service.steam.OpenApi;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
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


    @Resource
    private InvMapper invMapper;
//
//    @Autowired

//

    /**
     * 帐号绑定
     * @param openApi
     * @return
     * @throws Exception
     */
    public int bind(OpenApi openApi) throws Exception {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
//        Long userId = SecurityUtils.getUserId();
        // 校验OpenAPI  if
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
     * @return true 成功  false 失败
     * @throws UnsupportedEncodingException 异常
     */
    public boolean verifyOpenApi(OpenApi openApi) throws Exception {
        try{
            ConfigDO configByKey = configService.getConfigByKey("steam.host");
//            StringBuffer stringBuffer=new StringBuffer();
//            stringBuffer.append("openid.ns=").append(URLEncoder.encode(openApi.getNs(),"UTF8"));
//            stringBuffer.append("&").append("openid.mode=").append(URLEncoder.encode("check_authentication","UTF8"));
//            stringBuffer.append("&").append("openid.op_endpoint=").append(URLEncoder.encode(openApi.getOpEndpoint(),"UTF8"));
//            stringBuffer.append("&").append("openid.claimed_id=").append(URLEncoder.encode(openApi.getClaimedId(),"UTF8"));
//            stringBuffer.append("&").append("openid.identity=").append(URLEncoder.encode(openApi.getIdentity(),"UTF8"));
//            stringBuffer.append("&openid.return_to=").append(URLEncoder.encode(openApi.getReturnTo(),"utf-8"));
//            stringBuffer.append("&").append("openid.response_nonce=").append(URLEncoder.encode(openApi.getResponseNonce(),"UTF8"));
//            stringBuffer.append("&").append("openid.assoc_handle=").append(URLEncoder.encode(openApi.getAssocHandle(),"UTF8"));
//            stringBuffer.append("&").append("openid.signed=").append(URLEncoder.encode(openApi.getSigned(),"UTF8"));
//            stringBuffer.append("&").append("openid.sig=").append(URLEncoder.encode(openApi.getSig(),"UTF8"));
            HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
            builder.url(configByKey.getValue() + "/openid/login");
            builder.method(HttpUtil.Method.FORM);
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
     * @param identity openId里返回的steam信息
     * @return steamId
     */
    private String getSteamId(String identity){
        return identity.replace("https://steamcommunity.com/openid/id/","");
    }
    public InventoryDto fetchInventory(String steamId, String appId){
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.method(HttpUtil.Method.GET).url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=75");
        Map<String,String> pathVar=new HashMap<>();
        pathVar.put("steamId",steamId);
        pathVar.put("app",appId);
        builder.pathVar(pathVar);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        InventoryDto json = sent.json(InventoryDto.class);
        for (InventoryDto.AssetsDTO item:json.getAssets()) {
            InvPageReqVO steamInv=new InvPageReqVO();
            steamInv.setSteamId(steamId);
            steamInv.setAppid(item.getAppid());
            steamInv.setAssetid(item.getAssetid());
            PageResult<InvDO> invDOPageResult = invMapper.selectPage(steamInv);
            if(invDOPageResult.getTotal()>0){
                Optional<InvDO> first = invDOPageResult.getList().stream().findFirst();
                InvDO steamInv1 = first.get();
                steamInv1.setAmount(item.getAmount());
                steamInv1.setClassid(item.getClassid());
//                steamInv1.setUpdateTime(new Date());
                invMapper.updateById(steamInv1);
            }else{
                InvDO steamInv1=new InvDO();
                steamInv1.setSteamId(steamId);
                steamInv1.setAppid(item.getAppid());
                steamInv1.setAssetid(item.getAssetid());
                steamInv1.setAmount(item.getAmount());
                steamInv1.setClassid(item.getClassid());
                invMapper.insert(steamInv1);
            }
        }
        return json;
    }


}