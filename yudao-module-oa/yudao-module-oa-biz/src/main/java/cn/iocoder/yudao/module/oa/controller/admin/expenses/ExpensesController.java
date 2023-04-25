package cn.iocoder.yudao.module.oa.controller.admin.expenses;

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

import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expenses.ExpensesDO;
import cn.iocoder.yudao.module.oa.convert.expenses.ExpensesConvert;
import cn.iocoder.yudao.module.oa.service.expenses.ExpensesService;

@Tag(name = "管理后台 - 报销申请")
@RestController
@RequestMapping("/oa/expenses")
@Validated
public class ExpensesController {

    @Resource
    private ExpensesService expensesService;

    @PostMapping("/create")
    @Operation(summary = "创建报销申请")
    @PreAuthorize("@ss.hasPermission('oa:expenses:create')")
    public CommonResult<Long> createExpenses(@Valid @RequestBody ExpensesCreateReqVO createReqVO) {
        return success(expensesService.createExpenses(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新报销申请")
    @PreAuthorize("@ss.hasPermission('oa:expenses:update')")
    public CommonResult<Boolean> updateExpenses(@Valid @RequestBody ExpensesUpdateReqVO updateReqVO) {
        expensesService.updateExpenses(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报销申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('oa:expenses:delete')")
    public CommonResult<Boolean> deleteExpenses(@RequestParam("id") Long id) {
        expensesService.deleteExpenses(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报销申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('oa:expenses:query')")
    public CommonResult<ExpensesRespVO> getExpenses(@RequestParam("id") Long id) {
        ExpensesDO expenses = expensesService.getExpenses(id);
        return success(ExpensesConvert.INSTANCE.convert(expenses));
    }

    @GetMapping("/list")
    @Operation(summary = "获得报销申请列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('oa:expenses:query')")
    public CommonResult<List<ExpensesRespVO>> getExpensesList(@RequestParam("ids") Collection<Long> ids) {
        List<ExpensesDO> list = expensesService.getExpensesList(ids);
        return success(ExpensesConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得报销申请分页")
    @PreAuthorize("@ss.hasPermission('oa:expenses:query')")
    public CommonResult<PageResult<ExpensesRespVO>> getExpensesPage(@Valid ExpensesPageReqVO pageVO) {
        PageResult<ExpensesDO> pageResult = expensesService.getExpensesPage(pageVO);
        return success(ExpensesConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出报销申请 Excel")
    @PreAuthorize("@ss.hasPermission('oa:expenses:export')")
    @OperateLog(type = EXPORT)
    public void exportExpensesExcel(@Valid ExpensesExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ExpensesDO> list = expensesService.getExpensesList(exportReqVO);
        // 导出 Excel
        List<ExpensesExcelVO> datas = ExpensesConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "报销申请.xls", "数据", ExpensesExcelVO.class, datas);
    }

}
