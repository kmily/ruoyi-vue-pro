package cn.iocoder.yudao.module.oa.controller.admin.contract;

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

import cn.iocoder.yudao.module.oa.controller.admin.contract.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.contract.ContractDO;
import cn.iocoder.yudao.module.oa.convert.contract.ContractConvert;
import cn.iocoder.yudao.module.oa.service.contract.ContractService;

@Tag(name = "管理后台 - 合同")
@RestController
@RequestMapping("/oa/contract")
@Validated
public class ContractController {

    @Resource
    private ContractService contractService;

    @PostMapping("/create")
    @Operation(summary = "创建合同")
    @PreAuthorize("@ss.hasPermission('oa:contract:create')")
    public CommonResult<Long> createContract(@Valid @RequestBody ContractCreateReqVO createReqVO) {
        return success(contractService.createContract(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新合同")
    @PreAuthorize("@ss.hasPermission('oa:contract:update')")
    public CommonResult<Boolean> updateContract(@Valid @RequestBody ContractUpdateReqVO updateReqVO) {
        contractService.updateContract(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除合同")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('oa:contract:delete')")
    public CommonResult<Boolean> deleteContract(@RequestParam("id") Long id) {
        contractService.deleteContract(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('oa:contract:query')")
    public CommonResult<ContractRespVO> getContract(@RequestParam("id") Long id) {
        ContractDO contract = contractService.getContract(id);
        return success(ContractConvert.INSTANCE.convert(contract));
    }

    @GetMapping("/list")
    @Operation(summary = "获得合同列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('oa:contract:query')")
    public CommonResult<List<ContractRespVO>> getContractList(@RequestParam("ids") Collection<Long> ids) {
        List<ContractDO> list = contractService.getContractList(ids);
        return success(ContractConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得合同分页")
    @PreAuthorize("@ss.hasPermission('oa:contract:query')")
    public CommonResult<PageResult<ContractRespVO>> getContractPage(@Valid ContractPageReqVO pageVO) {
        PageResult<ContractDO> pageResult = contractService.getContractPage(pageVO);
        return success(ContractConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出合同 Excel")
    @PreAuthorize("@ss.hasPermission('oa:contract:export')")
    @OperateLog(type = EXPORT)
    public void exportContractExcel(@Valid ContractExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ContractDO> list = contractService.getContractList(exportReqVO);
        // 导出 Excel
        List<ContractExcelVO> datas = ContractConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "合同.xls", "数据", ContractExcelVO.class, datas);
    }

}
