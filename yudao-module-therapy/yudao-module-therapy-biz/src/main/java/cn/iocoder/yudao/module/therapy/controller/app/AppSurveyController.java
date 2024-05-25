package cn.iocoder.yudao.module.therapy.controller.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "C端 - 治疗问卷")
@RestController
@RequestMapping("/therapy/survey")
@Validated
public class AppSurveyController {

    @Resource
    private SurveyService surveyService;

    @PostMapping("/submit")
    @Operation(summary = "提交问卷")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Boolean> submit(@Valid @RequestBody SubmitSurveyReqVO reqVO) {
        surveyService.submitSurvey(reqVO);
        return success(true);
    }
}
