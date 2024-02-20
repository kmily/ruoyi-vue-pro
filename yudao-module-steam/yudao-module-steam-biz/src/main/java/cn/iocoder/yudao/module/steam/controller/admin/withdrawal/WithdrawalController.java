package cn.iocoder.yudao.module.steam.controller.admin.withdrawal;

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

import cn.iocoder.yudao.module.steam.controller.admin.withdrawal.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal.WithdrawalDO;
import cn.iocoder.yudao.module.steam.service.withdrawal.WithdrawalService;

@Tag(name = "管理后台 - 提现")
@RestController
@RequestMapping("/steam/withdrawal")
@Validated
public class WithdrawalController {

    @Resource
    private WithdrawalService withdrawalService;

    @PostMapping("/create")
    @Operation(summary = "创建提现")
    @PreAuthorize("@ss.hasPermission('steam:withdrawal:create')")
    public CommonResult<Long> createWithdrawal(@Valid @RequestBody WithdrawalSaveReqVO createReqVO) {
        return success(withdrawalService.createWithdrawal(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新提现")
    @PreAuthorize("@ss.hasPermission('steam:withdrawal:update')")
    public CommonResult<Boolean> updateWithdrawal(@Valid @RequestBody WithdrawalSaveReqVO updateReqVO) {
        withdrawalService.updateWithdrawal(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除提现")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:withdrawal:delete')")
    public CommonResult<Boolean> deleteWithdrawal(@RequestParam("id") Long id) {
        withdrawalService.deleteWithdrawal(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得提现")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:withdrawal:query')")
    public CommonResult<WithdrawalRespVO> getWithdrawal(@RequestParam("id") Long id) {
        WithdrawalDO withdrawal = withdrawalService.getWithdrawal(id);
        return success(BeanUtils.toBean(withdrawal, WithdrawalRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得提现分页")
    @PreAuthorize("@ss.hasPermission('steam:withdrawal:query')")
    public CommonResult<PageResult<WithdrawalRespVO>> getWithdrawalPage(@Valid WithdrawalPageReqVO pageReqVO) {
        PageResult<WithdrawalDO> pageResult = withdrawalService.getWithdrawalPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WithdrawalRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出提现 Excel")
    @PreAuthorize("@ss.hasPermission('steam:withdrawal:export')")
    @OperateLog(type = EXPORT)
    public void exportWithdrawalExcel(@Valid WithdrawalPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WithdrawalDO> list = withdrawalService.getWithdrawalPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "提现.xls", "数据", WithdrawalRespVO.class,
                        BeanUtils.toBean(list, WithdrawalRespVO.class));
    }

}