package cn.iocoder.yudao.module.oa.controller.admin.expensesdetail;

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

import cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expensesdetail.ExpensesDetailDO;
import cn.iocoder.yudao.module.oa.convert.expensesdetail.ExpensesDetailConvert;
import cn.iocoder.yudao.module.oa.service.expensesdetail.ExpensesDetailService;

@Tag(name = "管理后台 - 报销明细")
@RestController
@RequestMapping("/oa/expenses-detail")
@Validated
public class ExpensesDetailController {

    @Resource
    private ExpensesDetailService expensesDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建报销明细")
    @PreAuthorize("@ss.hasPermission('oa:expenses-detail:create')")
    public CommonResult<Long> createExpensesDetail(@Valid @RequestBody ExpensesDetailCreateReqVO createReqVO) {
        return success(expensesDetailService.createExpensesDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新报销明细")
    @PreAuthorize("@ss.hasPermission('oa:expenses-detail:update')")
    public CommonResult<Boolean> updateExpensesDetail(@Valid @RequestBody ExpensesDetailUpdateReqVO updateReqVO) {
        expensesDetailService.updateExpensesDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报销明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('oa:expenses-detail:delete')")
    public CommonResult<Boolean> deleteExpensesDetail(@RequestParam("id") Long id) {
        expensesDetailService.deleteExpensesDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报销明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('oa:expenses-detail:query')")
    public CommonResult<ExpensesDetailRespVO> getExpensesDetail(@RequestParam("id") Long id) {
        ExpensesDetailDO expensesDetail = expensesDetailService.getExpensesDetail(id);
        return success(ExpensesDetailConvert.INSTANCE.convert(expensesDetail));
    }

    @GetMapping("/list")
    @Operation(summary = "获得报销明细列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('oa:expenses-detail:query')")
    public CommonResult<List<ExpensesDetailRespVO>> getExpensesDetailList(@RequestParam("ids") Collection<Long> ids) {
        List<ExpensesDetailDO> list = expensesDetailService.getExpensesDetailList(ids);
        return success(ExpensesDetailConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得报销明细分页")
    @PreAuthorize("@ss.hasPermission('oa:expenses-detail:query')")
    public CommonResult<PageResult<ExpensesDetailRespVO>> getExpensesDetailPage(@Valid ExpensesDetailPageReqVO pageVO) {
        PageResult<ExpensesDetailDO> pageResult = expensesDetailService.getExpensesDetailPage(pageVO);
        return success(ExpensesDetailConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出报销明细 Excel")
    @PreAuthorize("@ss.hasPermission('oa:expenses-detail:export')")
    @OperateLog(type = EXPORT)
    public void exportExpensesDetailExcel(@Valid ExpensesDetailExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ExpensesDetailDO> list = expensesDetailService.getExpensesDetailList(exportReqVO);
        // 导出 Excel
        List<ExpensesDetailExcelVO> datas = ExpensesDetailConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "报销明细.xls", "数据", ExpensesDetailExcelVO.class, datas);
    }

}
