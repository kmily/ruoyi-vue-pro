package cn.iocoder.yudao.module.oa.dal.dataobject.feedbackfollowlog;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 反馈跟进日志 DO
 *
 * @author 管理员
 */
@TableName("oa_feedback_follow_log")
@KeySequence("oa_feedback_follow_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackFollowLogDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 产品反馈id
     */
    private Long feedbackId;
    /**
     * 跟进日志内容
     */
    private String logContent;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 更新者
     */
    private String updater;

}
