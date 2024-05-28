package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.common.JsonFieldAccessible;
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

@TableName("hlgyy_treatment_flow_dayitem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentFlowDayitemDO extends BaseDO implements JsonFieldAccessible {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @TableId
    private Long id;

    private Long dayId;

    /**
     * 方案id
     */
    private Long flowId;

    private String remark;

    /**
     * 废弃
     */
    private String itemType;

    /**
     * 任务类型
     * 枚举 {@link cn.iocoder.boot.module.therapy.enums.SurveyType}
     */
    private String type;

    private String settings;

    private String dependentItemIds;

    private Long agroup;

    private Long groupSeq;

    private String groupSettings;

    private boolean required;

    private String taskFlowId;

    public Map getSettingsObj() {
        if(settings == null || settings.isEmpty())
            return new HashMap();
        try {
            JsonNode jsonNode =  objectMapper.readTree(settings);
            return objectMapper.convertValue(jsonNode, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSettingsObj(Map settings) {
        this.settings = objectMapper.valueToTree(settings).toString();
    }

}