package cn.iocoder.yudao.module.system.dal.dataobject.definition;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 治疗流程实例 DO
 *
 * @author mayuchao
 */
@TableName("hlgyy_treatment_flow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentInstanceDO extends BaseDO {

    /**
     * 治疗流程ID
     */
    @TableId
    private Long id;
    private Long user_id;
    private Long flow_id;
}