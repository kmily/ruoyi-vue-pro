package cn.iocoder.yudao.module.hospital.controller.app.medicalcare;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.MedicalCareRespVO;
import cn.iocoder.yudao.module.hospital.convert.medicalcare.MedicalCareConvert;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import cn.iocoder.yudao.module.hospital.service.medicalcare.MedicalCareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @author whycode
 * @title: AppMedicalCareController
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2010:33
 */

@Tag(name = "医护 APP - 医护信息")
@RestController
@RequestMapping("/hospital/medical-care")
@Validated
public class AppMedicalCareController {

    @Resource
    private MedicalCareService medicalCareService;

    @GetMapping("/{memberId}/member")
    @Operation(summary = "根据手机号查询医护")
    @Parameter(name = "memberId", description = "手机号", required = true)
    @PreAuthenticated
    public CommonResult<MedicalCareRespVO> getMedicalCareByMemberId(@PathVariable("memberId") Long memberId) {
        MedicalCareDO medicalCare = medicalCareService.getByMemberId(memberId);
        return success(MedicalCareConvert.INSTANCE.convert(medicalCare));
    }
}
