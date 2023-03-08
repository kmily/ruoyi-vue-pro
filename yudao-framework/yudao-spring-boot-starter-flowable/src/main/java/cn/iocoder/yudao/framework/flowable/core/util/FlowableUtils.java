package cn.iocoder.yudao.framework.flowable.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.de.odysseus.el.ExpressionFactoryImpl;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.javax.el.ExpressionFactory;
import org.flowable.common.engine.impl.javax.el.PropertyNotFoundException;
import org.flowable.common.engine.impl.javax.el.ValueExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * Flowable 相关的工具方法
 *
 * @author 芋道源码
 */
@Slf4j
public class FlowableUtils {

    private static final ErrorCode DEFAULT_FLOW_NULL = new ErrorCode(100203000, "没有默认流转路径");

    // ========== User 相关的工具方法 ==========

    public static void setAuthenticatedUserId(Long userId) {
        Authentication.setAuthenticatedUserId(String.valueOf(userId));
    }

    public static void clearAuthenticatedUserId() {
        Authentication.setAuthenticatedUserId(null);
    }

    // ========== BPMN 相关的工具方法 ==========

    /**
     * 获得 BPMN 流程中，指定的元素们
     *
     * @param model
     * @param clazz 指定元素。例如说，{@link org.flowable.bpmn.model.UserTask}、{@link org.flowable.bpmn.model.Gateway} 等等
     *
     * @return 元素们
     */
    public static <T extends FlowElement> List<T> getBpmnModelElements(BpmnModel model, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        model.getProcesses().forEach(process -> {
            process.getFlowElements().forEach(flowElement -> {
                if (flowElement.getClass().isAssignableFrom(clazz)) {
                    result.add((T)flowElement);
                }
            });
        });
        return result;
    }

    /**
     * 获得 BPMN 流程中，开始节点
     *
     * @param model
     *
     * @return 元素们
     */
    public static StartEvent getBpmnModelStartEvent(BpmnModel model) {
        for (Process process : model.getProcesses()) {
            for (FlowElement flowElement : process.getFlowElements()) {
                if (flowElement.getClass().isAssignableFrom(StartEvent.class)) {
                    return (StartEvent)flowElement;
                }
            }
        }
        return null;
    }

    public static UserTask getBpmModelOneUserTask(BpmnModel model) {
        StartEvent bpmnModelStartEvent = getBpmnModelStartEvent(model);
        List<SequenceFlow> outgoingFlows = null;
        if (bpmnModelStartEvent != null) {
            outgoingFlows = bpmnModelStartEvent.getOutgoingFlows();
        }
        if (outgoingFlows != null) {
            for (FlowElement outgoingFlow : outgoingFlows) {
                if (outgoingFlow instanceof UserTask) {
                    return (UserTask)outgoingFlow;
                }
                return getUserTask(((SequenceFlow)outgoingFlow).getTargetFlowElement());
            }
        }
        return null;
    }

    private static UserTask getUserTask(FlowElement outgoingFlow) {
        if (outgoingFlow instanceof UserTask) {
            return (UserTask)outgoingFlow;
        } else {
            SequenceFlow outgoingFlow1 = (SequenceFlow)outgoingFlow;
            FlowElement targetFlowElement = outgoingFlow1.getTargetFlowElement();
            return getUserTask(targetFlowElement);
        }
    }

