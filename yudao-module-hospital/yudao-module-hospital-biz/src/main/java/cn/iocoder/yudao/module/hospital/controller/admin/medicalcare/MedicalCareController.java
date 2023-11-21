package cn.iocoder.yudao.module.hospital.controller.admin.medicalcare;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.hospital.enums.medicalcare.MedicalCareStatusEnum;
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

import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import cn.iocoder.yudao.module.hospital.convert.medicalcare.MedicalCareConvert;
import cn.iocoder.yudao.module.hospital.service.medicalcare.MedicalCareService;

@Tag(name = "管理后台 - 医护管理")
@RestController
@RequestMapping("/hospital/medical-care")
@Validated
public class MedicalCareController {

    @Resource
    private MedicalCareService medicalCareService;

    @PostMapping("/create")
    @Operation(summary = "创建医护管理")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:create')")
    public CommonResult<Long> createMedicalCare(@Valid @RequestBody MedicalCareCreateReqVO createReqVO) {
        return success(medicalCareService.createMedicalCare(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新医护管理")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:update')")
    public CommonResult<Boolean> updateMedicalCare(@Valid @RequestBody MedicalCareUpdateReqVO updateReqVO) {
        medicalCareService.updateMedicalCare(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除医护管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:delete')")
    public CommonResult<Boolean> deleteMedicalCare(@RequestParam("id") Long id) {
        medicalCareService.deleteMedicalCare(id);
        return success(true);
    }

    @PutMapping("/close")
    @Operation(summary = "禁用医护")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:close')")
    public CommonResult<Boolean> closeMedicalCare(@RequestParam("id") Long id) {
        medicalCareService.closeOrOpenMedicalCare(id, MedicalCareStatusEnum.CLOSED);
        return success(true);
    }

    @PutMapping("/open")
    @Operation(summary = "启用医护")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:open')")
    public CommonResult<Boolean> openMedicalCare(@RequestParam("id") Long id) {
        medicalCareService.closeOrOpenMedicalCare(id, MedicalCareStatusEnum.OPEN);
        return success(true);
    }
    @PutMapping("/audit")
    @Operation(summary = "审核组织机构")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:audit')")
    public CommonResult<Boolean> auditMedicalCare(@RequestBody MedicalCareAuditVO auditVO) {
        medicalCareService.auditMedicalCare(auditVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得医护管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:query')")
    public CommonResult<MedicalCareRespVO> getMedicalCare(@RequestParam("id") Long id) {
        MedicalCareDO medicalCare = medicalCareService.getMedicalCare(id);
        MedicalCareRespVO convert = MedicalCareConvert.INSTANCE.convert(medicalCare);
        if(StrUtil.isNotBlank(medicalCare.getIdCard())){
            convert.setAge(IdcardUtil.getAgeByIdCard(medicalCare.getIdCard()));
        }
        //convert.setMobile(PhoneUtil.hideBetween(convert.getMobile()).toString());
        //convert.setIdCard(IdcardUtil.hide(convert.getIdCard(), 6, 14));
        return success(convert);
    }


    @GetMapping("/{mobile}/mobile")
    @Operation(summary = "根据手机号查询医护")
    @Parameter(name = "mobile", description = "手机号", required = true)
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:query')")
    public CommonResult<MedicalCareRespVO> getMedicalCareByMobile(@PathVariable("mobile") String mobile) {
        MedicalCareDO medicalCare = medicalCareService.selectOneByMobile(mobile);
        return success(MedicalCareConvert.INSTANCE.convert(medicalCare));
    }



    @GetMapping("/list")
    @Operation(summary = "获得医护管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:query')")
    public CommonResult<List<MedicalCareRespVO>> getMedicalCareList(@RequestParam("ids") Collection<Long> ids) {
        List<MedicalCareDO> list = medicalCareService.getMedicalCareList(ids);
        return success(MedicalCareConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得医护管理分页")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:query')")
    public CommonResult<PageResult<MedicalCareRespVO>> getMedicalCarePage(@Valid MedicalCarePageReqVO pageVO) {
        pageVO.setOrgId(SecurityFrameworkUtils.getLoginOrgId());
        PageResult<MedicalCareDO> pageResult = medicalCareService.getMedicalCarePage(pageVO);
//        List<MedicalCareDO> list = pageResult.getList();
//        list.forEach(medicalCareDO -> {
//            medicalCareDO.setMobile(PhoneUtil.hideBetween(medicalCareDO.getMobile()).toString());
//        });
//        pageResult.setList(list);
        return success(MedicalCareConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出医护管理 Excel")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:export')")
    @OperateLog(type = EXPORT)
    public void exportMedicalCareExcel(@Valid MedicalCareExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        exportReqVO.setOrgId(SecurityFrameworkUtils.getLoginOrgId());
        List<MedicalCareDO> list = medicalCareService.getMedicalCareList(exportReqVO);
        // 导出 Excel
        List<MedicalCareExcelVO> datas = MedicalCareConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "医护管理.xls", "数据", MedicalCareExcelVO.class, datas);
    }

}
