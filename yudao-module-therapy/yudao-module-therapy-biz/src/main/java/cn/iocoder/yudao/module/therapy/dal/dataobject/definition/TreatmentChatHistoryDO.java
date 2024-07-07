package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;


import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@TableName("hlgyy_treatment_chat_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentChatHistoryDO extends BaseDO {
    public static String SOURCE_SYSTEM = "system";
    public static String SOURCE_USER = "user";
    public static String SOURCE_USER_SUBMIT = "user_submit";


    public static final Long MAIN_TREATMENT_DAYITEM_INSTANCE_ID = 0L; // 主治疗日程项实例，用0表示

    @TableId
    private Long id;

    private String message;

    private Long userId;

    private Long treatmentInstanceId;

    private boolean isSystem;

    private Long treatmentDayitemInstanceId;

    private String source; // system, user, user_sumbit

    public Map getMessageObj() {
        return JSON.parseObject(message, Map.class);
    }
}
