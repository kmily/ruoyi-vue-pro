package cn.iocoder.yudao.module.steam.controller.app.binduser;

import cn.hutool.core.io.IoUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.controller.app.binduser.vo.AppBindUserApiKeyReqVO;
import cn.iocoder.yudao.module.steam.controller.app.binduser.vo.AppBindUserMaFileReqVO;
import cn.iocoder.yudao.module.steam.controller.app.binduser.vo.AppUnBindUserReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.steam.OpenApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "steam后台 -  steam用户绑定")
@RestController
@RequestMapping("/steam-app/bind-user")
@Validated
@Slf4j
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
        public CommonResult<Integer> openApiBack(@RequestBody @Validated OpenApi openApi) {
            log.info("接入的入参{}",openApi);
        int bind = steamService.bind(openApi);
        return success(bind);
    }
    @GetMapping("/steam/list")
    @Operation(summary = "steam绑定列表", description = "上传ma文件")
    @PreAuthenticated
    @OperateLog(logArgs = false)
    public CommonResult<List<BindUserDO>> steamList(){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        return success(steamService.getBindUserByLoginUser(loginUser));
    }
    @PostMapping("/unBind")
    @Operation(summary = "解绑steam")
    @PreAuthenticated
    public CommonResult<Integer> unBind(@RequestBody @Valid AppUnBindUserReqVO reqVO)  {
        int i = steamService.unBind(reqVO);
        return success(i);
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
    @PostMapping("/changeWebApi")
    @Operation(summary = "解绑steam")
    @PreAuthenticated
    public CommonResult<Integer> changeWebApi(@RequestBody @Valid AppBindUserApiKeyReqVO reqVO)  {
        int i = steamService.changeWebApi(reqVO);
        return success(i);
    }

}