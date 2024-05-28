package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("hlgyy_treatment_flow_days")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentFlowDayDO extends BaseDO {

    @TableId
    private Long id;

    /**
     * TreatmentFlowDO 的 id
     */
    private Long flowId;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否休息日
     */
    private boolean hasBreak;

    private Integer sequence;
}
