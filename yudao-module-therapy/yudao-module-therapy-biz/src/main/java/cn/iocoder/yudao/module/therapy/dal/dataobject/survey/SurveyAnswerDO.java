package cn.iocoder.yudao.module.therapy.dal.dataobject.survey;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 具体的一次回答
 */
@TableName(value = "hlgyy_servey_answer", autoResultMap = true)
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswerDO extends BaseDO {
    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * json格式报告
     */
    private String reprot;

    /**
     * 所属问卷
     */
    private Long belongSurveyId;

    /**
     * 问卷类型
     */
    private Integer surveyType;


    /**
     * 答题来源
     * 字典：survey_answer_source
     */
    private Integer source;

}
