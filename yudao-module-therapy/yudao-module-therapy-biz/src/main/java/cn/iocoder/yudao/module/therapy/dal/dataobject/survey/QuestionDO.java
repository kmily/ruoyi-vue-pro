package cn.iocoder.yudao.module.therapy.dal.dataobject.survey;

import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyQuestionType;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 问卷的题目
 */
@TableName(value = "hlgyy_survey_question", autoResultMap = true)
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDO extends BaseDO {
    /**
     * 题目ID
     */
    @TableId
    private Long id;

    /**
     * 编码
     */
    private String code;
    /**
     * 所属问卷
     */
    private Long belongSurveyId;

    /**
     * 所属问卷编码
     */
    private String belongSurveyCode;

    /**
     * 是否必答题
     */
    private boolean required;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 问题类型
     * 枚举 {@link SurveyQuestionType}
     */
    private Integer qstType;

    /**
     * 题干json化数据
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONObject qstContext;
}
