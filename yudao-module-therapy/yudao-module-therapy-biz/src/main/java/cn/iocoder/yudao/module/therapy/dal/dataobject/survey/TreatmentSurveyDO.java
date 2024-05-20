package cn.iocoder.yudao.module.therapy.dal.dataobject.survey;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 治疗问卷
 */
@TableName(value = "hlgyy_treatment_survey", autoResultMap = true)
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentSurveyDO extends TenantBaseDO {

    /**
     * 调查ID
     */
    @TableId
    private Long id;

    /**
     * 调查标题
     */
    private String title;

    /**
     * 类型
     * 枚举 {@link SurveyType}
     */
    private String surveyType;

    /**
     * 是否禁用
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer disabled;
}
