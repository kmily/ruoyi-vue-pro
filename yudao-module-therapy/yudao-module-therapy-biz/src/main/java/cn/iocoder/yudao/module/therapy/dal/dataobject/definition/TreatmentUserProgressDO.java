package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("hlgyy_treatment_user_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentUserProgressDO {

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

        public static TreatmentDayitemInstanceDO.StatusEnum fromValue(int value) {
            for (TreatmentDayitemInstanceDO.StatusEnum status : TreatmentDayitemInstanceDO.StatusEnum.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid value for TreatmentStatus: " + value);
        }
    }

    @TableId
    private Long id;

    private Long user_id;

    private Long day_instance_id;

    private int group_seq;
}
