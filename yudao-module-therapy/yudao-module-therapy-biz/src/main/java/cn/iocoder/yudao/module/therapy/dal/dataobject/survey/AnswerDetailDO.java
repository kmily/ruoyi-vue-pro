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
import lombok.*;

/**
 * 问卷的题目
 */
@TableName(value = "hlgyy_answer_detail", autoResultMap = true)
@KeySequence("member_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDetailDO extends BaseDO {
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
     * 所属问卷编码
     */
    private String belongSurveyCode;

    /**
     *对应SurveyAnswerDO::ID
     */
    private Long answerId;

    /**
     * 所属问卷的题目
     */
    private Long qstId;
    /**
     * 所属题目编码
     */
    private String belongQstCode;

    /**
     * 回答的内容,json格式
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONObject answer;

    /**
     * 题干json化数据
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONObject qstContext;

    /**
     * 问题类型
     * 枚举 {@link SurveyQuestionType}
     */
    private Integer qstType;

}
