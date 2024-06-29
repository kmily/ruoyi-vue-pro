package cn.iocoder.yudao.module.therapy.controller.admin.flow;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.FlowPlanReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.FlowTaskVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.SaveFlowReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.TreatmentFlowRespVO;
import cn.iocoder.yudao.module.therapy.convert.TreatmentFlowConvert;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDayitemDO;
import cn.iocoder.yudao.module.therapy.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.TREATMENT_FLOW_NOT_EXISTS;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
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

    @PostMapping("/addPlan")
    @Operation(summary = "添加计划")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> addPlan(@Valid @RequestBody FlowPlanReqVO reqVO) {
//        Long id = surveyService.createSurvey(reqVO);
        return success(treatmentService.addPlan(reqVO));
    }

    @PostMapping("/updatePlan")
    @Operation(summary = "更新计划")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Boolean> updatePlan(@Valid @RequestBody FlowPlanReqVO reqVO) {
        treatmentService.updatePlan(reqVO);
        return success(true);
    }

    //
    @PutMapping("/deletePlan")
    @Operation(summary = "删除计划")
    @Parameter(name = "id", description = "计划id", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Boolean> delPlan(@RequestParam("id") Long id) {
        treatmentService.delPlan(id);
        return success(true);
    }

    @GetMapping("/getFlow")
    @Operation(summary = "获取方案")
    @Parameter(name = "id", description = "方案id", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<TreatmentFlowRespVO> getFlow(@RequestParam("id") Long id) {
        TreatmentFlowDO flow = treatmentService.getTreatmentFlow(id);
        if (Objects.isNull(flow)) return success(null);
        TreatmentFlowRespVO vo = BeanUtils.toBean(flow, TreatmentFlowRespVO.class);
        AdminUserRespDTO dto = adminUserApi.getUser(Long.parseLong(flow.getCreator()));
        vo.setCreatorName(dto.getNickname());
        //获取治疗日
        vo.setPlanList(new ArrayList<>());
        List<TreatmentFlowDayDO> flowDayDOS = treatmentService.getPlanListByFlowId(id);
        for (TreatmentFlowDayDO item : flowDayDOS) {
            vo.getPlanList().add(BeanUtils.toBean(item, FlowPlanReqVO.class));
        }
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

    @PostMapping("/createTask")
    @Operation(summary = "创建计划的任务")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> createTask(@Valid @RequestBody FlowTaskVO reqVO) {
        reqVO.setId(0L);
        Long id = treatmentService.createPlanTask(reqVO);
        return success(id);
    }

    @PostMapping("/updateTask")
    @Operation(summary = "创建计划的任务")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Boolean> updateTask(@Valid @RequestBody FlowTaskVO reqVO) {
        treatmentService.updatePlanTask(reqVO);
        return success(true);
    }

    @PutMapping("/deleteTask")
    @Operation(summary = "删除计划的任务")
    @Parameter(name = "id", description = "计划id", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Boolean> delTask(@RequestParam("id") Long id) {
        treatmentService.delPlanTask(id);
        return success(true);
    }

    @GetMapping("/getTaskList")
    @Operation(summary = "获取计划任务列表")
    @Parameter(name = "id", description = "计划id", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<List<FlowTaskVO>> getTaskList(@RequestParam("id") Long id) {
        List<TreatmentFlowDayitemDO> taskList = treatmentService.getTaskListByDayId(id);
        if (CollectionUtil.isEmpty(taskList)) {
            return success(new ArrayList<>());
        }
        List<FlowTaskVO> taskVOS = new ArrayList<>();
        for (TreatmentFlowDayitemDO item : taskList) {
            FlowTaskVO vo = BeanUtils.toBean(item, FlowTaskVO.class);
            taskVOS.add(vo);
        }
        return success(taskVOS);
    }

    @GetMapping("/getTask")
    @Operation(summary = "获取计划任务")
    @Parameter(name = "id", description = "任务id", required = true, example = "1024")
    public CommonResult<FlowTaskVO> getTask(@RequestParam("id") Long id) {
        TreatmentFlowDayitemDO dayitemDO = treatmentService.getTask(id);
        FlowTaskVO vo = BeanUtils.toBean(dayitemDO, FlowTaskVO.class);
        return success(vo);
    }

    @GetMapping("/publish")
    @Operation(summary = "发布方案")
    @Parameter(name = "id", description = "方案id", required = true, example = "1024")
    public CommonResult<Boolean> publish(@RequestParam("id") Long id) {
        treatmentService.publish(id, CommonStatusEnum.ENABLE.getStatus());
        return success(true);
    }

}
