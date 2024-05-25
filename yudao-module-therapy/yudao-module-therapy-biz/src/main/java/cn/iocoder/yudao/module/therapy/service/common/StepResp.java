package cn.iocoder.yudao.module.therapy.service.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StepResp {
    private TreatmentStepItem step;
    private boolean noMoreStepsToday = false;
    private boolean isPastDayStep = false;

    public Map<String, Object> getData(){
        HashMap<String, Object> data = new HashMap<>();
        //TODO Bug
        data.put("noMoreStepsToday", noMoreStepsToday);
        data.put("isPastDayStep", isPastDayStep);
        return data;
    }
}
