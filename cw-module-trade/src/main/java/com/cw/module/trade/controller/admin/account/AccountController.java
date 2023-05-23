package com.cw.module.trade.controller.admin.account;

import org.springframework.web.bind.annotation.*;

import com.cw.module.trade.controller.admin.account.vo.*;
import com.cw.module.trade.convert.account.AccountConvert;
import com.cw.module.trade.dal.dataobject.account.AccountDO;
import com.cw.module.trade.service.account.AccountService;

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

@Tag(name = "管理后台 - 交易账号")
@RestController
@RequestMapping("/trade/account")
@Validated
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping("/create")
    @Operation(summary = "创建交易账号")
    @PreAuthorize("@ss.hasPermission('trade:account:create')")
    public CommonResult<Long> createAccount(@Valid @RequestBody AccountCreateReqVO createReqVO) {
        return success(accountService.createAccount(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新交易账号")
    @PreAuthorize("@ss.hasPermission('trade:account:update')")
    public CommonResult<Boolean> updateAccount(@Valid @RequestBody AccountUpdateReqVO updateReqVO) {
        accountService.updateAccount(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除交易账号")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('trade:account:delete')")
    public CommonResult<Boolean> deleteAccount(@RequestParam("id") Long id) {
        accountService.deleteAccount(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得交易账号")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('trade:account:query')")
    public CommonResult<AccountRespVO> getAccount(@RequestParam("id") Long id) {
        AccountDO account = accountService.getAccount(id);
        return success(AccountConvert.INSTANCE.convert(account));
    }

    @GetMapping("/list")
    @Operation(summary = "获得交易账号列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('trade:account:query')")
    public CommonResult<List<AccountRespVO>> getAccountList(@RequestParam("ids") Collection<Long> ids) {
        List<AccountDO> list = accountService.getAccountList(ids);
        return success(AccountConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得交易账号分页")
    @PreAuthorize("@ss.hasPermission('trade:account:query')")
    public CommonResult<PageResult<AccountRespVO>> getAccountPage(@Valid AccountPageReqVO pageVO) {
        PageResult<AccountDO> pageResult = accountService.getAccountPage(pageVO);
        return success(AccountConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出交易账号 Excel")
    @PreAuthorize("@ss.hasPermission('trade:account:export')")
    @OperateLog(type = EXPORT)
    public void exportAccountExcel(@Valid AccountExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<AccountDO> list = accountService.getAccountList(exportReqVO);
        // 导出 Excel
        List<AccountExcelVO> datas = AccountConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "交易账号.xls", "数据", AccountExcelVO.class, datas);
    }

    
    @GetMapping("/sync/balance")
    @Operation(summary = "同步账户余额")
    public CommonResult<String> syncBalance(@RequestParam("id") Long id) {
        return success(accountService.syncBalance(id));
    }
    
}
