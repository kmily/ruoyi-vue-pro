package cn.iocoder.yudao.module.steam.service.uu;

import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiPayWalletRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@Slf4j
public class UUService {
    @Autowired
    private OpenApiService openApiService;

    /**
     * 余额查询
     * @return
     */
    public ApiPayWalletRespVO getAssetsInfo() {
        return openApiService.requestUU("https://gw-openapi.youpin898.com/open/v1/api/getAssetsInfo",new OpenApiReqVo<Serializable>(), ApiPayWalletRespVO.class);
    }
}
