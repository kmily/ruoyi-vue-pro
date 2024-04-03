package cn.iocoder.yudao.module.steam.controller.app.selling;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.idempotent.core.annotation.Idempotent;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.*;
import cn.iocoder.yudao.module.steam.service.selling.SellingExtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Validated
@RequestMapping("/steam-app/sale")
@Slf4j
@Tag(name="饰品出售管理")
public class AppSellingController {

    @Resource
    private SellingExtService sellingExtService;

    @PostMapping("/batchSale")
    @Operation(summary = "批量上架")
    @Idempotent(timeout = 30, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    @PreAuthenticated
    public CommonResult<String> batchSale(@RequestBody @Valid BatchSellReqVo reqVo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        sellingExtService.batchSale(reqVo,loginUser);
        return CommonResult.success("上架成功");
    }

    @PostMapping("/batchOffSale")
    @Operation(summary = "批量下架")
    @Idempotent(timeout = 30, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    @PreAuthenticated
    public CommonResult<String> batchOffSale(@RequestBody @Valid BatchOffSellReqVo reqVo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        sellingExtService.batchOffSale(reqVo,loginUser);
        return CommonResult.success("下架成功");
    }

    @PostMapping("/batchChangePrice")
    @Operation(summary = "批量改价")
    @Idempotent(timeout = 30, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    @PreAuthenticated
    public CommonResult<String> batchChangePrice(@RequestBody @Valid BatchChangePriceReqVo reqVo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        sellingExtService.batchChangePrice(reqVo,loginUser);
        return CommonResult.success("改价成功");
    }

    @GetMapping("/user/sellingMerge")
    @Operation(summary = "出售合并")
    public CommonResult<PageResult<SellingMergeListReqVo>> sellingMerge(@Valid SellingPageReqVO sellingPageReqVO) {
        SellingPageReqVO pageReqVO = new SellingPageReqVO();
        pageReqVO.setPageSize(2000);
        pageReqVO.setPageNo(1);
        PageResult<SellingMergeListReqVo> invPage = sellingExtService.sellingMerge(sellingPageReqVO);
        return CommonResult.success(invPage);
    }

    @GetMapping("/user/showGoodsWithMarketHashName")
    @Operation(summary = "按MarketHashName展示在售")
    public CommonResult<Map<String, Integer>> showGoodsWithMarketHashName(@RequestBody @Valid GoodsWithMarketHashNameReqVO reqVO) {
        Map<String, Integer> map = sellingExtService.showGoodsWithMarketName(reqVO);
        return CommonResult.success(map);
    }
}

