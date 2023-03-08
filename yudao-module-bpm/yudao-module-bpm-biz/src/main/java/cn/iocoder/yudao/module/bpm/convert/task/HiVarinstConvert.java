package cn.iocoder.yudao.module.bpm.convert.task;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.module.bpm.dal.dataobject.task.HiVarinstDO;
import org.flowable.engine.runtime.Execution;
import org.flowable.variable.service.impl.persistence.entity.HistoricVariableInstanceEntityImpl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Convert
 *
 * @author 芋道
 */
@Mapper
public interface HiVarinstConvert {

    HiVarinstConvert INSTANCE = Mappers.getMapper(HiVarinstConvert.class);

    default HiVarinstDO nrOfActiveInstancesConvert(HistoricVariableInstanceEntityImpl bean,
        List<Execution> executionList) {
        HiVarinstDO convert = convert(bean);
        convert.setVarType("integer");
        convert.setLongValue((long)executionList.size());
        convert.setText(String.valueOf(executionList.size()));
        convert.setText2(null);
        return convert;
    }

    default HiVarinstDO nrOfCompletedInstancesConvert(HistoricVariableInstanceEntityImpl bean,
        HistoricVariableInstanceEntityImpl nrOfInstances, List<Execution> executionList) {
        HiVarinstDO convert = convert(bean);
        convert.setVarType("integer");
        convert.setLongValue(nrOfInstances.getLongValue() - executionList.size());
        convert.setText(String.valueOf(nrOfInstances.getLongValue() - executionList.size()));
        convert.setText2(null);
        return convert;
    }

    default HiVarinstDO convert(HistoricVariableInstanceEntityImpl bean) {
        if (bean == null) {
            return null;
        }

        HiVarinstDO hiVarinstDO = new HiVarinstDO();

        if (ObjectUtil.isNotEmpty(bean.getId())) {
            hiVarinstDO.setId(bean.getId());
        }
        if (ObjectUtil.isNotEmpty(bean.getRevision())) {
            hiVarinstDO.setRev(bean.getRevision() + 1);
        }
        if (ObjectUtil.isNotEmpty(bean.getProcessInstanceId())) {
            hiVarinstDO.setProcInstId(bean.getProcessInstanceId());
        }
        if (ObjectUtil.isNotEmpty(bean.getExecutionId())) {
            hiVarinstDO.setExecutionId(bean.getExecutionId());
        }
        if (ObjectUtil.isNotEmpty(bean.getTaskId())) {
            hiVarinstDO.setTaskId(bean.getTaskId());
        }
        if (ObjectUtil.isNotEmpty(bean.getName())) {
            hiVarinstDO.setName(bean.getName());
        }
        if (ObjectUtil.isNotEmpty(bean.getVariableType())) {
            hiVarinstDO.setVarType(bean.getVariableType().getTypeName());
        }
        if (ObjectUtil.isNotEmpty(bean.getScopeId())) {
            hiVarinstDO.setScopeId(bean.getScopeId());
        }
        if (ObjectUtil.isNotEmpty(bean.getSubScopeId())) {
            hiVarinstDO.setSubScopeId(bean.getSubScopeId());
        }
        if (ObjectUtil.isNotEmpty(bean.getScopeType())) {
            hiVarinstDO.setScopeType(bean.getScopeType());
        }
        if (ObjectUtil.isNotEmpty(bean.getByteArrayRef())) {
            hiVarinstDO.setBytearrayId(bean.getByteArrayRef().getId());
        }
        if (ObjectUtil.isNotEmpty(bean.getDoubleValue())) {
            hiVarinstDO.setDoubleValue(bean.getDoubleValue());
        }
        if (ObjectUtil.isNotEmpty(bean.getLongValue())) {
            hiVarinstDO.setLongValue(bean.getLongValue());
        }
        if (ObjectUtil.isNotEmpty(bean.getTextValue())) {
            hiVarinstDO.setText(bean.getTextValue());
        }
        if (ObjectUtil.isNotEmpty(bean.getTextValue2())) {
            hiVarinstDO.setText2(bean.getTextValue2());
        }
        hiVarinstDO.setCreateTime(bean.getCreateTime());
        hiVarinstDO.setLastUpdatedTime(bean.getLastUpdatedTime());

        return hiVarinstDO;
    }

}
