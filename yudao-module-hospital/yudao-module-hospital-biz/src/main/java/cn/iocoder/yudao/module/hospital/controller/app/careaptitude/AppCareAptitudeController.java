package cn.iocoder.yudao.module.hospital.controller.app.careaptitude;

import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import cn.iocoder.yudao.module.hospital.service.medicalcare.MedicalCareService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

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

import cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import cn.iocoder.yudao.module.hospital.convert.careaptitude.CareAptitudeConvert;
import cn.iocoder.yudao.module.hospital.service.careaptitude.CareAptitudeService;

@Tag(name = "用户 APP - 医护资质")
@RestController
@RequestMapping("/hospital/care-aptitude")
@Validated
public class AppCareAptitudeController {

    @Resource
    private CareAptitudeService careAptitudeService;

    @Resource
    private MedicalCareService medicalCareService;

    @PostMapping("/create")
    @Operation(summary = "创建医护资质")
    @PreAuthenticated
    public CommonResult<Long> createCareAptitude(@Valid @RequestBody AppCareAptitudeCreateReqVO createReqVO) {

//        MedicalCareDO careDO = medicalCareService.getByMemberId(SecurityFrameworkUtils.getLoginUserId());
        createReqVO.setCareId(SecurityFrameworkUtils.getLoginUserId());
        return success(careAptitudeService.createCareAptitude(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新医护资质")
    @PreAuthenticated
    public CommonResult<Boolean> updateCareAptitude(@Valid @RequestBody AppCareAptitudeUpdateReqVO updateReqVO) {
//        MedicalCareDO careDO = medicalCareService.getByMemberId(SecurityFrameworkUtils.getLoginUserId());
      // updateReqVO.setCareId(SecurityFrameworkUtils.getLoginUserId());
        careAptitudeService.updateCareAptitude(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除医护资质")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteCareAptitude(@RequestParam("id") Long id) {
        careAptitudeService.deleteCareAptitude(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得医护资质")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<AppCareAptitudeRespVO> getCareAptitude(@RequestParam("id") Long id) {
        CareAptitudeDO careAptitude = careAptitudeService.getCareAptitude(id);
        return success(CareAptitudeConvert.INSTANCE.convert(careAptitude));
    }

    @GetMapping("/list")
    @Operation(summary = "获得医护资质列表")
    @PreAuthenticated
    public CommonResult<List<AppCareAptitudeRespVO>> getCareAptitudeList() {
        //MedicalCareDO careDO = medicalCareService.getByMemberId(SecurityFrameworkUtils.getLoginUserId());
        List<CareAptitudeDO> list = careAptitudeService.getCareAptitudeList(SecurityFrameworkUtils.getLoginUserId());
        return success(CareAptitudeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得医护资质分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppCareAptitudeRespVO>> getCareAptitudePage(@Valid AppCareAptitudePageReqVO pageVO) {
       // MedicalCareDO careDO = medicalCareService.getByMemberId(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setCareId(SecurityFrameworkUtils.getLoginUserId());
        PageResult<CareAptitudeDO> pageResult = careAptitudeService.getCareAptitudePage(pageVO);
        return success(CareAptitudeConvert.INSTANCE.convertPage(pageResult));
    }
}
