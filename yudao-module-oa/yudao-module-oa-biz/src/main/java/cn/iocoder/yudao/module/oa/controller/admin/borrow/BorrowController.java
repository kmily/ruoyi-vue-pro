package cn.iocoder.yudao.module.oa.controller.admin.borrow;

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

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.borrow.BorrowDO;
import cn.iocoder.yudao.module.oa.convert.borrow.BorrowConvert;
import cn.iocoder.yudao.module.oa.service.borrow.BorrowService;

@Tag(name = "管理后台 - 借支申请")
@RestController
@RequestMapping("/oa/borrow")
@Validated
public class BorrowController {

    @Resource
    private BorrowService borrowService;

    @PostMapping("/create")
    @Operation(summary = "创建借支申请")
    @PreAuthorize("@ss.hasPermission('oa:borrow:create')")
    public CommonResult<Long> createBorrow(@Valid @RequestBody BorrowCreateReqVO createReqVO) {
        return success(borrowService.createBorrow(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新借支申请")
    @PreAuthorize("@ss.hasPermission('oa:borrow:update')")
    public CommonResult<Boolean> updateBorrow(@Valid @RequestBody BorrowUpdateReqVO updateReqVO) {
        borrowService.updateBorrow(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除借支申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('oa:borrow:delete')")
    public CommonResult<Boolean> deleteBorrow(@RequestParam("id") Long id) {
        borrowService.deleteBorrow(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得借支申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('oa:borrow:query')")
    public CommonResult<BorrowRespVO> getBorrow(@RequestParam("id") Long id) {
        BorrowDO borrow = borrowService.getBorrow(id);
        return success(BorrowConvert.INSTANCE.convert(borrow));
    }

    @GetMapping("/list")
    @Operation(summary = "获得借支申请列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('oa:borrow:query')")
    public CommonResult<List<BorrowRespVO>> getBorrowList(@RequestParam("ids") Collection<Long> ids) {
        List<BorrowDO> list = borrowService.getBorrowList(ids);
        return success(BorrowConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得借支申请分页")
    @PreAuthorize("@ss.hasPermission('oa:borrow:query')")
    public CommonResult<PageResult<BorrowRespVO>> getBorrowPage(@Valid BorrowPageReqVO pageVO) {
        PageResult<BorrowDO> pageResult = borrowService.getBorrowPage(pageVO);
        return success(BorrowConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出借支申请 Excel")
    @PreAuthorize("@ss.hasPermission('oa:borrow:export')")
    @OperateLog(type = EXPORT)
    public void exportBorrowExcel(@Valid BorrowExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<BorrowDO> list = borrowService.getBorrowList(exportReqVO);
        // 导出 Excel
        List<BorrowExcelVO> datas = BorrowConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "借支申请.xls", "数据", BorrowExcelVO.class, datas);
    }

}
