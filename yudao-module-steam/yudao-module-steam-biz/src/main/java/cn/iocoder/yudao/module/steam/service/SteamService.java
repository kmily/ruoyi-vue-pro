package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.binduser.vo.AppBindUserApiKeyReqVO;
import cn.iocoder.yudao.module.steam.controller.app.binduser.vo.AppUnBindUserReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.ioinvupdate.IOInvUpdateService;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import cn.iocoder.yudao.module.steam.service.steam.OpenApi;
import cn.iocoder.yudao.module.steam.service.steam.SteamMaFile;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Resource
    private InvMapper invMapper;


    @Resource
    private IOInvUpdateService ioInvUpdateService;



    /**
     * 帐号绑定
     * @param openApi
     * @return
     */
    public int bind(OpenApi openApi) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
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
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
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
    /**
     * 帐号绑定
     * @param reqVO 解绑ID
     * @return
     */
    public int changeWebApi(AppBindUserApiKeyReqVO reqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        BindUserDO bindUserDO = bindUserMapper.selectById(reqVO.getBindId());
        if(!bindUserDO.getUserId().equals(loginUser.getId())){
            throw new ServiceException(-1,"无权限操作");
        }
        if(!bindUserDO.getUserType().equals(loginUser.getUserType())){
            throw new ServiceException(-1,"无权限操作");
        }
        int i = bindUserMapper.updateById(new BindUserDO().setId(bindUserDO.getId()).setApiKey(reqVO.getApiKey()));
        if(i<0){
            throw new ServiceException(-1,"操作失败");
        }
        return i;
    }

    /**
     * 获取临时帐号
     * @param loginUser
     * @param tradeUrl
     * @return
     */
    public BindUserDO getTempBindUserByLogin(LoginUser loginUser,String tradeUrl,Boolean autoCreate){
        Optional<BindUserDO> first = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eqIfPresent(BindUserDO::getUserId, loginUser.getId())
                .eqIfPresent(BindUserDO::getTradeUrl, tradeUrl)
                .eqIfPresent(BindUserDO::getIsTempAccount, true)
                .orderByDesc(BindUserDO::getId)).stream().findFirst();
        log.info("getTempBindUserByLogin",first);
        if(!first.isPresent() && autoCreate){
            if(autoCreate){
                log.info("getTempBindUserByLogin--autoCreate",first);
                BindUserDO bindUserDO=new BindUserDO().setUserId(loginUser.getId())
                        .setUserType(loginUser.getUserType()).setTradeUrl(tradeUrl).setIsTempAccount(true)
                        .setSteamId("temp"+System.currentTimeMillis());
                bindUserMapper.insert(bindUserDO);
                return bindUserDO;
            }
            return null;
        }
        log.info("getTempBindUserByLogin--last ret{}",first.get());
        return first.get();
    }

    /**
     * 获取bind用户
     * @param loginUser
     * @return
     */
    public List<BindUserDO> getBindUserByLoginUser(LoginUser loginUser){
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        List<BindUserDO> bindUserDOS = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eqIfPresent(BindUserDO::getUserId, loginUser.getId())
                .eqIfPresent(BindUserDO::getIsTempAccount,false)
                .orderByDesc(BindUserDO::getId));
        for(BindUserDO bindUserDO:bindUserDOS){
            bindUserDO.setSteamPassword(Objects.isNull(bindUserDO.getSteamPassword())?"0":"1");
            if(Objects.nonNull(bindUserDO.getMaFile())){
                bindUserDO.setMaFile(new SteamMaFile().setSharedSecret(bindUserDO.getMaFile().getSharedSecret()));
            }
        }
        return bindUserDOS;
    }
    public BindUserDO getBindUserByLoginUserAndSteamId(LoginUser loginUser,String steamId){
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        BindUserDO bindUserDO = bindUserMapper.selectOne(new LambdaQueryWrapperX<BindUserDO>()
                .eqIfPresent(BindUserDO::getUserId, loginUser.getId())
                .eqIfPresent(BindUserDO::getSteamId,steamId)
                .eqIfPresent(BindUserDO::getIsTempAccount,false)
                .orderByDesc(BindUserDO::getId));
        bindUserDO.setSteamPassword(Objects.isNull(bindUserDO.getSteamPassword())?"0":"1");
        bindUserDO.setMaFile(null);
        return bindUserDO;
    }
    public void bindMaFile(byte[] maFileJsonByte,String password,Integer bindUserId) throws JsonProcessingException {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if(Objects.isNull(loginUser)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        BindUserDO bindUserDO = bindUserMapper.selectById(bindUserId);
        if(Objects.isNull(bindUserDO)){
            throw new ServiceException(-1,"绑定失败，请检查后再试。");
        }
        if(bindUserDO.getIsTempAccount()){
            throw new ServiceException(-1,"不支持绑定");
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
        bindUserDO.setSteamPassword(password);
        bindUserDO.setMaFile(steamMaFile);
        if(steamWeb.checkLogin(bindUserDO)){
            bindUserDO.setLoginCookie(steamWeb.getCookieString());
        }
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
        if(steamWeb.getTreadUrl().isPresent()){
            bindUserDO.setTradeUrl(steamWeb.getTreadUrl().get());
        }
        if(steamWeb.getWebApiKey().isPresent()){
            bindUserDO.setApiKey(steamWeb.getWebApiKey().get());
        }
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
        InventoryDto inventoryDto = ioInvUpdateService.gitInvFromSteam(bindUserDO);
        if(inventoryDto != null){
            ioInvUpdateService.firstInsertInventory(inventoryDto,bindUserDO);
        }
    }

    /**
     * 验证用户是否已经被绑定
     * @param openApi steam open返回的信息
     * @return true 成功  false 失败
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
            log.error("解析出错原因{}",e.getMessage());
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
}
