package cn.iocoder.yudao.module.oa.controller.app.expensedetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo.*;
import cn.iocoder.yudao.module.oa.convert.expensesdetail.ExpensesDetailConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.expensesdetail.ExpensesDetailDO;
import cn.iocoder.yudao.module.oa.service.expensesdetail.ExpensesDetailService;
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

@Tag(name = "用户App - 报销明细")
@RestController
@RequestMapping("/oa/expenses-detail")
@Validated
public class AppExpensesDetailController {

    @Resource
    private ExpensesDetailService expensesDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建报销明细")
    @PreAuthenticated
    public CommonResult<Long> createExpensesDetail(@Valid @RequestBody ExpensesDetailCreateReqVO createReqVO) {
        return success(expensesDetailService.createExpensesDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新报销明细")
    @PreAuthenticated
    public CommonResult<Boolean> updateExpensesDetail(@Valid @RequestBody ExpensesDetailUpdateReqVO updateReqVO) {
        expensesDetailService.updateExpensesDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报销明细")
    @PreAuthenticated
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteExpensesDetail(@RequestParam("id") Long id) {
        expensesDetailService.deleteExpensesDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报销明细")
    @PreAuthenticated
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ExpensesDetailRespVO> getExpensesDetail(@RequestParam("id") Long id) {
        ExpensesDetailDO expensesDetail = expensesDetailService.getExpensesDetail(id);
        return success(ExpensesDetailConvert.INSTANCE.convert(expensesDetail));
    }

    @GetMapping("/list")
    @Operation(summary = "获得报销明细列表")
    @PreAuthenticated
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<ExpensesDetailRespVO>> getExpensesDetailList(@RequestParam("ids") Collection<Long> ids) {
        List<ExpensesDetailDO> list = expensesDetailService.getExpensesDetailList(ids);
        return success(ExpensesDetailConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得报销明细分页")
    @PreAuthenticated
    public CommonResult<PageResult<ExpensesDetailRespVO>> getExpensesDetailPage(@Valid ExpensesDetailPageReqVO pageVO) {
        PageResult<ExpensesDetailDO> pageResult = expensesDetailService.getExpensesDetailPage(pageVO);
        return success(ExpensesDetailConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出报销明细 Excel")
    @OperateLog(type = EXPORT)
    @PreAuthenticated
    public void exportExpensesDetailExcel(@Valid ExpensesDetailExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ExpensesDetailDO> list = expensesDetailService.getExpensesDetailList(exportReqVO);
        // 导出 Excel
        List<ExpensesDetailExcelVO> datas = ExpensesDetailConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "报销明细.xls", "数据", ExpensesDetailExcelVO.class, datas);
    }

}
