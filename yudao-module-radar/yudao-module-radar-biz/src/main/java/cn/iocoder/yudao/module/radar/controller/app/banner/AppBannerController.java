package cn.iocoder.yudao.module.radar.controller.app.banner;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.radar.controller.admin.banner.vo.BannerRespVO;
import cn.iocoder.yudao.module.radar.convert.banner.BannerConvert;
import cn.iocoder.yudao.module.radar.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.module.radar.service.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @author: XIA
 */
@RestController
@RequestMapping("/radar/banner")
@Tag(name = "用户APP- 首页Banner")
@Validated
public class AppBannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    @Operation(summary = "获得banner列表")
    public CommonResult<List<BannerRespVO>> getBannerList() {
        List<BannerDO> list = bannerService.getBannerList();
        return success(BannerConvert.INSTANCE.convertList(list));
    }

}
