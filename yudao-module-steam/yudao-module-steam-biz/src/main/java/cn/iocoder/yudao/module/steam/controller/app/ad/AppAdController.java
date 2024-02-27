package cn.iocoder.yudao.module.steam.controller.app.ad;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.steam.controller.app.ad.vo.AdBlockReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.ad.AdDO;
import cn.iocoder.yudao.module.steam.service.adblock.AdBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "获取下拉选择信息")
@RestController
@RequestMapping("steam/ad")
@Validated
public class AppAdController {

    @Resource
    private AdBlockService adBlockService;

    @GetMapping("/getAd")
    @Operation(summary = "饰品在售预览")
    public CommonResult<List<AdDO>> getAd(@RequestBody @Valid AdBlockReqVO adBlockReqVO){
        List<AdDO> enableAdListByBlockId = adBlockService.getEnableAdListByBlockId(adBlockReqVO.getBlockId());
        return success(enableAdListByBlockId);
    }
}
