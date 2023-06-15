package cn.iocoder.yudao.module.oa.controller.app.feedback;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.module.oa.controller.admin.feedback.vo.*;
import cn.iocoder.yudao.module.oa.convert.feedback.FeedbackConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.feedback.FeedbackDO;
import cn.iocoder.yudao.module.oa.service.feedback.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "用户App - 产品反馈")
@RestController
@RequestMapping("/oa/feedback")
@Validated
public class AppFeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @PostMapping("/create")
    @Operation(summary = "创建产品反馈")
    @PreAuthenticated
    public CommonResult<Long> createFeedback(@Valid @RequestBody FeedbackCreateReqVO createReqVO) {
        return success(feedbackService.createFeedback(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品反馈")
    @PreAuthenticated
    public CommonResult<Boolean> updateFeedback(@Valid @RequestBody FeedbackUpdateReqVO updateReqVO) {
        feedbackService.updateFeedback(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品反馈")
    @PreAuthenticated
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteFeedback(@RequestParam("id") Long id) {
        feedbackService.deleteFeedback(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品反馈")
    @PreAuthenticated
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<FeedbackRespVO> getFeedback(@RequestParam("id") Long id) {
        FeedbackDO feedback = feedbackService.getFeedback(id);
        return success(FeedbackConvert.INSTANCE.convert(feedback));
    }

    @GetMapping("/list")
    @Operation(summary = "获得产品反馈列表")
    @PreAuthenticated
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<FeedbackRespVO>> getFeedbackList(@RequestParam("ids") Collection<Long> ids) {
        List<FeedbackDO> list = feedbackService.getFeedbackList(ids);
        return success(FeedbackConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品反馈分页")
    @PreAuthenticated
    public CommonResult<PageResult<FeedbackRespVO>> getFeedbackPage(@Valid FeedbackPageReqVO pageVO) {
        PageResult<FeedbackDO> pageResult = feedbackService.getFeedbackPage(pageVO);
        return success(FeedbackConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品反馈 Excel")
    @OperateLog(type = EXPORT)
    @PreAuthenticated
    public void exportFeedbackExcel(@Valid FeedbackExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<FeedbackDO> list = feedbackService.getFeedbackList(exportReqVO);
        // 导出 Excel
        List<FeedbackExcelVO> datas = FeedbackConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "产品反馈.xls", "数据", FeedbackExcelVO.class, datas);
    }

}
