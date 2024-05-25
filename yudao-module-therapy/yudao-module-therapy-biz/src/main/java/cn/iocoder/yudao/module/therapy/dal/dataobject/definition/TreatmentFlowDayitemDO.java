package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.common.JsonFieldAccessible;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("hlgyy_treatment_flow_dayitem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentFlowDayitemDO extends BaseDO implements JsonFieldAccessible {

    @TableId
    private Long id;

    private Long day_id;

    private String remark;

    private String item_type;

    private String dependent_item_ids;

    private Long group;

    private String group_settings;

    private String required;

}