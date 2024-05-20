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
@TableName(value = "hlgyy_treatment_anAnswer", autoResultMap = true)
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnAnswer extends TenantBaseDO {
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
     * 所属问卷的题目
     */
    private Long qstId;

    /**
     * 回答的内容,json格式
     */
    private String answer;

    /**
     * 问题类型
     * 枚举 {@link SurveyQuestionType}
     */
    private Integer qstType;

}
