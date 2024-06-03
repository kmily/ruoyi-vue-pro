package cn.iocoder.yudao.module.therapy.taskflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;


public class GAMFlow extends BaseFlow{
    @Override
    public Map run() {
        return null;
    }

    public String deploy(Long id, Map<String, Object> settings) {
        // read settings from resources/goal_and_motivation_settings.json
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getResourceAsStream("/goal_and_motivation_settings.json")) {
            settings = objectMapper.readValue(inputStream, Map.class);
            // use settings
        } catch (IOException e) {
            // handle exception
        }
        createBPMNModel(id, settings);
        return getProcessName(id);
    }


    @Override
    public String getProcessName(Long id) {
        return "GOAL_AND_MOTIVATION-"  + String.valueOf(id);
    }

    public Map<String, Object> auto_primary_troubles_qst(Map data){
        // TODO init question instance
        data.put("instance_id", 123);
        return data;
    }

    public void submit_primary_troubles_qst(Map data){
        // TODO submit question instance
    }
}
