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
        NOT_STARTED(0),
        INITIATED(1),
        IN_PROGRESS(2),
        COMPLETED(3),
        CANCELLED(4);

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
    private Long userId;
    private Long flowId;
    private int status;

}