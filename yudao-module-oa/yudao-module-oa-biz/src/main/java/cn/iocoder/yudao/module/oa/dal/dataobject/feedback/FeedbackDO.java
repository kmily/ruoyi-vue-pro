package cn.iocoder.yudao.module.oa.dal.dataobject.feedback;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品反馈 DO
 *
 * @author 管理员
 */
@TableName("oa_feedback")
@KeySequence("oa_feedback_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 反馈内容
     */
    private String feedbackContent;
    /**
     * 联系人
     */
    private String contactName;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 附件
     */
    private String appendFiles;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;

}
