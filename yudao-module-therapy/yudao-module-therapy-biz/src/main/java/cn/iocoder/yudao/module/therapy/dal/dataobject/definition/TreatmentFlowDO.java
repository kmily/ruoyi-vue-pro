package cn.iocoder.yudao.module.therapy.dal.dataobject.definition;
import cn.iocoder.boot.module.therapy.enums.FlowType;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.definition.common.JsonFieldAccessible;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 治疗流程 DO
 *
 * @author mayuchao
 */
@TableName("hlgyy_treatment_flow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentFlowDO extends BaseDO  implements JsonFieldAccessible {

    /**
     * 治疗流程ID
     */
    @TableId
    private Long id;
    /**
     * 治疗流程名称
     */
    private String name;
    /**
     * 治疗流程编码
     */
    private String code;

    /**
     * 发布状态
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;

    /**
     * 方案类型
     * {@link cn.iocoder.boot.module.therapy.enums.FlowType}
     */
    private Integer type;

    private String remark;
}