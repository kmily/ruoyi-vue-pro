package cn.iocoder.yudao.module.member.dal.dataobject.noticeuser;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户消息关联 DO
 *
 * @author 和尘同光
 */
@TableName("member_notice_user")
@KeySequence("member_notice_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeUserDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 消息ID
     */
    private Long noticeId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 阅读时间
     */
    private LocalDateTime readTime;
    /**
     * 阅读状态0-未读1-已读
     */
    private Byte status;

}
