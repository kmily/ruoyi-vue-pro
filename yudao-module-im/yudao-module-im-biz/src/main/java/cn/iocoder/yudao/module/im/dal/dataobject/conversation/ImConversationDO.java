package cn.iocoder.yudao.module.im.dal.dataobject.conversation;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 会话 DO
 *
 * @author 芋道源码
 */
@TableName("im_conversation")
@KeySequence("im_conversation_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImConversationDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 所属用户
     */
    private Long userId;
    /**
     * 类型：1 单聊；2 群聊；4 通知会话（预留）
     */
    private Integer conversationType;
    /**
     * 单聊时，用户编号；群聊时，群编号
     */
    private String targetId;
    /**
     * 会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId
     */
    private String no;
    /**
     * 是否置顶 0否 1是
     */
    private Boolean pinned;
    /**
     * 最后已读时间
     */
    private LocalDateTime lastReadTime;

}