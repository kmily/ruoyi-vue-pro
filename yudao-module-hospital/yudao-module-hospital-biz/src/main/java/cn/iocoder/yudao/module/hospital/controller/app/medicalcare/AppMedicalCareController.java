package cn.iocoder.yudao.module.hospital.controller.app.medicalcare;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.MedicalCareRespVO;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.MedicalCareUpdateReqVO;
import cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo.AppCareAptitudePageReqVO;
import cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo.AppCareAptitudeRespVO;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.convert.careaptitude.CareAptitudeConvert;
import cn.iocoder.yudao.module.hospital.convert.medicalcare.MedicalCareConvert;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import cn.iocoder.yudao.module.hospital.service.medicalcare.CareFavoriteService;
import cn.iocoder.yudao.module.hospital.service.medicalcare.MedicalCareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
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

    @Resource
    private CareFavoriteService careFavoriteService;

    @GetMapping("/{memberId}/member")
    @Operation(summary = "根据手机号查询医护")
    @Parameter(name = "memberId", description = "手机号", required = true)
    @PreAuthenticated
    public CommonResult<MedicalCareRespVO> getMedicalCareByMemberId(@PathVariable("memberId") Long memberId) {
        MedicalCareDO medicalCare = medicalCareService.getByMemberId(memberId);
        return success(MedicalCareConvert.INSTANCE.convert(medicalCare));
    }

    @PutMapping("/perfect")
    @Operation(summary = "完善医护信息")
    @PreAuthenticated
    public CommonResult<Boolean> perfectMedicalCare(@Valid @RequestBody AppMedicalCarePerfectVO perfectVO) {
        MedicalCareDO medicalCareDO = medicalCareService.getByMemberId(SecurityFrameworkUtils.getLoginUserId());
        medicalCareService.perfectMedicalCare(perfectVO.setId(medicalCareDO.getId()));
        return success(true);
    }

    @PutMapping("/real-name")
    @Operation(summary = "医护人员实名")
    @PreAuthenticated
    public CommonResult<Boolean> realNameMedicalCare(@Valid @RequestBody AppRealNameReqVO realNameReqVO) {
       // MedicalCareDO medicalCareDO = medicalCareService.getByMemberId(SecurityFrameworkUtils.getLoginUserId());
        medicalCareService.realNameMedicalCare(realNameReqVO.setId(SecurityFrameworkUtils.getLoginUserId()));
        return success(true);
    }


    @GetMapping("/page")
    @Operation(summary = "获得医护分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppMedicalCareRespVO>> getCareAptitudePage(@Valid AppMedicalCarePageReqVO pageVO) {
        PageResult<MedicalCareDO> pageResult = medicalCareService.getCareAptitudePage(pageVO);
        return success(CareAptitudeConvert.INSTANCE.convertPage01(pageResult));
    }

    @GetMapping("/get")
    @Operation(summary = "获得医护信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppMedicalCareRespVO> getMedicalCare(@RequestParam("id") Long id) {
        MedicalCareDO medicalCare = medicalCareService.getMedicalCare(id);
        AppMedicalCareRespVO convert = MedicalCareConvert.INSTANCE.convert02(medicalCare);
        // TODO  此处后面添加查询搜藏数量
        Long careFavorite = careFavoriteService.getCareFavorite(convert.getId(), null);
        convert.setCollect(careFavorite);
        return success(convert);
    }


    @GetMapping("/favorite-page")
    @Operation(summary = "查询我的收藏")
    @PreAuthenticated
    public CommonResult<PageResult<AppMedicalCareRespVO>> getCareFavoritePage(@Valid CareFavoritePageReqVO pageVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        pageVO.setUserId(userId);
        PageResult<MedicalCareDO> pageResult = medicalCareService.getCareFavoritePage(pageVO);
        return success(CareAptitudeConvert.INSTANCE.convertPage01(pageResult));
    }
}
