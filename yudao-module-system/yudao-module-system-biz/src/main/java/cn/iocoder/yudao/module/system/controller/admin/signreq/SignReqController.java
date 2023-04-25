package cn.iocoder.yudao.module.system.controller.admin.signreq;

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

import cn.iocoder.yudao.module.system.controller.admin.signreq.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.signreq.SignReqDO;
import cn.iocoder.yudao.module.system.convert.signreq.SignReqConvert;
import cn.iocoder.yudao.module.system.service.signreq.SignReqService;

@Tag(name = "管理后台 - 注册申请")
@RestController
@RequestMapping("/system/sign-req")
@Validated
public class SignReqController {

    @Resource
    private SignReqService signReqService;

    @PostMapping("/create")
    @Operation(summary = "创建注册申请")
    @PreAuthorize("@ss.hasPermission('system:sign-req:create')")
    public CommonResult<Long> createSignReq(@Valid @RequestBody SignReqCreateReqVO createReqVO) {
        return success(signReqService.createSignReq(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新注册申请")
    @PreAuthorize("@ss.hasPermission('system:sign-req:update')")
    public CommonResult<Boolean> updateSignReq(@Valid @RequestBody SignReqUpdateReqVO updateReqVO) {
        signReqService.updateSignReq(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除注册申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:sign-req:delete')")
    public CommonResult<Boolean> deleteSignReq(@RequestParam("id") Long id) {
        signReqService.deleteSignReq(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得注册申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:sign-req:query')")
    public CommonResult<SignReqRespVO> getSignReq(@RequestParam("id") Long id) {
        SignReqDO signReq = signReqService.getSignReq(id);
        return success(SignReqConvert.INSTANCE.convert(signReq));
    }

    @GetMapping("/list")
    @Operation(summary = "获得注册申请列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:sign-req:query')")
    public CommonResult<List<SignReqRespVO>> getSignReqList(@RequestParam("ids") Collection<Long> ids) {
        List<SignReqDO> list = signReqService.getSignReqList(ids);
        return success(SignReqConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得注册申请分页")
    @PreAuthorize("@ss.hasPermission('system:sign-req:query')")
    public CommonResult<PageResult<SignReqRespVO>> getSignReqPage(@Valid SignReqPageReqVO pageVO) {
        PageResult<SignReqDO> pageResult = signReqService.getSignReqPage(pageVO);
        return success(SignReqConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出注册申请 Excel")
    @PreAuthorize("@ss.hasPermission('system:sign-req:export')")
    @OperateLog(type = EXPORT)
    public void exportSignReqExcel(@Valid SignReqExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<SignReqDO> list = signReqService.getSignReqList(exportReqVO);
        // 导出 Excel
        List<SignReqExcelVO> datas = SignReqConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "注册申请.xls", "数据", SignReqExcelVO.class, datas);
    }

}
