package cn.iocoder.yudao.module.bpm.convert.definition;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleRespVO;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmTaskAssignRuleDO;
import org.flowable.bpmn.model.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface BpmTaskAssignRuleConvert {
    BpmTaskAssignRuleConvert INSTANCE = Mappers.getMapper(BpmTaskAssignRuleConvert.class);

    default List<BpmTaskAssignRuleRespVO> convertList(List<UserTask> tasks, List<BpmTaskAssignRuleDO> rules) {
        if (CollUtil.isEmpty(tasks)) {
            return new ArrayList<>();
        }
        Map<String, List<BpmTaskAssignRuleDO>> ruleMap = CollectionUtils.convertMultiMap(rules, BpmTaskAssignRuleDO::getTaskDefinitionKey);
        List<BpmTaskAssignRuleRespVO> list = new ArrayList<>();
        tasks.forEach(userTask -> {
            List<BpmTaskAssignRuleDO> bpmTaskAssignRuleDOS = ruleMap.get(userTask.getId());
            if (CollUtil.isEmpty(bpmTaskAssignRuleDOS)) {
                BpmTaskAssignRuleRespVO respVO = new BpmTaskAssignRuleRespVO();
                respVO.setTaskDefinitionKey(userTask.getId());
                respVO.setTaskDefinitionName(userTask.getName());
                list.add(respVO);
                return;
            }
            bpmTaskAssignRuleDOS.forEach(bpmTaskAssignRuleDO -> {
                BpmTaskAssignRuleRespVO respVO = convert(bpmTaskAssignRuleDO);
                respVO.setTaskDefinitionName(userTask.getName());
                list.add(respVO);
            });

        });
        return list;
    }

    BpmTaskAssignRuleRespVO convert(BpmTaskAssignRuleDO bean);

    BpmTaskAssignRuleDO convert(BpmTaskAssignRuleCreateReqVO bean);

    BpmTaskAssignRuleDO convert(BpmTaskAssignRuleUpdateReqVO bean);

    List<BpmTaskAssignRuleDO> convertList2(List<BpmTaskAssignRuleRespVO> list);
}
