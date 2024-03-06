package cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.service.youyoucommodity.YouyouCommodityService;

@Tag(name = "管理后台 - 悠悠商品列表")
@RestController
@RequestMapping("/steam/youyou-commodity")
@Validated
public class YouyouCommodityController {

    @Resource
    private YouyouCommodityService youyouCommodityService;

    @PostMapping("/create")
    @Operation(summary = "创建悠悠商品列表")
    @PreAuthorize("@ss.hasPermission('steam:youyou-commodity:create')")
    public CommonResult<Integer> createYouyouCommodity(@Valid @RequestBody YouyouCommoditySaveReqVO createReqVO) {
        return success(youyouCommodityService.createYouyouCommodity(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新悠悠商品列表")
    @PreAuthorize("@ss.hasPermission('steam:youyou-commodity:update')")
    public CommonResult<Boolean> updateYouyouCommodity(@Valid @RequestBody YouyouCommoditySaveReqVO updateReqVO) {
        youyouCommodityService.updateYouyouCommodity(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除悠悠商品列表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:youyou-commodity:delete')")
    public CommonResult<Boolean> deleteYouyouCommodity(@RequestParam("id") Integer id) {
        youyouCommodityService.deleteYouyouCommodity(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得悠悠商品列表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:youyou-commodity:query')")
    public CommonResult<UUCommodityRespVO> getYouyouCommodity(@RequestParam("id") Integer id) {
        YouyouCommodityDO youyouCommodity = youyouCommodityService.getYouyouCommodity(id);
        return success(BeanUtils.toBean(youyouCommodity, UUCommodityRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得悠悠商品列表分页")
    @PreAuthorize("@ss.hasPermission('steam:youyou-commodity:query')")
    public CommonResult<PageResult<UUCommodityRespVO>> getYouyouCommodityPage(@Valid YouyouCommodityPageReqVO pageReqVO) {
        PageResult<YouyouCommodityDO> pageResult = youyouCommodityService.getYouyouCommodityPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UUCommodityRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出悠悠商品列表 Excel")
    @PreAuthorize("@ss.hasPermission('steam:youyou-commodity:export')")
    @OperateLog(type = EXPORT)
    public void exportYouyouCommodityExcel(@Valid YouyouCommodityPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<YouyouCommodityDO> list = youyouCommodityService.getYouyouCommodityPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "悠悠商品列表.xls", "数据", UUCommodityRespVO.class,
                        BeanUtils.toBean(list, UUCommodityRespVO.class));
    }

}