package cn.iocoder.yudao.module.steam.controller.app.selling;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.InvPageReqVo;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.SellingReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.service.selling.SellingExtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/steam-app/sale")
@Slf4j
@Tag(name="饰品出售管理")
public class AppSellingController {

    @Resource
    private SellingExtService sellingExtService;

    @GetMapping("/onsale")
    @Operation(summary = "饰品上架出售")
    public CommonResult<SellingDO> getOnSale(@Valid InvPageReqVo invPageReqVo) {
        // 入参：id  price
        // 查询 steam_inv
        SellingDO invPage = sellingExtService.getToSale(invPageReqVo);
        return CommonResult.success(invPage);

    }
    @GetMapping("/offsale")
    @Operation(summary = "饰品下架")
    public CommonResult<Optional<SellingDO>> getOffSale(@Valid SellingReqVo sellingReqVo) {

        Optional<SellingDO> invPage = sellingExtService.getOffSale(sellingReqVo);
        return CommonResult.success(invPage);

    }
}

