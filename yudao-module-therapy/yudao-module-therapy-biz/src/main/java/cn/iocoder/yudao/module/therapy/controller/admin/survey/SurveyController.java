package cn.iocoder.yudao.module.therapy.controller.admin.survey;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.*;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentScheduleRespVO;
import cn.iocoder.yudao.module.therapy.convert.SurveyConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentScheduleDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TreatmentScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_NOT_EXISTS;
import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_QUESTION_TYPE_CHANGE;
import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;


@Tag(name = "管理后台 - 治疗问卷")
@RestController
@RequestMapping("/therapy/survey")
@Validated
public class SurveyController {

    @Resource
    private SurveyService surveyService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private TreatmentScheduleService treatmentScheduleService;

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
        if (reqVO.getId() <= 0) throw exception(BAD_REQUEST);
        surveyService.updateSurvey(reqVO);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得治疗问卷分页列表")
//    @PreAuthorize("@ss.hasPermission('system:user:list')")
    public CommonResult<PageResult<SurveyRespVO>> getSurveyPage(@Valid SurveyPageReqVO pageReqVO) {
        // 获得用户分页列表
        PageResult<TreatmentSurveyDO> pageResult = surveyService.getSurveyPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        Set<Long> userIds = pageResult.getList().stream()
                .map(TreatmentSurveyDO::getCreator)
                .filter(Objects::nonNull)
                .map(x -> Long.parseLong(x))
                .collect(Collectors.toSet());
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        return success(new PageResult<>(SurveyConvert.INSTANCE.convertList(pageResult.getList(), userMap), pageResult.getTotal()));
    }

    @GetMapping("/get")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @Operation(summary = "获得治疗问卷")
//    @PreAuthorize("@ss.hasPermission('system:user:list')")
    public CommonResult<SurveySaveReqVO> getSurvey(@RequestParam("id") Long id) {
        TreatmentSurveyDO tsDO = surveyService.get(id);
        if (Objects.isNull(tsDO)) {
            throw exception(SURVEY_NOT_EXISTS);
        }
        List<QuestionDO> qstList = surveyService.getQuestionBySurveyId(id);

        return success(SurveyConvert.INSTANCE.convert(tsDO, qstList));
    }

    @GetMapping("/delete")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @Operation(summary = "删除治疗问卷")
//    @PreAuthorize("@ss.hasPermission('system:user:list')")
    public CommonResult<Boolean> delSurvey(@RequestParam("id") Long id) {
        surveyService.del(id);
        return success(true);
    }

    @GetMapping("/getSurveyAnswerPage")
    @Operation(summary = "获得患者答题列表")
    public CommonResult<PageResult<SurveyAnswerRespVO>> getSurveyAnswerPage(@Valid SurveyAnswerPageReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
        if (reqVO.getSurveyType().equals(SurveyType.SCHEDULE_LIST)) {
            return success(this.getScheduleList(reqVO));
        }

        PageResult<SurveyAnswerDO> pageResult = surveyService.getSurveyAnswerPage(reqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }

        Set<Long> surveyIds = pageResult.getList().stream()
                .map(SurveyAnswerDO::getBelongSurveyId)
                .collect(Collectors.toSet());
        List<TreatmentSurveyDO> treatmentSurveyDOS = surveyService.getSurveyByIds(surveyIds);
        Map<Long, TreatmentSurveyDO> treatmentSurveyDOMap = CollectionUtils.convertMap(treatmentSurveyDOS, TreatmentSurveyDO::getId);
        return success(new PageResult<>(SurveyConvert.INSTANCE.convertList(treatmentSurveyDOMap, pageResult.getList()), pageResult.getTotal()));
    }

    private PageResult<SurveyAnswerRespVO> getScheduleList(SurveyAnswerPageReqVO reqVO) {
        PageResult<TreatmentScheduleDO> result = treatmentScheduleService.getTreatmentSchedulePage(reqVO);
        List<SurveyAnswerRespVO> list = new ArrayList<>();
        for (TreatmentScheduleDO item : result.getList()) {
            SurveyAnswerRespVO respVO = new SurveyAnswerRespVO();
            respVO.setId(item.getId());
            respVO.setTitle(item.getName());
            respVO.setCreateTime(item.getCreateTime());
            respVO.setUpdateTime(item.getUpdateTime());
            respVO.setSource(2);
            respVO.setSurveyType(SurveyType.SCHEDULE_LIST.getType());
            list.add(respVO);
        }
        return new PageResult<>(list, result.getTotal());
    }

    @GetMapping("/initSurveyAnswer")
    @Parameter(name = "surveyCode", description = "问卷code", required = true, example = "1024")
    @Parameter(name = "source", description = "source", required = true, example = "1024")
    @Operation(summary = "实例化答题实例")
    public CommonResult<Long> initSurveyAnswer(@RequestParam("surveyCode") String surveyCode, @RequestParam("source") Integer source) {
        return success(surveyService.initSurveyAnswer(surveyCode, source));
    }

    @PostMapping("/submitForTools")
    @Operation(summary = "流程提交问卷")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> submitForTools(@Valid @RequestBody SubmitSurveyReqVO reqVO) {
//        reqVO.setSource(2);
        return success(surveyService.submitSurveyForTools(reqVO));
    }

    @PostMapping("/submitForFlow")
    @Operation(summary = "工具箱提交问卷")
    public CommonResult<Long> submitForFlow(@Valid @RequestBody SubmitSurveyReqVO reqVO) {
//        reqVO.setSource(1);
        return success(surveyService.submitSurveyForFlow(reqVO));
    }


    @PostMapping("/setSurveyRel")
    @Operation(summary = "设置问卷关联性")
    @Parameter(name = "id", description = "问卷id", required = true, example = "1024")
    @Parameter(name = "relList", description = "关联问卷id", required = true, example = "1024,43,534")
    public CommonResult<Boolean> setSurveyRel(@RequestParam("id") Long id, @RequestParam("relList") List<Long> relList) {
//        reqVO.setSource(1);
        return success(true);
    }

}
