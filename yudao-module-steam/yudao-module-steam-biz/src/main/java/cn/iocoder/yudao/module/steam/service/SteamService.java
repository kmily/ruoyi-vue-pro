package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.BindUserPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.binduser.vo.BindUserSaveReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.InvDescPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.service.steam.OpenApi;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Stream;

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
    @Resource
    private InvDescMapper invDescMapper;
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
//        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
//        builder.method(HttpUtil.Method.GET).url("https://steamcommunity.com/inventory/:steamId/:app/2?l=schinese&count=75");
//        Map<String,String> pathVar=new HashMap<>();
//        pathVar.put("steamId",steamId);
//        pathVar.put("app",appId);
//        builder.pathVar(pathVar);
//        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
//        InventoryDto json = sent.json(InventoryDto.class);
        String j="{\n" +
                "\t\"assets\": [{\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35290397850\",\n" +
                "\t\t\"classid\": \"5665331088\",\n" +
                "\t\t\"instanceid\": \"302028390\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35286915802\",\n" +
                "\t\t\"classid\": \"520026577\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35284793744\",\n" +
                "\t\t\"classid\": \"5334431299\",\n" +
                "\t\t\"instanceid\": \"188530170\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35284792879\",\n" +
                "\t\t\"classid\": \"520026577\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35284753775\",\n" +
                "\t\t\"classid\": \"3035582416\",\n" +
                "\t\t\"instanceid\": \"302028390\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35284715816\",\n" +
                "\t\t\"classid\": \"3035582416\",\n" +
                "\t\t\"instanceid\": \"302028390\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35284358468\",\n" +
                "\t\t\"classid\": \"5604816244\",\n" +
                "\t\t\"instanceid\": \"188530170\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35276486098\",\n" +
                "\t\t\"classid\": \"4307675336\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35273943602\",\n" +
                "\t\t\"classid\": \"3770751316\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"35273518744\",\n" +
                "\t\t\"classid\": \"1560433642\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"contextid\": \"2\",\n" +
                "\t\t\"assetid\": \"34949559500\",\n" +
                "\t\t\"classid\": \"2735679255\",\n" +
                "\t\t\"instanceid\": \"188530170\",\n" +
                "\t\t\"amount\": \"1\"\n" +
                "\t}],\n" +
                "\t\"descriptions\": [{\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"5665331088\",\n" +
                "\t\t\"instanceid\": \"302028390\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot621FA957OnHdTRD746JmYWPnuL5feqBwD8Gvpcg3rDDrN30jlHl_kdka2j1JY-SewJoN1CC-lK8xOfrhZai_MOeIIc7ylQ\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot621FA957OnHdTRD746JmYWPnuL5DLfQhGxUppQh3L2Wo96k3A228kI9NTzyIYKdegc4YFHX81Xol-a9hZW-uZTNyXJ9-n51oWQQ_Vc\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 久经沙场\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"高风险，高回报，恶名昭彰的 AWP 因其标志性的枪声和一枪一个的准则而为人熟知。 这把武器使用十二宫的花纹进行了涂装。\\n\\n<i>这对你的敌人来说将是难熬的一个月</i>\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"神魔收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D16162507490224551977\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"fraudwarnings\": [\"名称标签：“应当在日暮时咆哮燃烧，怒斥光明的消逝”\"],\n" +
                "\t\t\"name\": \"AWP | 狮子之日\",\n" +
                "\t\t\"name_color\": \"D2D2D2\",\n" +
                "\t\t\"type\": \"工业级 狙击步枪\",\n" +
                "\t\t\"market_name\": \"AWP | 狮子之日 (久经沙场)\",\n" +
                "\t\t\"market_hash_name\": \"AWP | Sun in Leo (Field-Tested)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D16162507490224551977\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_SniperRifle\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"狙击步枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_awp\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"AWP\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_gods_and_monsters\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"神魔收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"normal\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"普通\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Uncommon_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"工业级\",\n" +
                "\t\t\t\"color\": \"5e98d9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory2\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"久经沙场\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"520026577\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgposbaqKAxf0Ob3djFN79f7mImagvLnML7fglRd4cJ5nqeQoN3w0QHgrhdoMjylJo7GIVU7ZAzQqQC6k-rs1JHotZvNzSRgvHFx-z-DyPzurK-U\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgposbaqKAxf0Ob3djFN79f7mImagvLnML7fglRd4cJ5ntbN9J7yjRrl_kI5amz3cdKRI1NoY1CDqQK7xLrv1se47pnKmHU3syYm4SnemUTkn1gSOYPIEaei\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 崭新出厂\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"格洛克 18 型是把耐用的第一回合手枪，非常适合对付那些没有穿护甲的对手，并且可以进行三连发爆炸开火。 这把武器使用现代图形风格的生物图像进行了自定义涂装。\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"突围收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D17045106441843134382\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"name\": \"格洛克 18 型 | 水灵\",\n" +
                "\t\t\"name_color\": \"D2D2D2\",\n" +
                "\t\t\"type\": \"保密 手枪\",\n" +
                "\t\t\"market_name\": \"格洛克 18 型 | 水灵 (崭新出厂)\",\n" +
                "\t\t\"market_hash_name\": \"Glock-18 | Water Elemental (Factory New)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D17045106441843134382\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_Pistol\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"手枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_glock\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"格洛克 18 型\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_community_4\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"突围收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"normal\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"普通\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Legendary_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"保密\",\n" +
                "\t\t\t\"color\": \"d32ce6\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory0\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"崭新出厂\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"5334431299\",\n" +
                "\t\t\"instanceid\": \"188530170\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot7HxfDhjxszJemkV09G3h5SOhe7LP7LWnn8fvJYh3-qR942higTmqBZpYGild4adIQQ5ZA6B_AC3lebo0ce-78vOnGwj5HeAJ9sV6g\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot7HxfDhjxszJemkV09G3h5SOhe7LP7LWnn9u5MRjjeyPod-l3VfkqRJoMWnxd9OQcQdoMljYqVO5xLi-g8e16JXOnSNh6XYlsGGdwUI-f1fsZg\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 略有磨损\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"该物品具有 StatTrak™ 技术，在其所有者装备时能跟踪若干统计数据。\",\n" +
                "\t\t\t\"color\": \"99ccff\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"该物品记录已认证杀敌数。\",\n" +
                "\t\t\t\"color\": \"CF6A32\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"强大又可靠，AK-47 是世上最流行的突击步枪之一。近距离内控制好的短点射极为致命。 这把武器将花纹和严谨的漏印结合起来，通过热转印贴花加强细节，然后进行了自定义涂装。\\n\\n<i>小心翼翼的娜奥米啊…… 和你干同一个行当的人可不是以长命著称的呢 -《盾与蛇（下）》</i>\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"幻彩 2 号收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"<br><div id=\\\"sticker_info\\\" name=\\\"sticker_info\\\" title=\\\"印花\\\" style=\\\"border: 2px solid rgb(102, 102, 102); border-radius: 6px; width=100; margin:4px; padding:8px;\\\"><center><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/community2022/street_artist_glitter.da23d5efea044dfd36cce81b5f0619c49da91737.png\\\"><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/rio2022/sig_fame_glitter.e6f2223dbeb984a97492b2f9e8366f6cdf67b5e5.png\\\"><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/rio2022/sig_fame_glitter.e6f2223dbeb984a97492b2f9e8366f6cdf67b5e5.png\\\"><br>印花: 溜光大道艺人（闪耀）, fame（闪耀）| 2022年里约热内卢锦标赛, fame（闪耀）| 2022年里约热内卢锦标赛</center></div>\"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D2657307142714950943\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"name\": \"AK-47（StatTrak™） | 精英之作\",\n" +
                "\t\t\"name_color\": \"CF6A32\",\n" +
                "\t\t\"type\": \"StatTrak™ 军规级 步枪\",\n" +
                "\t\t\"market_name\": \"AK-47（StatTrak™） | 精英之作 (略有磨损)\",\n" +
                "\t\t\"market_hash_name\": \"StatTrak™ AK-47 | Elite Build (Minimal Wear)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D2657307142714950943\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_Rifle\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"步枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_ak47\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"AK-47\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_community_7\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"幻彩 2 号收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"strange\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"StatTrak™\",\n" +
                "\t\t\t\"color\": \"CF6A32\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Rare_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"军规级\",\n" +
                "\t\t\t\"color\": \"4b69ff\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory1\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"略有磨损\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Tournament\",\n" +
                "\t\t\t\"internal_name\": \"Tournament20\",\n" +
                "\t\t\t\"localized_category_name\": \"锦标赛\",\n" +
                "\t\t\t\"localized_tag_name\": \"2022年 IEM 里约锦标赛\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"TournamentTeam\",\n" +
                "\t\t\t\"internal_name\": \"Team109\",\n" +
                "\t\t\t\"localized_category_name\": \"战队\",\n" +
                "\t\t\t\"localized_tag_name\": \"Outsiders\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ProPlayer\",\n" +
                "\t\t\t\"internal_name\": \"fame\",\n" +
                "\t\t\t\"localized_category_name\": \"职业选手\",\n" +
                "\t\t\t\"localized_tag_name\": \"fame（Petr Bolyshev）\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"3035582416\",\n" +
                "\t\t\"instanceid\": \"302028390\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpou6ryFAZh7PXJdTh94czhq4yCkP_gfb7VwzgGsZAn3L_Codigjge2-0M9Z2D1LIaTcAM_ZQrTqAO7lbrug5Ci_MOeD_P4rHw\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpou6ryFAZh7PXJdTh94czhq4yCkP_gDLfQhGxUpsB137qWrNmj3gK3_0dpNWqhJIbEdg84aVjW-FS_xbzs18S56MnIz3R9-n51LuzV2Hc\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 崭新出厂\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"非常好用，但价格昂贵，德国制造的 MP7 微型冲锋枪非常适合高强度的近距离作战。 这把武器以铬为外表，使用喷笔涂上了相互渐变的透明涂装。\\n\\n<i>这不仅仅是一把武器，更是一种潮流 - 实习军火商伊莫金</i>\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"2018 炼狱小镇收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D11549064306218822699\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"name\": \"MP7 | 渐变之色\",\n" +
                "\t\t\"name_color\": \"D2D2D2\",\n" +
                "\t\t\"type\": \"受限 微型冲锋枪\",\n" +
                "\t\t\"market_name\": \"MP7 | 渐变之色 (崭新出厂)\",\n" +
                "\t\t\"market_hash_name\": \"MP7 | Fade (Factory New)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D11549064306218822699\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_SMG\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"微型冲锋枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_mp7\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"MP7\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_inferno_2\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"2018 炼狱小镇收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"normal\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"普通\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Mythical_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"受限\",\n" +
                "\t\t\t\"color\": \"8847ff\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory0\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"崭新出厂\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"5604816244\",\n" +
                "\t\t\"instanceid\": \"188530170\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot7HxfDhjxszJemkV09-5k5SDnvnzIITck29Y_cg_i-rHoYrw3VLs_RZlZ2umLYSTdQc_Zl7ZrwPoxefp18Du7Z-bwHZh6z5iuyiJTfqMXg\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot7HxfDhjxszJemkV09-5k5SDnvnzIITck29Y_chOhujT8om7iVey_xU5Zj_7ItOcdgRraFrW_VC_xujm0MTquJTPmyQx6yJw7Hvfzgv3309IyM9cTA\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 久经沙场\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"该物品具有 StatTrak™ 技术，在其所有者装备时能跟踪若干统计数据。\",\n" +
                "\t\t\t\"color\": \"99ccff\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"该物品记录已认证杀敌数。\",\n" +
                "\t\t\t\"color\": \"CF6A32\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"强大又可靠，AK-47 是世上最流行的突击步枪之一。近距离内控制好的短点射极为致命。 这把武器采用醒目的绿色和蓝色渐变进行了自定义涂装。\\n\\n<i>触感冰冷</i>\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"反冲收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"<br><div id=\\\"sticker_info\\\" name=\\\"sticker_info\\\" title=\\\"印花\\\" style=\\\"border: 2px solid rgb(102, 102, 102); border-radius: 6px; width=100; margin:4px; padding:8px;\\\"><center><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/paris2023/sig_history_glitter.ba3a5e7fea2a3ab885a1479ad34438fa1d49d997.png\\\"><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/paris2023/sig_history_glitter.ba3a5e7fea2a3ab885a1479ad34438fa1d49d997.png\\\"><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/paris2023/sig_history_glitter.ba3a5e7fea2a3ab885a1479ad34438fa1d49d997.png\\\"><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/paris2023/sig_history_glitter.ba3a5e7fea2a3ab885a1479ad34438fa1d49d997.png\\\"><br>印花: History（闪耀）| 2023年巴黎锦标赛, History（闪耀）| 2023年巴黎锦标赛, History（闪耀）| 2023年巴黎锦标赛, History（闪耀）| 2023年巴黎锦标赛</center></div>\"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D9711531615027070335\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"fraudwarnings\": [\"名称标签：“我身后的冰连火都融不了”\"],\n" +
                "\t\t\"name\": \"AK-47（StatTrak™） | 可燃冰\",\n" +
                "\t\t\"name_color\": \"CF6A32\",\n" +
                "\t\t\"type\": \"StatTrak™ 保密 步枪\",\n" +
                "\t\t\"market_name\": \"AK-47（StatTrak™） | 可燃冰 (久经沙场)\",\n" +
                "\t\t\"market_hash_name\": \"StatTrak™ AK-47 | Ice Coaled (Field-Tested)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D9711531615027070335\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_Rifle\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"步枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_ak47\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"AK-47\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_community_31\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"反冲收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"strange\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"StatTrak™\",\n" +
                "\t\t\t\"color\": \"CF6A32\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Legendary_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"保密\",\n" +
                "\t\t\t\"color\": \"d32ce6\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory2\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"久经沙场\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Tournament\",\n" +
                "\t\t\t\"internal_name\": \"Tournament21\",\n" +
                "\t\t\t\"localized_category_name\": \"锦标赛\",\n" +
                "\t\t\t\"localized_tag_name\": \"2023年 BLAST.tv 巴黎锦标赛\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"TournamentTeam\",\n" +
                "\t\t\t\"internal_name\": \"Team121\",\n" +
                "\t\t\t\"localized_category_name\": \"战队\",\n" +
                "\t\t\t\"localized_tag_name\": \"Fluxo\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ProPlayer\",\n" +
                "\t\t\t\"internal_name\": \"history\",\n" +
                "\t\t\t\"localized_category_name\": \"职业选手\",\n" +
                "\t\t\t\"localized_tag_name\": \"History（Allan Botton）\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"4307675336\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgposr-kLAtl7PLFTjVH5c-iq5CHlvnnDKvEhHtd7fp9g-7J4cKi2wOyqUVuZ2vyLNKUdgc2ZA3S_1jryLjv1MC-vMycyHRhvHEisH-PgVXp1mPRINq1\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgposr-kLAtl7PLFTjVH5c-iq5CHlvnnDKvEhHtd7fp9g-7J4bP5iUazrl1rYG6ldoCXdgQ_aQzR_lC2xLvtgJ_ptcvJmCRhunF35HnfmUS31BgecKUx0pI1kjtX\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 崭新出厂\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"杀伤力和它的卖价一样高，沙漠之鹰是一把很难驾驭，而在远距离又出乎意料精准的经典手枪。 枪身饰有紫色电镀地图花纹，宛如手工绘制一般。\\n\\n<i>计划是计划，执行是执行。</i>\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"浩劫收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"<br><div id=\\\"sticker_info\\\" name=\\\"sticker_info\\\" title=\\\"印花\\\" style=\\\"border: 2px solid rgb(102, 102, 102); border-radius: 6px; width=100; margin:4px; padding:8px;\\\"><center><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/rmr2020/nip.da62b427774f6a6fea21c05d2136ef072538d2c2.png\\\"><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/rmr2020/navi.afde6ff3f9bb974066e6791013b44babab5b5f27.png\\\"><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/broken_fang/battle_scarred.8f95410ed52cdf856221264b667960419e6bbde0.png\\\"><img width=64 height=48 src=\\\"https://steamcdn-a.akamaihd.net/apps/730/icons/econ/stickers/broken_fang/battle_scarred.8f95410ed52cdf856221264b667960419e6bbde0.png\\\"><br>印花: Ninjas in Pyjamas | 2020 RMR, Natus Vincere | 2020 RMR, 战痕累累, 战痕累累</center></div>\"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D5192308185557823215\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"name\": \"沙漠之鹰 | 午夜凶匪\",\n" +
                "\t\t\"name_color\": \"D2D2D2\",\n" +
                "\t\t\"type\": \"军规级 手枪\",\n" +
                "\t\t\"market_name\": \"沙漠之鹰 | 午夜凶匪 (崭新出厂)\",\n" +
                "\t\t\"market_hash_name\": \"Desert Eagle | Night Heist (Factory New)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D5192308185557823215\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_Pistol\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"手枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_deagle\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"沙漠之鹰\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_op10_t\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"浩劫收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"normal\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"普通\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Rare_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"军规级\",\n" +
                "\t\t\t\"color\": \"4b69ff\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory0\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"崭新出厂\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Tournament\",\n" +
                "\t\t\t\"internal_name\": \"Tournament17\",\n" +
                "\t\t\t\"localized_category_name\": \"锦标赛\",\n" +
                "\t\t\t\"localized_tag_name\": \"2020 RMR\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"TournamentTeam\",\n" +
                "\t\t\t\"internal_name\": \"Team1\",\n" +
                "\t\t\t\"localized_category_name\": \"战队\",\n" +
                "\t\t\t\"localized_tag_name\": \"Ninjas in Pyjamas\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"TournamentTeam\",\n" +
                "\t\t\t\"internal_name\": \"Team12\",\n" +
                "\t\t\t\"localized_category_name\": \"战队\",\n" +
                "\t\t\t\"localized_tag_name\": \"Natus Vincere\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"3770751316\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgposbaqKAxf0Ob3djFN79fnzL-cluX5MrLVk2Vu5Mx2gv2P8dWsiQKyrxFoMGj3Io_BcwA6YFDSq1a6lLq91J7o6Z3MzHVhvHFz4GGdwUK867nN7w\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgposbaqKAxf0Ob3djFN79fnzL-cluX5MrLVk2Vu5Mx2gv3--Y3nj1H6r0plMm-lcNSRIQc6Z1GE-1e6wObt1JG46cmbmHo37yAn4HjfmUTmhAYMMLKVxXRrDQ\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 崭新出厂\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"格洛克 18 型是把耐用的第一回合手枪，非常适合对付那些没有穿护甲的对手，并且可以进行三连发爆炸开火。 黄黑底色上涂有来势汹汹的子弹与穿着粉红夹克的少女。\\n\\n<i>“你说嘘的时候我就停火。”</i>\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"棱彩2号收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D2595450432077729572\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"name\": \"格洛克 18 型 | 子弹皇后\",\n" +
                "\t\t\"name_color\": \"D2D2D2\",\n" +
                "\t\t\"type\": \"隐秘 手枪\",\n" +
                "\t\t\"market_name\": \"格洛克 18 型 | 子弹皇后 (崭新出厂)\",\n" +
                "\t\t\"market_hash_name\": \"Glock-18 | Bullet Queen (Factory New)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D2595450432077729572\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_Pistol\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"手枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_glock\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"格洛克 18 型\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_community_25\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"棱彩2号收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"normal\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"普通\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Ancient_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"隐秘\",\n" +
                "\t\t\t\"color\": \"eb4b4b\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory0\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"崭新出厂\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"1560433642\",\n" +
                "\t\t\"instanceid\": \"480085569\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot621FAR17PLfYQJP7c-ikZKSqPrxN7LEmyVTsZV33OiT9tys2AG1_UJlZ2HxJ47EIAI_N1CErAe_lOzsgMO66syd1zI97a8kXc4r\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpot621FAR17PLfYQJP7c-ikZKSqPrxN7LEm1Rd6dd2j6fDrNzz3lXmqENlY2yiIoecdg48YlCBqFW_l-a708C96Z_KzCdl7HF2-z-DyCGf5u7A\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 久经沙场\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"高风险，高回报，恶名昭彰的 AWP 因其标志性的枪声和一枪一个的准则而为人熟知。 这把武器将花纹和严谨的漏印结合起来，通过热转印贴花加强细节，然后进行了自定义涂装。\\n\\n<i>蔡斯.特纳曾是个伟大的男人… 你现在可是背负着他那重大的使命啊 –《凤凰与崛起（上）》</i>\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"野火收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D14277657804014900741\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"name\": \"AWP | 精英之作\",\n" +
                "\t\t\"name_color\": \"D2D2D2\",\n" +
                "\t\t\"type\": \"保密 狙击步枪\",\n" +
                "\t\t\"market_name\": \"AWP | 精英之作 (久经沙场)\",\n" +
                "\t\t\"market_hash_name\": \"AWP | Elite Build (Field-Tested)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D14277657804014900741\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_SniperRifle\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"狙击步枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_awp\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"AWP\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_community_11\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"野火收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"normal\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"普通\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Legendary_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"保密\",\n" +
                "\t\t\t\"color\": \"d32ce6\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory2\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"久经沙场\"\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"appid\": 730,\n" +
                "\t\t\"classid\": \"2735679255\",\n" +
                "\t\t\"instanceid\": \"188530170\",\n" +
                "\t\t\"currency\": 0,\n" +
                "\t\t\"background_color\": \"\",\n" +
                "\t\t\"icon_url\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpoo6m1FBRp3_bGcjhQ09-jq5WYh8j3Jq_um25V4dB8xLCXod2jjgHt_0M4MmrwcoCWIFM-MFDUqATtxey-g5XouJzNwHdgsyc8pSGKt-UEtYA\",\n" +
                "\t\t\"icon_url_large\": \"-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpoo6m1FBRp3_bGcjhQ09-jq5WYh8j3Jq_um25V4dB8teXA54vwxg3i_0JqNWz6IIbBIwU9N17TqAS-kObr18PvuJ-Yz3E3viEm7HrelhKpwUYbXkaqFVw\",\n" +
                "\t\t\"descriptions\": [{\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"外观： 久经沙场\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"该物品具有 StatTrak™ 技术，在其所有者装备时能跟踪若干统计数据。\",\n" +
                "\t\t\t\"color\": \"99ccff\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"该物品记录已认证杀敌数。\",\n" +
                "\t\t\t\"color\": \"CF6A32\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"从《反恐精英：起源》中穿越而来，是玩家们的最爱，消音版 USP 手枪装有一个可拆卸的消音器，降低后坐力的同时还能抑制住容易引起注意的枪声。 这把武器在银黑底色上采用了粉色点缀。\\n\\n<i>保持冷静</i>\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \"命悬一线收藏品\",\n" +
                "\t\t\t\"color\": \"9da1a9\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"html\",\n" +
                "\t\t\t\"value\": \" \"\n" +
                "\t\t}],\n" +
                "\t\t\"tradable\": 1,\n" +
                "\t\t\"actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20S%owner_steamid%A%assetid%D16312113606057376555\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"name\": \"USP 消音版（StatTrak™） | 脑洞大开\",\n" +
                "\t\t\"name_color\": \"CF6A32\",\n" +
                "\t\t\"type\": \"StatTrak™ 保密 手枪\",\n" +
                "\t\t\"market_name\": \"USP 消音版（StatTrak™） | 脑洞大开 (久经沙场)\",\n" +
                "\t\t\"market_hash_name\": \"StatTrak™ USP-S | Cortex (Field-Tested)\",\n" +
                "\t\t\"market_actions\": [{\n" +
                "\t\t\t\"link\": \"steam://rungame/730/76561202255233023/+csgo_econ_action_preview%20M%listingid%A%assetid%D16312113606057376555\",\n" +
                "\t\t\t\"name\": \"在游戏中检视…\"\n" +
                "\t\t}],\n" +
                "\t\t\"commodity\": 0,\n" +
                "\t\t\"market_tradable_restriction\": 7,\n" +
                "\t\t\"marketable\": 1,\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"category\": \"Type\",\n" +
                "\t\t\t\"internal_name\": \"CSGO_Type_Pistol\",\n" +
                "\t\t\t\"localized_category_name\": \"类型\",\n" +
                "\t\t\t\"localized_tag_name\": \"手枪\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Weapon\",\n" +
                "\t\t\t\"internal_name\": \"weapon_usp_silencer\",\n" +
                "\t\t\t\"localized_category_name\": \"武器\",\n" +
                "\t\t\t\"localized_tag_name\": \"USP 消音版\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"ItemSet\",\n" +
                "\t\t\t\"internal_name\": \"set_community_19\",\n" +
                "\t\t\t\"localized_category_name\": \"收藏品\",\n" +
                "\t\t\t\"localized_tag_name\": \"命悬一线收藏品\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Quality\",\n" +
                "\t\t\t\"internal_name\": \"strange\",\n" +
                "\t\t\t\"localized_category_name\": \"类别\",\n" +
                "\t\t\t\"localized_tag_name\": \"StatTrak™\",\n" +
                "\t\t\t\"color\": \"CF6A32\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Rarity\",\n" +
                "\t\t\t\"internal_name\": \"Rarity_Legendary_Weapon\",\n" +
                "\t\t\t\"localized_category_name\": \"品质\",\n" +
                "\t\t\t\"localized_tag_name\": \"保密\",\n" +
                "\t\t\t\"color\": \"d32ce6\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"category\": \"Exterior\",\n" +
                "\t\t\t\"internal_name\": \"WearCategory2\",\n" +
                "\t\t\t\"localized_category_name\": \"外观\",\n" +
                "\t\t\t\"localized_tag_name\": \"久经沙场\"\n" +
                "\t\t}]\n" +
                "\t}],\n" +
                "\t\"total_inventory_count\": 11,\n" +
                "\t\"success\": 1,\n" +
                "\t\"rwgrsn\": -2\n" +
                "}";
        ObjectMapper objectMapper=new ObjectMapper();
        InventoryDto json = null;
        try {
            json = objectMapper.readValue(j, InventoryDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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
        List<InventoryDto.DescriptionsDTOX> descriptions = json.getDescriptions();
        for(InventoryDto.DescriptionsDTOX item:descriptions){
            InvDescPageReqVO invDescPageReqVO=new InvDescPageReqVO();
//            invDescPageReqVO.set(steamId);
            invDescPageReqVO.setAppid(item.getAppid());
//            invDescPageReqVO.setAppid(123);
            invDescPageReqVO.setClassid(item.getClassid());
            invDescPageReqVO.setInstanceid(item.getInstanceid());
            PageResult<InvDescDO> invDescDOPageResult = invDescMapper.selectPage(invDescPageReqVO);
            if(invDescDOPageResult.getTotal()>0){
                Optional<InvDescDO> first = invDescDOPageResult.getList().stream().findFirst();
                InvDescDO invDescDO = first.get();
                invDescDO.setAppid(item.getAppid());
                invDescDO.setClassid(item.getClassid());
                invDescDO.setInstanceid(item.getInstanceid());
                invDescDO.setCurrency(item.getCurrency());
                invDescDO.setBackgroundColor(item.getBackgroundColor());
                invDescDO.setIconUrl(item.getIconUrl());
                invDescDO.setIconUrlLarge(item.getIconUrlLarge());
                invDescDO.setDescriptions(item.getDescriptions());
                invDescDO.setTradable(item.getTradable());
                invDescDO.setActions(item.getActions());
                invDescDO.setName(item.getName());
                invDescDO.setNameColor(item.getNameColor());
                invDescDO.setType(item.getType());
                invDescDO.setMarketName(item.getMarketName());
                invDescDO.setMarketHashName(item.getMarketHashName());
                invDescDO.setMarketActions(item.getMarketActions());
                invDescDO.setCommodity(item.getCommodity());
                invDescDO.setMarketTradableRestriction(item.getMarketTradableRestriction());
                invDescDO.setMarketable(item.getMarketable());
                invDescDO.setTags(item.getTags());
                //解析tags
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> type = item.getTags().stream().filter(i -> i.getCategory().equals("Type")).findFirst();
                if(type.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = type.get();
                    invDescDO.setSelType(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> weapon = item.getTags().stream().filter(i -> i.getCategory().equals("Weapon")).findFirst();
                if(weapon.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = weapon.get();
                    invDescDO.setSelWeapon(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> itemSet = item.getTags().stream().filter(i -> i.getCategory().equals("ItemSet")).findFirst();
                if(itemSet.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = itemSet.get();
                    invDescDO.setSelItemset(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> quality = item.getTags().stream().filter(i -> i.getCategory().equals("Quality")).findFirst();
                if(quality.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = quality.get();
                    invDescDO.setSelQuality(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> rarity = item.getTags().stream().filter(i -> i.getCategory().equals("Rarity")).findFirst();
                if(rarity.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = rarity.get();
                    invDescDO.setSelRarity(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> exterior = item.getTags().stream().filter(i -> i.getCategory().equals("Exterior")).findFirst();
                if(exterior.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = exterior.get();
                    invDescDO.setSelExterior(tagsDTO.getInternalName());
                }
                invDescMapper.updateById(invDescDO);
            }else{
                InvDescDO invDescDO=new InvDescDO();
                invDescDO.setAppid(item.getAppid());
                invDescDO.setClassid(item.getClassid());
                invDescDO.setInstanceid(item.getInstanceid());
                invDescDO.setCurrency(item.getCurrency());
                invDescDO.setBackgroundColor(item.getBackgroundColor());
                invDescDO.setIconUrl(item.getIconUrl());
                invDescDO.setIconUrlLarge(item.getIconUrlLarge());
                invDescDO.setDescriptions(item.getDescriptions());
                invDescDO.setTradable(item.getTradable());
                invDescDO.setActions(item.getActions());
                invDescDO.setName(item.getName());
                invDescDO.setNameColor(item.getNameColor());
                invDescDO.setType(item.getType());
                invDescDO.setMarketName(item.getMarketName());
                invDescDO.setMarketHashName(item.getMarketHashName());
                invDescDO.setMarketActions(item.getMarketActions());
                invDescDO.setCommodity(item.getCommodity());
                invDescDO.setMarketTradableRestriction(item.getMarketTradableRestriction());
                invDescDO.setMarketable(item.getMarketable());
                invDescDO.setTags(item.getTags());
                //解析tags
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> type = item.getTags().stream().filter(i -> i.getCategory().equals("Type")).findFirst();
                if(type.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = type.get();
                    invDescDO.setSelType(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> weapon = item.getTags().stream().filter(i -> i.getCategory().equals("Weapon")).findFirst();
                if(weapon.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = weapon.get();
                    invDescDO.setSelWeapon(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> itemSet = item.getTags().stream().filter(i -> i.getCategory().equals("ItemSet")).findFirst();
                if(itemSet.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = itemSet.get();
                    invDescDO.setSelItemset(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> quality = item.getTags().stream().filter(i -> i.getCategory().equals("Quality")).findFirst();
                if(quality.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = quality.get();
                    invDescDO.setSelQuality(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> rarity = item.getTags().stream().filter(i -> i.getCategory().equals("Rarity")).findFirst();
                if(rarity.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = rarity.get();
                    invDescDO.setSelRarity(tagsDTO.getInternalName());
                }
                Optional<InventoryDto.DescriptionsDTOX.TagsDTO> exterior = item.getTags().stream().filter(i -> i.getCategory().equals("Exterior")).findFirst();
                if(exterior.isPresent()){
                    InventoryDto.DescriptionsDTOX.TagsDTO tagsDTO = exterior.get();
                    invDescDO.setSelExterior(tagsDTO.getInternalName());
                }
                invDescMapper.insert(invDescDO);
            }
        }
        return json;
    }


}
