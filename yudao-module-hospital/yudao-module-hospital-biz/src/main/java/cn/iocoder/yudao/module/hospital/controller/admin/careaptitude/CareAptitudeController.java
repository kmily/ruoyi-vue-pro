package cn.iocoder.yudao.module.hospital.controller.admin.careaptitude;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo.CareAptitudeAuditReqVO;
import cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo.AppCareAptitudeRespVO;
import cn.iocoder.yudao.module.hospital.convert.careaptitude.CareAptitudeConvert;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import cn.iocoder.yudao.module.hospital.service.careaptitude.CareAptitudeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 医护资质")
@RestController
@RequestMapping("/hospital/care-aptitude")
@Validated
public class CareAptitudeController {

    @Resource
    private CareAptitudeService careAptitudeService;


    @PutMapping("/audit")
    @Operation(summary = "审核组织机构")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:audit')")
    public CommonResult<Boolean> auditMedicalCare(@RequestBody CareAptitudeAuditReqVO auditVO) {
        careAptitudeService.auditCareAptitude(auditVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得医护资质")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:query')")
    public CommonResult<AppCareAptitudeRespVO> getCareAptitude(@RequestParam("id") Long id) {
        CareAptitudeDO careAptitude = careAptitudeService.getCareAptitude(id);
        return success(CareAptitudeConvert.INSTANCE.convert(careAptitude));
    }

    @GetMapping("/list")
    @Operation(summary = "获得医护资质列表")
    @Parameter(name = "careId", description = "医护编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('hospital:medical-care:query')")
    public CommonResult<List<AppCareAptitudeRespVO>> getCareAptitudeList(@RequestParam("careId") Long careId) {
        List<CareAptitudeDO> list = careAptitudeService.getCareAptitudeList(careId);
        return success(CareAptitudeConvert.INSTANCE.convertList(list));
    }
}
