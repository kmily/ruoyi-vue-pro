package cn.iocoder.yudao.module.steam.controller.admin.otherselling;

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

import cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import cn.iocoder.yudao.module.steam.service.otherselling.OtherSellingService;

@Tag(name = "管理后台 - 其他平台在售")
@RestController
@RequestMapping("/steam/other-selling")
@Validated
public class OtherSellingController {

    @Resource
    private OtherSellingService otherSellingService;

    @PostMapping("/create")
    @Operation(summary = "创建其他平台在售")
    @PreAuthorize("@ss.hasPermission('steam:other-selling:create')")
    public CommonResult<Integer> createOtherSelling(@Valid @RequestBody OtherSellingSaveReqVO createReqVO) {
        return success(otherSellingService.createOtherSelling(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新其他平台在售")
    @PreAuthorize("@ss.hasPermission('steam:other-selling:update')")
    public CommonResult<Boolean> updateOtherSelling(@Valid @RequestBody OtherSellingSaveReqVO updateReqVO) {
        otherSellingService.updateOtherSelling(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除其他平台在售")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:other-selling:delete')")
    public CommonResult<Boolean> deleteOtherSelling(@RequestParam("id") Integer id) {
        otherSellingService.deleteOtherSelling(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得其他平台在售")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:other-selling:query')")
    public CommonResult<OtherSellingRespVO> getOtherSelling(@RequestParam("id") Integer id) {
        OtherSellingDO otherSelling = otherSellingService.getOtherSelling(id);
        return success(BeanUtils.toBean(otherSelling, OtherSellingRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得其他平台在售分页")
    @PreAuthorize("@ss.hasPermission('steam:other-selling:query')")
    public CommonResult<PageResult<OtherSellingRespVO>> getOtherSellingPage(@Valid OtherSellingPageReqVO pageReqVO) {
        PageResult<OtherSellingDO> pageResult = otherSellingService.getOtherSellingPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OtherSellingRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出其他平台在售 Excel")
    @PreAuthorize("@ss.hasPermission('steam:other-selling:export')")
    @OperateLog(type = EXPORT)
    public void exportOtherSellingExcel(@Valid OtherSellingPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OtherSellingDO> list = otherSellingService.getOtherSellingPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "其他平台在售.xls", "数据", OtherSellingRespVO.class,
                        BeanUtils.toBean(list, OtherSellingRespVO.class));
    }

}