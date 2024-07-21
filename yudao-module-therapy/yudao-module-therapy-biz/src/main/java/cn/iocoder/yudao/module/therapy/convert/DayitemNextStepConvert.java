package cn.iocoder.yudao.module.therapy.convert;

import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemNextStepRespVO;

import java.util.Map;

public class DayitemNextStepConvert {
    public static DayitemNextStepRespVO convert(Map data, String userName) {
        DayitemNextStepRespVO vo = new DayitemNextStepRespVO();
        String stepType = (String) data.getOrDefault("step_type", "SYS_INFO");
        vo.setStep_type(stepType);
        vo.setStep_id((String) data.get("__step_id"));
        vo.setStep_name((String) data.get("__step_name"));
        if(stepType.equals("guide_language")){
            String content = (String) data.getOrDefault("content", "");
            data.put("content", content.replace("用户昵称", userName));
        }
        vo.setStep_data((Map<String, Object>) data.get("step_data"));
        vo.setStep_status((String) data.getOrDefault("status", "IN_PROGRESS"));
        return vo;
    }
}
