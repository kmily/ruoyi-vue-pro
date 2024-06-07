package cn.iocoder.yudao.module.therapy.convert;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemNextStepRespVO;

import java.util.Map;

public class DayitemNextStepConvert {
    public static DayitemNextStepRespVO convert(Map data) {
        DayitemNextStepRespVO vo = new DayitemNextStepRespVO();
        vo.setStep_type((String) data.getOrDefault("step_type", "SYS_INFO"));
        vo.setStep_id((String) data.get("__step_id"));
        vo.setStep_name((String) data.get("__step_name"));
        vo.setStep_data((Map<String, Object>) data.get("step_data"));
        vo.setStep_status((String) data.getOrDefault("status", "IN_PROGRESS"));
        return vo;
    }
}
