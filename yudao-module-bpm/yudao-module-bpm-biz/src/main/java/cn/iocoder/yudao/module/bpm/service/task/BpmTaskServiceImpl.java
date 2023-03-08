package cn.iocoder.yudao.module.bpm.service.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.framework.common.util.object.PageUtils;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.framework.flowable.core.util.FlowableUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.task.*;
import cn.iocoder.yudao.module.bpm.convert.task.BpmTaskConvert;
import cn.iocoder.yudao.module.bpm.convert.task.HiVarinstConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.task.BpmActivityDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.task.BpmTaskExtDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.task.HiVarinstDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.task.BpmActivityMapper;
import cn.iocoder.yudao.module.bpm.dal.mysql.task.BpmTaskExtMapper;
import cn.iocoder.yudao.module.bpm.dal.mysql.task.HiVarinstMapper;
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceDeleteReasonEnum;
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import cn.iocoder.yudao.module.bpm.service.definition.BpmProcessDefinitionService;
import cn.iocoder.yudao.module.bpm.service.message.BpmMessageService;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.flowable.variable.service.impl.persistence.entity.HistoricVariableInstanceEntityImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.*;

/**
 * 流程任务实例 Service 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Slf4j
@Service
public class BpmTaskServiceImpl implements BpmTaskService {

    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;
    @Resource
    private BpmTaskExtMapper taskExtMapper;
    @Resource
    private BpmMessageService messageService;
    @Resource
    private BpmActivityMapper bpmActivityMapper;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private HiVarinstMapper hiVarinstMapper;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;

    @Override
    public PageResult<BpmTaskTodoPageItemRespVO> getTodoTaskPage(Long userId, BpmTaskTodoPageReqVO pageVO) {
        // 查询待办任务
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(userId)) // 分配给自己
            .orderByTaskCreateTime().desc(); // 创建时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (ArrayUtil.get(pageVO.getCreateTime(), 0) != null) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[0]));
        }
        if (ArrayUtil.get(pageVO.getCreateTime(), 1) != null) {
            taskQuery.taskCreatedBefore(DateUtils.of(pageVO.getCreateTime()[1]));
        }
        // 执行查询
        List<Task> tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        // 获得 ProcessInstance Map
        Map<String, ProcessInstance> processInstanceMap =
            processInstanceService.getProcessInstanceMap(convertSet(tasks, Task::getProcessInstanceId));
        // 获得 User Map
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
            convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        // 拼接结果
        return new PageResult<>(BpmTaskConvert.INSTANCE.convertList1(tasks, processInstanceMap, userMap),
            taskQuery.count());
    }

    @Override
    public PageResult<BpmTaskDonePageItemRespVO> getDoneTaskPage(Long userId, BpmTaskDonePageReqVO pageVO) {
        // 查询已办任务
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery().finished() // 已完成
            .taskAssignee(String.valueOf(userId)) // 分配给自己
            .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (pageVO.getBeginCreateTime() != null) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getBeginCreateTime()));
        }
        if (pageVO.getEndCreateTime() != null) {
            taskQuery.taskCreatedBefore(DateUtils.of(pageVO.getEndCreateTime()));
        }
        // 执行查询
        List<HistoricTaskInstance> tasks = taskQuery.listPage(PageUtils.getStart(pageVO), pageVO.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            return PageResult.empty(taskQuery.count());
        }

        // 获得 TaskExtDO Map
        List<BpmTaskExtDO> bpmTaskExtDOs =
            taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);
        // 获得 ProcessInstance Map
        Map<String, HistoricProcessInstance> historicProcessInstanceMap =
            processInstanceService.getHistoricProcessInstanceMap(
                convertSet(tasks, HistoricTaskInstance::getProcessInstanceId));
        // 获得 User Map
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
            convertSet(historicProcessInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        // 拼接结果
        return new PageResult<>(
            BpmTaskConvert.INSTANCE.convertList2(tasks, bpmTaskExtDOMap, historicProcessInstanceMap, userMap),
            taskQuery.count());
    }

    @Override
    public List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
    }

    @Override
    public List<BpmTaskRespVO> getTaskListByProcessInstanceId(String processInstanceId) {
        // 获得任务列表
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(processInstanceId)
            .orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
            .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        // 获得 TaskExtDO Map
        List<BpmTaskExtDO> bpmTaskExtDOs =
            taskExtMapper.selectListByTaskIds(convertSet(tasks, HistoricTaskInstance::getId));
        Map<String, BpmTaskExtDO> bpmTaskExtDOMap = convertMap(bpmTaskExtDOs, BpmTaskExtDO::getTaskId);
        // 获得 ProcessInstance Map
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User Map
        Set<Long> userIds = convertSet(tasks, task -> NumberUtils.parseLong(task.getAssignee()));
        userIds.add(NumberUtils.parseLong(processInstance.getStartUserId()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        // 获得 Dept Map
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(convertSet(userMap.values(), AdminUserRespDTO::getDeptId));

        // 拼接数据
        return BpmTaskConvert.INSTANCE.convertList3(tasks, bpmTaskExtDOMap, processInstance, userMap, deptMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(Long userId, @Valid BpmTaskApproveReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }

        // 完成任务，审批通过
        taskService.complete(task.getId(), instance.getProcessVariables());

        // 更新任务拓展表为通过
        taskExtMapper.updateByTaskId(
            new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.APPROVE.getResult())
                .setReason(reqVO.getReason()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(Long userId, @Valid BpmTaskRejectReqVO reqVO) {
        Task task = checkTask(userId, reqVO.getId());
        // 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
        }

        // 更新流程实例为不通过
        processInstanceService.updateProcessInstanceExtReject(instance.getProcessInstanceId(), reqVO.getReason());

        // 更新任务拓展表为不通过
        taskExtMapper.updateByTaskId(
            new BpmTaskExtDO().setTaskId(task.getId()).setResult(BpmProcessInstanceResultEnum.REJECT.getResult())
                .setEndTime(LocalDateTime.now()).setReason(reqVO.getReason()));
    }

    @Override
    @TenantIgnore
    @DataPermission(enable = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> backTask(Long userId, BpmTaskBackReqVO reqVO) {
        // 判断是否有审批意见, 没有的时候设置为空字符串
        if (StrUtil.isBlank(reqVO.getReason())) {
            reqVO.setReason("");
        }
        // 校验任务存在
        checkTask(userId, reqVO.getTaskId());
        // 获取审批流程所有任务
        List<BpmActivityDO> bpmActivityDOList = bpmActivityMapper.listAllByProcInstId(reqVO.getProcInstId());
        // 获取任务id
        List<String> taskIdList = bpmActivityDOList.stream().filter(
                bpmActivityDO -> bpmActivityDO.getActivityId().equals(reqVO.getOldTaskDefKey())
                    && bpmActivityDO.getRev().equals(1)
                    && !bpmActivityDO.getTaskId().equals(reqVO.getTaskId()))
            .map(BpmActivityDO::getTaskId).collect(Collectors.toList());
        // 获取未完成的任务, 用于获取执行任务的父id, 并且用未完成的数量计算已完成的任务数量
        List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId(reqVO.getProcInstId())
            .activityId(reqVO.getOldTaskDefKey()).list();
        // 判断是否有未完成的任务
        if (CollUtil.isEmpty(executionList)) {
            throw exception(EXECUTION_NOT_EXISTS);
        }
        // 获取任务数量变量值, 值包括(nrOfInstances(全部任务数), nrOfCompletedInstances(完成任务数), nrOfActiveInstances(未完成任务数))
        Set<String> executionIdSet = convertSet(executionList, Execution::getId);
        executionIdSet.add(executionList.get(0).getParentId());
        List<HistoricVariableInstance> historicVariableInstanceList =
            historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(reqVO.getProcInstId())
                .executionIds(executionIdSet).list();

        // 初始化当前任务标识列表用于驳回
        ArrayList<String> oldTaskDefKeyList = CollUtil.newArrayList(reqVO.getOldTaskDefKey());
        // 使用flowable更改任务节点
        runtimeService.createChangeActivityStateBuilder()
            .processInstanceId(reqVO.getProcInstId())
            .moveActivityIdsToSingleActivityId(oldTaskDefKeyList, reqVO.getNewTaskDefKey())
            .changeState();
        // 更新流程数据
        updateProcessInstanceVariable(taskIdList, reqVO, historicVariableInstanceList, executionList);
        return CommonResult.success(true);
    }

    @Override
    public CommonResult<List<BpmBackTaskRespVO>> getBackTaskRule(BpmBackTaskReqVO reqVO) {
        List<UserTask> resultUserTaskList = new ArrayList<>();
        // 获取流程模型
        BpmnModel bpmnModel = processDefinitionService.getBpmnModel(reqVO.getProcDefId());
        // 获取模型中的第一个用户任务
        UserTask userTask = FlowableUtils.getBpmModelOneUserTask(bpmnModel);
        if (ObjectUtil.isNull(userTask)) {
            throw exception(BACK_TASK_NOT_ONE_USER_TASK);
        }
        if (!"申请人".equals(userTask.getName())) {
            resultUserTaskList.add(userTask);
        }
        // 获取审批流程表单数据
        List<HistoricVariableInstance> hisVarList = historyService.createHistoricVariableInstanceQuery()
            .processInstanceId(reqVO.getProcInstId()).list();
        Map<String, Object> formVariables = new HashMap<>(hisVarList.size() + 50);
        for (HistoricVariableInstance hisVarInst : hisVarList) {
            formVariables.put(hisVarInst.getVariableName(), hisVarInst.getValue());
        }
        List<UserTask> flow = FlowableUtils.getFlow(bpmnModel, userTask, formVariables);
        for (; ; ) {
            if (ObjectUtil.isNull(flow)) {
                throw exception(BACK_TASK_NOT_USER_TASK);
            }
            List<String> tmpList = convertList(flow, UserTask::getId);
            if (tmpList.contains(reqVO.getOldTaskDefKey())) {
                break;
            }
            resultUserTaskList.addAll(flow);
            flow = FlowableUtils.getFlow(bpmnModel, flow, formVariables);
        }
        return CommonResult.success(BpmTaskConvert.INSTANCE.convertList(resultUserTaskList));
    }

    @TenantIgnore
    @DataPermission(enable = false)
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessInstanceVariable(List<String> taskIdList, BpmTaskBackReqVO reqVO,
        List<HistoricVariableInstance> historicVariableInstanceList, List<Execution> executionList) {
        // 更新流程引擎数据, 如果不手动更新, 流程引擎会不更新驳回之前的旧任务, 导致审批单无法正常进行流转
        Boolean delHiActInstResult = true;
        Boolean delHiTaskInstResult = true;
        Boolean delTaskResult = true;
        if (CollUtil.isNotEmpty(taskIdList) && taskIdList.size() > 0) {
            // 逻辑删除扩展表任务
            delTaskResult = taskExtMapper.delByTaskIds(taskIdList);
            // 逻辑删除hiActInst表任务
            delHiActInstResult = bpmActivityMapper.delHiActInstByTaskId(taskIdList);
            // 逻辑删除hiTaskInst表任务
            delHiTaskInstResult = bpmActivityMapper.delHiTaskInstByTaskId(taskIdList);
        }
        // 如果有任何一个表没有逻辑删除成功, 所有数据回滚, 报错任务驳回失败
        if (!delHiActInstResult || !delHiTaskInstResult || !delTaskResult) {
            throw exception(BACK_TASK_NOT_ERROR);
        }
        /*
         * 修改ACT_HI_VARINST表数据, 由于回退任务时, 引擎不会自动修改之前执行的任务数据,
         * 因此需要手动修改执行对应的任务数据
         * nrOfCompletedInstances：已完成的实例 nr是number单词缩写 需要修改
         * nrOfActiveInstances：未完成的实例个数 需要修改
         **/
        // 转为map
        Map<String, HistoricVariableInstance> historicVariableInstanceMap =
            convertMap(historicVariableInstanceList, HistoricVariableInstance::getVariableName);
        HistoricVariableInstanceEntityImpl nrOfInstances =
            (HistoricVariableInstanceEntityImpl)historicVariableInstanceMap.get("nrOfInstances");
        HistoricVariableInstanceEntityImpl nrOfCompletedInstances =
            (HistoricVariableInstanceEntityImpl)historicVariableInstanceMap.get("nrOfCompletedInstances");
        if (ObjectUtil.isNotEmpty(nrOfCompletedInstances)) {
            HiVarinstDO nrOfCompletedInstancesVar =
                HiVarinstConvert.INSTANCE.nrOfCompletedInstancesConvert(nrOfCompletedInstances, nrOfInstances,
                    executionList);
            hiVarinstMapper.updateById(nrOfCompletedInstancesVar);
        }
        HistoricVariableInstanceEntityImpl nrOfActiveInstances =
            (HistoricVariableInstanceEntityImpl)historicVariableInstanceMap.get("nrOfActiveInstances");
        if (ObjectUtil.isNotEmpty(nrOfActiveInstances)) {
            HiVarinstDO nrOfActiveInstancesVar =
                HiVarinstConvert.INSTANCE.nrOfActiveInstancesConvert(nrOfActiveInstances, executionList);
            hiVarinstMapper.updateById(nrOfActiveInstancesVar);
        }
        // 更新任务拓展表
        taskExtMapper.updateByTaskId(new BpmTaskExtDO()
            .setTaskId(reqVO.getTaskId())
            .setEndTime(new DateTime().toLocalDateTime())
            .setResult(BpmProcessInstanceResultEnum.BACK.getResult())
            .setReason(reqVO.getReason()));
    }

    @Override
    public void updateTaskAssignee(Long userId, BpmTaskUpdateAssigneeReqVO reqVO) {
        // 校验任务存在
        Task task = checkTask(userId, reqVO.getId());
        // 更新负责人
        updateTaskAssignee(task.getId(), reqVO.getAssigneeUserId());
    }

    @Override
    public void updateTaskAssignee(String id, Long userId) {
        taskService.setAssignee(id, String.valueOf(userId));
    }

    /**
     * 校验任务是否存在， 并且是否是分配给自己的任务
     *
     * @param userId 用户 id
     * @param taskId task id
     */
    private Task checkTask(Long userId, String taskId) {
        Task task = getTask(taskId);
        if (task == null) {
            throw exception(TASK_COMPLETE_FAIL_NOT_EXISTS);
        }
        if (!Objects.equals(userId, NumberUtils.parseLong(task.getAssignee()))) {
            throw exception(TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF);
        }
        return task;
    }

    @Override
    public void createTaskExt(Task task) {
        BpmTaskExtDO taskExtDO =
            BpmTaskConvert.INSTANCE.convert2TaskExt(task).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        taskExtMapper.insert(taskExtDO);
    }

    @Override
    public void updateTaskExtComplete(Task task) {
        BpmTaskExtDO taskExtDO = BpmTaskConvert.INSTANCE.convert2TaskExt(task)
            .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()) // 不设置也问题不大，因为 Complete 一般是审核通过，已经设置
            .setEndTime(LocalDateTime.now());
        taskExtMapper.updateByTaskId(taskExtDO);
    }

    @Override
    public void updateTaskExtCancel(String taskId) {
        // 需要在事务提交后，才进行查询。不然查询不到历史的原因
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                // 可能只是活动，不是任务，所以查询不到
                HistoricTaskInstance task = getHistoricTask(taskId);
                if (task == null) {
                    return;
                }

                // 如果任务拓展表已经是完成的状态，则跳过
                BpmTaskExtDO taskExt = taskExtMapper.selectByTaskId(taskId);
                if (taskExt == null) {
                    log.error("[updateTaskExtCancel][taskId({}) 查找不到对应的记录，可能存在问题]", taskId);
                    return;
                }
                // 如果已经是最终的结果，则跳过
                if (BpmProcessInstanceResultEnum.isEndResult(taskExt.getResult())) {
                    log.error("[updateTaskExtCancel][taskId({}) 处于结果({})，无需进行更新]", taskId,
                        taskExt.getResult());
                    return;
                }

                // 更新任务
                taskExtMapper.updateById(
                    new BpmTaskExtDO().setId(taskExt.getId()).setResult(BpmProcessInstanceResultEnum.CANCEL.getResult())
                        .setEndTime(LocalDateTime.now())
                        .setReason(BpmProcessInstanceDeleteReasonEnum.translateReason(task.getDeleteReason())));
            }

        });
    }

    @Override
    public void updateTaskExtAssign(Task task) {
        BpmTaskExtDO taskExtDO =
            new BpmTaskExtDO().setAssigneeUserId(NumberUtils.parseLong(task.getAssignee())).setTaskId(task.getId());
        taskExtMapper.updateByTaskId(taskExtDO);
        // 发送通知。在事务提交时，批量执行操作，所以直接查询会无法查询到 ProcessInstance，所以这里是通过监听事务的提交来实现。
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                ProcessInstance processInstance =
                    processInstanceService.getProcessInstance(task.getProcessInstanceId());
                AdminUserRespDTO startUser = adminUserApi.getUser(Long.valueOf(processInstance.getStartUserId()));
                messageService.sendMessageWhenTaskAssigned(
                    BpmTaskConvert.INSTANCE.convert(processInstance, startUser, task));
            }
        });
    }

    private Task getTask(String id) {
        return taskService.createTaskQuery().taskId(id).singleResult();
    }

    private HistoricTaskInstance getHistoricTask(String id) {
        return historyService.createHistoricTaskInstanceQuery().taskId(id).singleResult();
    }

}
