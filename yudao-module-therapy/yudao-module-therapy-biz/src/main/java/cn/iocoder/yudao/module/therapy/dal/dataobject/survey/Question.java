package cn.iocoder.yudao.module.therapy.dal.dataobject.survey;

import cn.iocoder.boot.module.therapy.enums.SurveyQuestionType;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 问卷的题目
 */
@TableName(value = "hlgyy_treatment_question", autoResultMap = true)
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends TenantBaseDO {
    /**
     * 题目ID
     */
    @TableId
    private Long id;

    /**
     * 所属问卷
     */
    private Long belongSurveyId;

    /**
     * 问题标题
     */
    private String qstTitle;

    /**
     * 问题描述
     */
    private String qstDesc;

    /**
     * 问题类型
     * 枚举 {@link SurveyQuestionType}
     */
    private Integer qstType;

    /**
     * 题干json化数据
     */
    private String qstContext;
}
