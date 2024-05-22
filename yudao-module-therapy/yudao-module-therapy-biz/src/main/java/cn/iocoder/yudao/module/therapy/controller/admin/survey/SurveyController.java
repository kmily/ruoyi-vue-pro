package cn.iocoder.yudao.module.therapy.controller.admin.survey;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyRespVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.convert.SurveyConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 治疗问卷")
@RestController
@RequestMapping("/therapy/survey")
@Validated
public class SurveyController {

    @Resource
    private SurveyService surveyService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建问卷")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> createSurvey(@Valid @RequestBody SurveySaveReqVO reqVO) {
        Long id = surveyService.createSurvey(reqVO);
        return success(id);
    }

    @PutMapping("update")
    @Operation(summary = "修改问卷")
//    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public CommonResult<Boolean> updateSurvey(@Valid @RequestBody SurveySaveReqVO reqVO) {
        if(reqVO.getId()<=0) throw exception(BAD_REQUEST);
        surveyService.updateSurvey(reqVO);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得治疗问卷分页列表")
//    @PreAuthorize("@ss.hasPermission('system:user:list')")
    public CommonResult<PageResult<SurveyRespVO>> getUserPage(@Valid SurveyPageReqVO pageReqVO) {
        // 获得用户分页列表
        PageResult<TreatmentSurveyDO> pageResult = surveyService.getSurveyPage(pageReqVO);
//        if (CollUtil.isEmpty(pageResult.getList())) {
//            return success(new PageResult<>(pageResult.getTotal()));
//        }
//        // 拼接数据
//        Map<Long, DeptDO> deptMap = deptService.getDeptMap(
//                convertList(pageResult.getList(), AdminUserDO::getDeptId));
        return success(new PageResult<>(SurveyConvert.INSTANCE.convertList(pageResult.getList()), pageResult.getTotal()));
    }


}
