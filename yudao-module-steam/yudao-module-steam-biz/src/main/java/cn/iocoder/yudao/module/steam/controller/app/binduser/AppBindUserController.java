package cn.iocoder.yudao.module.steam.controller.app.binduser;

import cn.hutool.core.io.IoUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.controller.app.binduser.vo.AppBindUserMaFileReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.steam.OpenApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "steam后台 -  steam用户绑定")
@RestController
@RequestMapping("/steam-app/bind-user")
@Validated
public class AppBindUserController {
    private SteamService steamService;
    private ConfigService configService;
    @Autowired
    public void setSteamService(SteamService steamService) {
        this.steamService = steamService;
    }

    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * 查询 steam用户绑定列表
     */
    @GetMapping("/openapi")
    public CommonResult<String> openApi() throws IOException {
        ConfigDO configByKey = configService.getConfigByKey("steam.host");
        ConfigDO siteConfig = configService.getConfigByKey("site.host");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(configByKey.getValue());
        stringBuffer.append("/openid/login?");
        stringBuffer.append("openid.return_to=").append(URLEncoder.encode(siteConfig.getValue() + "api/steam/user/openapi/back", "utf-8"));
        stringBuffer.append("&openid.realm=").append(URLEncoder.encode(siteConfig.getValue() + "api/steam/user/openapi/back", "utf-8"));
        stringBuffer.append("&").append("openid.ns=").append(URLEncoder.encode("http://specs.openid.net/auth/2.0", "UTF8"));
        stringBuffer.append("&").append("openid.identity=").append(URLEncoder.encode("http://specs.openid.net/auth/2.0/identifier_select", "UTF8"));
        stringBuffer.append("&").append("openid.claimed_id=").append(URLEncoder.encode("http://specs.openid.net/auth/2.0/identifier_select", "UTF8"));
        stringBuffer.append("&").append("openid.mode=").append(URLEncoder.encode("checkid_setup", "UTF8"));
        return success(stringBuffer.toString());
    }
    /**
     * 导出 steam用户绑定列表
     */
    @Operation(summary = "steam用户绑定")
    @PreAuthenticated
    @PostMapping("/openapi/back")
    public CommonResult<Integer> openApiBack(
            @RequestParam("openid.ns") String ns,
            @RequestParam("openid.mode") String mode,
            @RequestParam("openid.op_endpoint") String opEndpoint ,
            @RequestParam("openid.claimed_id") String claimedId,
            @RequestParam("openid.identity") String identity,
            @RequestParam("openid.return_to") String return_to,
            @RequestParam("openid.response_nonce") String response_nonce,
            @RequestParam("openid.assoc_handle") String assoc_handle,
            @RequestParam("openid.signed") String signed,
            @RequestParam("openid.sig") String sig
                                             ) {
        OpenApi openApi = new OpenApi();
        openApi.setNs(ns);
        openApi.setMode(mode);
        openApi.setOpEndpoint(opEndpoint);
        openApi.setClaimedId(claimedId);
        openApi.setIdentity(identity);
        openApi.setReturnTo(return_to);
        openApi.setResponseNonce(response_nonce);
        openApi.setAssocHandle(assoc_handle);
        openApi.setSigned(signed);
        openApi.setSig(sig);

        int bind = steamService.bind(openApi);
        return success(1);
    }
    @GetMapping("/steam/list")
    @Operation(summary = "steam绑定列表", description = "上传ma文件")
    @PreAuthenticated
    @OperateLog(logArgs = false)
    public CommonResult<List<BindUserDO>> steamList(){
        return success(steamService.steamList());
    }

    @PostMapping("/upload/mafile")
    @Operation(summary = "上传ma文件", description = "上传ma文件")
    @PreAuthenticated
    @OperateLog(logArgs = false) // 上传文件，没有记录操作日志的必要
    public CommonResult<String> uploadFile(@Valid AppBindUserMaFileReqVO appBindUserMaFileReqVO) throws Exception {
        MultipartFile file = appBindUserMaFileReqVO.getFile();
        steamService.bindMaFile(IoUtil.readBytes(file.getInputStream()),appBindUserMaFileReqVO.getPassword(),appBindUserMaFileReqVO.getBindUserId());
        return success("成功");
    }

}