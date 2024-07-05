package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;


import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@TableName("hlgyy_tt_main_inserted_steps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TTMainInsertedStepDO extends BaseDO {

    public enum StatusEnum {
        DEFAULT(0),
        USED(1);
        private final int value;

        StatusEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @TableId
    private Long id;

    private Long userId;

    private Long treatmentInstanceId;

    private int sequence;

    private int status;

    private String message;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getMessageObj(){
        if(message == null || message.isEmpty())
            return new HashMap();
        try {
            JsonNode jsonNode =  objectMapper.readTree(message);
            return objectMapper.convertValue(jsonNode, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
