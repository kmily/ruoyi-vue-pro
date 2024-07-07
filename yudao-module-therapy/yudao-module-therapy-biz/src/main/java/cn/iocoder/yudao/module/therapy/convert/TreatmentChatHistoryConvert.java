package cn.iocoder.yudao.module.therapy.convert;

import cn.iocoder.boot.module.therapy.enums.TaskType;
import cn.iocoder.yudao.module.therapy.controller.app.vo.TreatmentHistoryChatMessageVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentChatHistoryDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.TreatmentDayitemInstanceDO;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreatmentChatHistoryConvert {
    public static List<TreatmentHistoryChatMessageVO> convert(List<TreatmentChatHistoryDO> list) {
        List<TreatmentHistoryChatMessageVO> result = new ArrayList<>();
        for (TreatmentChatHistoryDO item : list) {
            TreatmentHistoryChatMessageVO vo = new TreatmentHistoryChatMessageVO();
            vo.setMessage(item.getMessage());
            vo.setIsSystem(item.isSystem());
            vo.setCreateTime(item.getCreateTime());
            vo.setSource(item.getSource());
            result.add(vo);
        }
        return result;
    }
}