    public static List<UserTask> getNextToUserTask(UserTask userTask) {
        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
        if (CollUtil.isEmpty(outgoingFlows)) {
            return ListUtil.empty();
        }
        ArrayList<UserTask> result = new ArrayList<>();
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            result.add(getUserTask(outgoingFlow));
        }
        return result;
    }

    /**
     * 比较 两个bpmnModel 是否相同
     *
     * @param oldModel 老的bpmn model
     * @param newModel 新的bpmn model
     */
    public static boolean equals(BpmnModel oldModel, BpmnModel newModel) {
        // 由于 BpmnModel 未提供 equals 方法，所以只能转成字节数组，进行比较
        return Arrays.equals(getBpmnBytes(oldModel), getBpmnBytes(newModel));
    }

    /**
     * 把 bpmnModel 转换成 byte[]
     *
     * @param model bpmnModel
     */
    public static byte[] getBpmnBytes(BpmnModel model) {
        if (model == null) {
            return new byte[0];
        }
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return converter.convertToXML(model);
    }

    // ========== Execution 相关的工具方法 ==========

    public static String formatCollectionVariable(String activityId) {
        return activityId + "_assignees";
    }

    public static String formatCollectionElementVariable(String activityId) {
        return activityId + "_assignee";
    }

    //  =========== BPM钻取方法 ===================

    public static List<UserTask> getFlow(BpmnModel bpmnModel, List<UserTask> targetFlowElement,
        Map<String, Object> variableMap) {
        List<UserTask> result = new ArrayList<>();
        for (FlowElement flowElement : targetFlowElement) {
            result.addAll(FlowableUtils.getFlow(bpmnModel, flowElement, variableMap));
        }
        return result;
    }

    public static List<UserTask> getFlow(BpmnModel bpmnModel, FlowElement flowElement,
        Map<String, Object> variableMap) {
        List<UserTask> result = new ArrayList<>();
        //下个是节点
        if (flowElement instanceof UserTask) {
            //传节点定义key获取当前节点
            FlowNode flowNode = (FlowNode)bpmnModel.getFlowElement(flowElement.getId());
            //输出连线
            List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
            if (outgoingFlows.size() < 1) {
                return result;
            }
            if (outgoingFlows.size() == 1) {
                FlowElement targetFlowElement = outgoingFlows.get(0).getTargetFlowElement();
                if (targetFlowElement instanceof ExclusiveGateway) {
                    result.add(getGateway(targetFlowElement, variableMap));
                }
                if (targetFlowElement instanceof UserTask) {
                    result.add((UserTask)targetFlowElement);
                }
                return result;
            }
            //遍历返回下一个节点信息
            // TODO 当节点返回的是多个时,代表是并行任务, 目前并不支持,可能会有问题
            for (SequenceFlow outgoingFlow : outgoingFlows) {
                //类型自己判断（获取下个节点是网关还是节点）
                FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
                if (targetFlowElement instanceof ExclusiveGateway) {
                    result.add(getGateway(targetFlowElement, variableMap));
                }
                if (targetFlowElement instanceof UserTask) {
                    result.add((UserTask)targetFlowElement);
                }
            }
        }
        return result;
    }

    private static UserTask getGateway(FlowElement targetFlowElement,
        Map<String, Object> variableMap) {
        if (targetFlowElement instanceof ExclusiveGateway) {
            String defaultFlow = ((ExclusiveGateway)targetFlowElement).getDefaultFlow();
            FlowElement nextFlowElement = getExclusiveGateway(targetFlowElement, variableMap, defaultFlow);
            if (nextFlowElement instanceof ExclusiveGateway) {
                getGateway(nextFlowElement, variableMap);
            }
            if (nextFlowElement instanceof UserTask) {
                return (UserTask)nextFlowElement;
            }
        }
        return null;
    }

    /**
     * 获取排他网关分支名称、分支表达式、下一级任务节点
     *
     * @param flowElement 任务节点
     * @param taskVarMap  审批数据
     */
    private static FlowElement getExclusiveGateway(FlowElement flowElement, Map<String, Object> taskVarMap,
        String defaultFlow) {
        // 获取所有网关分支
        List<SequenceFlow> targetFlows = ((ExclusiveGateway)flowElement).getOutgoingFlows();
        Boolean elExpressionFlag = Boolean.FALSE;
        FlowElement sequenceFlowResult = null;
        FlowElement defaultSequenceFlow = null;
        // 循环每个网关分支
        for (SequenceFlow sequenceFlow : targetFlows) {
            if (ObjectUtil.isEmpty(sequenceFlow)) {
                continue;
            }
            if (ObjectUtil.isEmpty(defaultFlow)) {
                throw exception(DEFAULT_FLOW_NULL);
            }
            if (defaultFlow.equals(sequenceFlow.getId())) {
                defaultSequenceFlow = sequenceFlow.getTargetFlowElement();
                continue;
            }
            elExpressionFlag = elExpression(sequenceFlow.getConditionExpression(), taskVarMap);
            if (BooleanUtil.isTrue(elExpressionFlag)) {
                // 获取下一个网关和节点数据
                FlowElement targetFlowElement = sequenceFlow.getTargetFlowElement();
                // 网关的下个节点是用户节点
                if (targetFlowElement instanceof UserTask) {
                    sequenceFlowResult = targetFlowElement;
                    break;
                } else if (targetFlowElement instanceof EndEvent) {
                    log.info("排他网关的下一节点是EndEvent: 结束节点");
                } else if (targetFlowElement instanceof ServiceTask) {
                    log.info("排他网关的下一节点是ServiceTask: 内部方法");
                } else if (targetFlowElement instanceof ExclusiveGateway) {
                    defaultFlow = ((ExclusiveGateway)targetFlowElement).getDefaultFlow();
                    return getExclusiveGateway(targetFlowElement, taskVarMap, defaultFlow);
                } else if (targetFlowElement instanceof SubProcess) {
                    log.info("排他网关的下一节点是SubProcess: 内部子流程");
                }
            }
        }
        // 判断是否有流程可走，如果没有，直接走默认路线
        if (BooleanUtil.isFalse(elExpressionFlag)) {
            if (defaultSequenceFlow instanceof UserTask) {
                sequenceFlowResult = defaultSequenceFlow;
            } else if (defaultSequenceFlow instanceof ExclusiveGateway) {
                defaultFlow = ((ExclusiveGateway)defaultSequenceFlow).getDefaultFlow();
                return getExclusiveGateway(defaultSequenceFlow, taskVarMap, defaultFlow);
            }
        }
        return sequenceFlowResult;
    }

    /**
     * 网关分叉条件判断，网关分叉，必须要有默认出口
     *
     * @param elExpression el表达式
     * @param variableMap  流程所有变量
     *
     * @return 返回true或false
     */
    public static Boolean elExpression(String elExpression, Map<String, Object> variableMap) {
        if (CollUtil.isEmpty(variableMap)) {
            return false;
        }
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        for (String k : variableMap.keySet()) {
            if (variableMap.get(k) != null) {
                Object varObj = variableMap.get(k);
                if ("day".equals(k)) {
                    context.setVariable(k, factory.createValueExpression(varObj, Double.class));
                } else {
                    context.setVariable(k, factory.createValueExpression(varObj, varObj.getClass()));
                }
            }
        }
        ValueExpression e = factory.createValueExpression(context, elExpression, Boolean.class);
        //el表达式和variables得到的结果
        try {
            return (Boolean)e.getValue(context);
        } catch (PropertyNotFoundException exception) {
            return false;
        }
    }
}
