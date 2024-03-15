package cn.iocoder.yudao.module.steam.controller.app.selling;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.idempotent.core.annotation.Idempotent;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingRespVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.InvPageReqVo;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.service.selling.SellingExtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    public CommonResult<SellingDO> getOnSale(@Valid InvPageReqVo invPageReqVo) {
        // 入参：id  price
        // 查询 steam_inv
        SellingDO invPage = sellingExtService.getToSale(invPageReqVo);
        return CommonResult.success(invPage);

    }
    @PostMapping("/batchSale")
    @Operation(summary = "批量上架")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    @PreAuthenticated
    public CommonResult<String> batchSale(@RequestBody @Valid BatchSellReqVo reqVo) {
        // 入参：id  price
        // 查询 steam_inv
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        sellingExtService.batchSale(reqVo,loginUser);
        return CommonResult.success("上架成功");
    }
    @GetMapping("/offsale")
    @Operation(summary = "饰品下架")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    public CommonResult<Optional<SellingDO>> getOffSale(@Valid SellingReqVo sellingReqVo) {

        Optional<SellingDO> invPage = sellingExtService.getOffSale(sellingReqVo);
        return CommonResult.success(invPage);

    }
    @PostMapping("/batchOffSale")
    @Operation(summary = "批量下架")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    @PreAuthenticated
    public CommonResult<String> batchOffSale(@RequestBody @Valid BatchOffSellReqVo reqVo) {
        // 入参：id  price
        // 查询 steam_inv
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        sellingExtService.batchOffSale(reqVo,loginUser);
        return CommonResult.success("下架成功");
    }
    @GetMapping("/changePrice")
    @Operation(summary = "饰品改价")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    public CommonResult<Integer> changePrice(@RequestBody @Valid SellingChangePriceReqVo reqVo) {

        Integer integer = sellingExtService.changePrice(reqVo);
        return CommonResult.success(integer);
    }

    @PostMapping("/batchChangePrice")
    @Operation(summary = "批量改价")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "操作太快，请稍后再试")
    @PreAuthenticated
    public CommonResult<String> batchChangePrice(@RequestBody @Valid BatchChangePriceReqVo reqVo) {
        // 入参：id  price
        // 查询 steam_inv
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        sellingExtService.batchChangePrice(reqVo,loginUser);
        return CommonResult.success("改价成功");
    }

    @GetMapping("/user/sellingUnMerge")
    @Operation(summary = "出售未合并")
    public CommonResult<PageResult<SellingRespVO>> sellingUnMerge(@Valid SellingPageReqVO sellingPageReqVO) {
        SellingPageReqVO pageReqVO = new SellingPageReqVO();

        PageResult<SellingRespVO> sellingDOS = sellingExtService.sellingUnMerge(sellingPageReqVO);
        pageReqVO.setPageSize(20);
        pageReqVO.setPageNo(1);

        return CommonResult.success(sellingDOS);

    }
    @GetMapping("/user/sellingMerge")
    @Operation(summary = "出售合并")
    public CommonResult<PageResult<SellingMergeListVO>> sellingMerge(@Valid SellingPageReqVO sellingPageReqVO) {
        SellingPageReqVO pageReqVO = new SellingPageReqVO();
        pageReqVO.setPageSize(20);
        pageReqVO.setPageNo(1);
        PageResult<SellingMergeListVO> invPage = sellingExtService.sellingMerge(sellingPageReqVO);
        return CommonResult.success(invPage);
    }
}

