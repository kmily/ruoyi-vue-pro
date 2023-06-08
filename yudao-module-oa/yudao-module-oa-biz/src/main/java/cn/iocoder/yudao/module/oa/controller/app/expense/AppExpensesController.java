package cn.iocoder.yudao.module.oa.controller.app.expense;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.*;
import cn.iocoder.yudao.module.oa.convert.expenses.ExpensesConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.expenses.ExpensesDO;
import cn.iocoder.yudao.module.oa.service.expenses.ExpensesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "用户App - 报销申请")
@RestController
@RequestMapping("/oa/expenses")
@Validated
public class AppExpensesController {

    @Resource
    private ExpensesService expensesService;

    @PostMapping("/create")
    @Operation(summary = "创建报销申请")
    public CommonResult<Long> createExpenses(@Valid @RequestBody ExpensesCreateReqVO createReqVO) {
        return success(expensesService.createExpenses(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新报销申请")
    public CommonResult<Boolean> updateExpenses(@Valid @RequestBody ExpensesUpdateReqVO updateReqVO) {
        expensesService.updateExpenses(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报销申请")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteExpenses(@RequestParam("id") Long id) {
        expensesService.deleteExpenses(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报销申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ExpensesRespVO> getExpenses(@RequestParam("id") Long id) {
        ExpensesDO expenses = expensesService.getExpenses(id);
        return success(ExpensesConvert.INSTANCE.convert(expenses));
    }

    @GetMapping("/list")
    @Operation(summary = "获得报销申请列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<ExpensesRespVO>> getExpensesList(@RequestParam("ids") Collection<Long> ids) {
        List<ExpensesDO> list = expensesService.getExpensesList(ids);
        return success(ExpensesConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得报销申请分页")
    public CommonResult<PageResult<ExpensesRespVO>> getExpensesPage(@Valid ExpensesPageReqVO pageVO) {
        PageResult<ExpensesDO> pageResult = expensesService.getExpensesPage(pageVO);
        return success(ExpensesConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出报销申请 Excel")
    @OperateLog(type = EXPORT)
    public void exportExpensesExcel(@Valid ExpensesExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ExpensesDO> list = expensesService.getExpensesList(exportReqVO);
        // 导出 Excel
        List<ExpensesExcelVO> datas = ExpensesConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "报销申请.xls", "数据", ExpensesExcelVO.class, datas);
    }

}
