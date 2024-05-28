package cn.iocoder.yudao.module.therapy.convert;

import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.therapy.controller.admin.flow.vo.TreatmentFlowRespVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentFlowDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface TreatmentFlowConvert {
    TreatmentFlowConvert INSTANCE = Mappers.getMapper(TreatmentFlowConvert.class);

    default List<TreatmentFlowRespVO> convertList(List<TreatmentFlowDO> list, Map<Long, AdminUserRespDTO> userMap) {
        List<TreatmentFlowRespVO> respVOS = new ArrayList<>();
        for (TreatmentFlowDO item : list) {
            TreatmentFlowRespVO res = convert(item);
            AdminUserRespDTO temp = userMap.get(Long.parseLong(item.getCreator()));
            res.setCreatorName(temp.getNickname());
            respVOS.add(res);
        }
        return respVOS;
    }

    TreatmentFlowRespVO convert(TreatmentFlowDO flowDO);

}
