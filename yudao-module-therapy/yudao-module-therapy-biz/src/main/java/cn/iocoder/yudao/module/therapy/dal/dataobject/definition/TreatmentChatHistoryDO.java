package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("hlgyy_treatment_chat_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentChatHistoryDO {

    @TableId
    private Long id;

    private String message;

    private Long userId;

    private Long TreatmentInstanceId;

    private boolean isSystem;
}
