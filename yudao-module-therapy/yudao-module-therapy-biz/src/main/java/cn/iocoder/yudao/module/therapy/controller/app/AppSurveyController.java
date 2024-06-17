package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SignInReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentScheduleRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentScheduleSaveReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentScheduleDO;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "C端 - 治疗问卷")
@RestController
@RequestMapping("/therapy/survey")
@Validated
public class AppSurveyController {

    @Resource
    private SurveyService surveyService;
    @Resource
    private TreatmentScheduleService treatmentScheduleService;

    @PostMapping("/submitForTools")
    @Operation(summary = "工具箱提交问卷")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> submitForTools(@Valid @RequestBody SubmitSurveyReqVO reqVO) {
//        reqVO.setSource(2);
        return success(surveyService.submitSurveyForTools(reqVO));
    }

    @PostMapping("/submitForFlow")
    @Operation(summary = "流程提交问卷")
    public CommonResult<Long> submitForFlow(@Valid @RequestBody SubmitSurveyReqVO reqVO) {
//        reqVO.setSource(1);
        return success(surveyService.submitSurveyForFlow(reqVO));
    }


    @PostMapping("/createSchedule")
    @Operation(summary = "创建患者日程")
//    @PreAuthorize("@ss.hasPermission('hlgyy:treatment-schedule:create')")
    public CommonResult<Long> createTreatmentSchedule(@Valid @RequestBody TreatmentScheduleSaveReqVO createReqVO) {
        return success(treatmentScheduleService.createTreatmentSchedule(createReqVO));
    }

    @DeleteMapping("/deleteSchedule")
    @Operation(summary = "删除患者日程")
    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('hlgyy:treatment-schedule:delete')")
    public CommonResult<Boolean> deleteTreatmentSchedule(@RequestParam("id") Long id) {
        treatmentScheduleService.deleteTreatmentSchedule(id);
        return success(true);
    }

    @GetMapping("/getSchedule")
    @Operation(summary = "获得患者日程")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('hlgyy:treatment-schedule:query')")
    public CommonResult<TreatmentScheduleRespVO> getTreatmentSchedule(@RequestParam("id") Long id) {
        TreatmentScheduleDO treatmentSchedule = treatmentScheduleService.getTreatmentSchedule(id);
        return success(BeanUtils.toBean(treatmentSchedule, TreatmentScheduleRespVO.class));
    }

    @PostMapping("/signIn")
    @Operation(summary = "签到")
//    @PreAuthorize("@ss.hasPermission('hlgyy:treatment-schedule:create')")
    public CommonResult<Boolean> signIn(@Valid @RequestBody SignInReqVO reqVO) {
        treatmentScheduleService.signIn(reqVO);
        return success(true);
    }

    @GetMapping("/listSchedule")
    @Operation(summary = "获得患者指定日期日程列表")
//    @PreAuthorize("@ss.hasPermission('hlgyy:treatment-schedule:query')")
    public CommonResult<List<TreatmentScheduleRespVO>> getTreatmentSchedulePage(@Valid @Param("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        List<TreatmentScheduleDO> pageResult = treatmentScheduleService.getScheduleList(day);
        return success(BeanUtils.toBean(pageResult, TreatmentScheduleRespVO.class));
    }

    @GetMapping("/getGoalMotive")
    @Operation(summary = "获取目标与动机详情")
    public CommonResult<SubmitSurveyReqVO> getGoalMotive() {
        return success(surveyService.getGoalMotive(getLoginUserId()));
    }

    @GetMapping("/getThoughtTrap")
    @Operation(summary = "获取思维陷阱详情")
    public CommonResult<SubmitSurveyReqVO> getThoughtTrap() {
        return success(surveyService.getThoughtTrap());

    }
}
