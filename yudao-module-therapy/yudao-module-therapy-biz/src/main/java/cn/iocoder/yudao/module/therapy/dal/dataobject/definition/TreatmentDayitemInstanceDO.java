package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@TableName("hlgyy_treatment_dayitem_instance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentDayitemInstanceDO extends BaseDO {

    public enum StatusEnum {
        INITIATED(0),
        IN_PROGRESS(1),
        COMPLETED(2),
        CANCELLED(3);

        private final int value;

        StatusEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static StatusEnum fromValue(int value) {
            for (StatusEnum status : StatusEnum.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid value for TreatmentStatus: " + value);
        }
    }

    @TableId
    private Long id;

    private Long flowInstanceId;

    private Long dayInstanceId;

    private Long dayitemId;

    private int status;

    private Long userId;

    private boolean required;

    private String taskInstanceId;

    private String extAttr;

    public JSONObject getExtAttrObj() {
        return JSON.parseObject(extAttr);
    }

    void setExtAttrObj(JSONObject extAttr) {
        this.extAttr = extAttr.toJSONString();
    }

    public void updateExtAttr(String key, Object val) {
        JSONObject jsonObject = getExtAttrObj();
        if(jsonObject== null){
            jsonObject = new JSONObject();
        }
        jsonObject.put(key, val);
        setExtAttrObj(jsonObject);
    }


}
