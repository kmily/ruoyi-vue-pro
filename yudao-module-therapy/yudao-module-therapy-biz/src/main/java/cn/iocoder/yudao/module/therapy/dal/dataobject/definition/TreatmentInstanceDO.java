package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 治疗流程实例 DO
 *
 * @author mayuchao
 */
@TableName("hlgyy_treatment_instance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentInstanceDO extends BaseDO {
    public enum TreatmentStatus {
        INITIATED(0),
        IN_PROGRESS(1),
        COMPLETED(2),
        CANCELLED(3);

        private final int value;

        TreatmentStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static TreatmentStatus fromValue(int value) {
            for (TreatmentStatus status : TreatmentStatus.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid value for TreatmentStatus: " + value);
        }
    }
    /**
     * 治疗流程ID
     */
    @TableId
    private Long id;
    private Long user_id;
    private Long flow_id;
    private int status;

}