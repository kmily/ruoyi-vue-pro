package cn.iocoder.yudao.module.therapy.controller.admin.flow;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.SaveFlowReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.TreatmentFlowRespVO;
import cn.iocoder.yudao.module.therapy.convert.TreatmentFlowConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 治疗方案")
@RestController
@RequestMapping("/therapy/flow")
@Validated
public class TreatmentFlowController {

    @Resource
    private TreatmentService treatmentService;

    @Resource
    private AdminUserApi adminUserApi;
    @PostMapping("/createFlow")
    @Operation(summary = "创建方案")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> createFlow(@Valid @RequestBody SaveFlowReqVO reqVO) {
        Long id = treatmentService.createTreatmentFlow(reqVO);
        return success(id);
    }

    @PostMapping("/updateFlow")
    @Operation(summary = "更新方案")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Boolean> updatgeFlow(@Valid @RequestBody SaveFlowReqVO reqVO) {
        treatmentService.updateTreatmentFlow(reqVO);
        return success(true);
    }

//    @PostMapping("/addPlan")
//    @Operation(summary = "添加计划")
////    @PreAuthorize("@ss.hasPermission('system:user:create')")
//    public CommonResult<Long> addPlan(@Valid @RequestBody SurveySaveReqVO reqVO) {
//        Long id = surveyService.createSurvey(reqVO);
//        return success(id);
//    }
//
//    @PostMapping("/deletePlan")
//    @Operation(summary = "删除计划")
////    @PreAuthorize("@ss.hasPermission('system:user:create')")
//    public CommonResult<Long> delPlan(@Valid @RequestBody SurveySaveReqVO reqVO) {
//        Long id = surveyService.createSurvey(reqVO);
//        return success(id);
//    }

    @GetMapping("/getFlow")
    @Operation(summary = "获取方案")
    @Parameter(name = "id", description = "方案id", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<SaveFlowReqVO> getFlow(@RequestParam("id") Long id) {
        TreatmentFlowDO flow = treatmentService.getTreatmentFlow(id);
        SaveFlowReqVO vo= BeanUtils.toBean(flow, SaveFlowReqVO.class);
        return success(vo);
    }

    @GetMapping("/page")
    @Operation(summary = "获得治疗流程模板分页")
//    @PreAuthorize("@ss.hasPermission('hlgyy:treatment-flow:query')")
    public CommonResult<PageResult<TreatmentFlowRespVO>> getTreatmentFlowPage(@Valid PageParam pageReqVO) {
        PageResult<TreatmentFlowDO> pageResult = treatmentService.getTreatmentFlowPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        Set<Long> userIds = pageResult.getList().stream()
                .map(TreatmentFlowDO::getCreator)
                .filter(Objects::nonNull)
                .map(x -> Long.parseLong(x))
                .collect(Collectors.toSet());
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        return success(new PageResult<>(TreatmentFlowConvert.INSTANCE.convertList(pageResult.getList(), userMap), pageResult.getTotal()));
    }
}
