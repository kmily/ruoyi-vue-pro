package cn.iocoder.yudao.module.steam.controller.admin.youyougoodslist;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
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

import cn.iocoder.yudao.module.steam.controller.admin.youyougoodslist.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyougoodslist.YouyouGoodslistDO;
import cn.iocoder.yudao.module.steam.service.youyougoodslist.YouyouGoodslistService;

@Tag(name = "管理后台 - 查询商品列")
@RestController
@RequestMapping("/steam/youyou-goodslist")
@Validated
public class YouyouGoodslistController {

    @Resource
    private YouyouGoodslistService youyouGoodslistService;

    @PostMapping("/create")
    @Operation(summary = "创建查询商品列")
    @PreAuthorize("@ss.hasPermission('steam:youyou-goodslist:create')")
    public CommonResult<Integer> createYouyouGoodslist(@Valid @RequestBody YouyouGoodslistSaveReqVO createReqVO) {
        return success(youyouGoodslistService.createYouyouGoodslist(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新查询商品列")
    @PreAuthorize("@ss.hasPermission('steam:youyou-goodslist:update')")
    public CommonResult<Boolean> updateYouyouGoodslist(@Valid @RequestBody YouyouGoodslistSaveReqVO updateReqVO) {
        youyouGoodslistService.updateYouyouGoodslist(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除查询商品列")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:youyou-goodslist:delete')")
    public CommonResult<Boolean> deleteYouyouGoodslist(@RequestParam("id") Integer id) {
        youyouGoodslistService.deleteYouyouGoodslist(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得查询商品列")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:youyou-goodslist:query')")
    public CommonResult<YouyouGoodslistRespVO> getYouyouGoodslist(@RequestParam("id") Integer id) {
        YouyouGoodslistDO youyouGoodslist = youyouGoodslistService.getYouyouGoodslist(id);
        return success(BeanUtils.toBean(youyouGoodslist, YouyouGoodslistRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得查询商品列分页")
    @PreAuthorize("@ss.hasPermission('steam:youyou-goodslist:query')")
    public CommonResult<PageResult<YouyouGoodslistRespVO>> getYouyouGoodslistPage(@Valid YouyouGoodslistPageReqVO pageReqVO) {
        PageResult<YouyouGoodslistDO> pageResult = youyouGoodslistService.getYouyouGoodslistPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, YouyouGoodslistRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出查询商品列 Excel")
    @PreAuthorize("@ss.hasPermission('steam:youyou-goodslist:export')")
    @OperateLog(type = EXPORT)
    public void exportYouyouGoodslistExcel(@Valid YouyouGoodslistPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<YouyouGoodslistDO> list = youyouGoodslistService.getYouyouGoodslistPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "查询商品列.xls", "数据", YouyouGoodslistRespVO.class,
                        BeanUtils.toBean(list, YouyouGoodslistRespVO.class));
    }

}