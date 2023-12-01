package cn.iocoder.yudao.module.system.controller.admin.helpcenter;

import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.system.dal.dataobject.helpcenter.HelpCenterDO;
import cn.iocoder.yudao.module.system.convert.helpcenter.HelpCenterConvert;
import cn.iocoder.yudao.module.system.service.helpcenter.HelpCenterService;

@Tag(name = "管理后台 - 常见问题")
@RestController
@RequestMapping("/system/help-center")
@Validated
public class HelpCenterController {

    @Resource
    private HelpCenterService helpCenterService;

    @PostMapping("/create")
    @Operation(summary = "创建常见问题")
    @PreAuthorize("@ss.hasPermission('system:help-center:create')")
    public CommonResult<Long> createHelpCenter(@Valid @RequestBody HelpCenterCreateReqVO createReqVO) {
        return success(helpCenterService.createHelpCenter(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新常见问题")
    @PreAuthorize("@ss.hasPermission('system:help-center:update')")
    public CommonResult<Boolean> updateHelpCenter(@Valid @RequestBody HelpCenterUpdateReqVO updateReqVO) {
        helpCenterService.updateHelpCenter(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除常见问题")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:help-center:delete')")
    public CommonResult<Boolean> deleteHelpCenter(@RequestParam("id") Long id) {
        helpCenterService.deleteHelpCenter(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得常见问题")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:help-center:query')")
    public CommonResult<HelpCenterRespVO> getHelpCenter(@RequestParam("id") Long id) {
        HelpCenterDO helpCenter = helpCenterService.getHelpCenter(id);
        return success(HelpCenterConvert.INSTANCE.convert(helpCenter));
    }

    @GetMapping("/list")
    @Operation(summary = "获得常见问题列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:help-center:query')")
    public CommonResult<List<HelpCenterRespVO>> getHelpCenterList(@RequestParam("ids") Collection<Long> ids) {
        List<HelpCenterDO> list = helpCenterService.getHelpCenterList(ids);
        return success(HelpCenterConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得常见问题分页")
    @PreAuthorize("@ss.hasPermission('system:help-center:query')")
    public CommonResult<PageResult<HelpCenterRespVO>> getHelpCenterPage(@Valid HelpCenterPageReqVO pageVO) {
        PageResult<HelpCenterDO> pageResult = helpCenterService.getHelpCenterPage(pageVO);
        return success(HelpCenterConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出常见问题 Excel")
    @PreAuthorize("@ss.hasPermission('system:help-center:export')")
    @OperateLog(type = EXPORT)
    public void exportHelpCenterExcel(@Valid HelpCenterExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<HelpCenterDO> list = helpCenterService.getHelpCenterList(exportReqVO);
        // 导出 Excel
        List<HelpCenterExcelVO> datas = HelpCenterConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "常见问题.xls", "数据", HelpCenterExcelVO.class, datas);
    }

    @PutMapping("/close")
    @Operation(summary = "禁用常见问题")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:help-center:close')")
    public CommonResult<Boolean> closeMedicalCare(@RequestParam("id") Long id) {
        HelpCenterUpdateReqVO vo = new  HelpCenterUpdateReqVO();
        vo.setId(id);
        vo.setStatus((byte)1);
        vo.setType((byte)0);
        helpCenterService.updateHelpCenter(vo);
        return success(true);
    }

    @PutMapping("/open")
    @Operation(summary = "开启常见问题")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> openMedicalCare(@RequestParam("id") Long id) {
        HelpCenterUpdateReqVO vo = new  HelpCenterUpdateReqVO();
        vo.setId(id);
        vo.setStatus((byte)0);
        vo.setType((byte)0);
        helpCenterService.updateHelpCenter(vo);
        return success(true);
    }

}
