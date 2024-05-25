package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@TableName("hlgyy_treatment_day_instance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentDayInstanceDO extends BaseDO {

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

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 治疗流程实例ID
     */
    private Long flow_instance_id;

    /**
     * 治疗流程Day ID
     */
    private Long day_id;

    /**
     * 状态
     */
    private int status;
}
