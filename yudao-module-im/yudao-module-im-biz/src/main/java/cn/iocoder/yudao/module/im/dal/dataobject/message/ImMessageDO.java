package cn.iocoder.yudao.module.im.dal.dataobject.message;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageContentTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageSourceEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageStatusEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * IM 消息 DO
 *
 * @author 芋道源码
 */
@TableName("im_message")
@KeySequence("im_message_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImMessageDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 客户端消息编号 uuid，用于排重
     */
    private String clientMessageId;
    /**
     * 发送人编号 存储的是用户编号
     */
    private Long senderId;
    /**
     * 接收人编号
     * <p>
     * 1. 单聊时，用户编号；群聊时，群编号
     */
    private Long receiverId;
    /**
     * 消息发送者昵称
     * <p>
     * 冗余 AdminUserDO 的 nickname 字段
     */
    private String senderNickname;
    /**
     * 消息发送者头像
     * <p>
     * 冗余 AdminUserDO 的 avatar 字段
     */
    private String senderAvatar;
    /**
     * 会话类型 枚举 {@link ImConversationTypeEnum}
     */
    private Integer conversationType;
    /**
     * 会话标志
     * <p>
     * 生成规则：{@link ImConversationTypeEnum#generateConversationNo(Long, Long, Integer)} 方法
     */
    private String conversationNo;
    /**
     * 消息类型
     * <p>
     * 枚举 {@link ImMessageContentTypeEnum}
     */
    private Integer contentType;
    /**
     * 消息内容
     * <p>
     * JSON 格式 对应 dal/dataobject/message/content 包
     */
    private String content;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    /**
     * 消息来源
     * <p>
     * 枚举 {@link ImMessageSourceEnum}
     */
    private Integer sendFrom;
    /**
     * 消息状态
     * <p>
     * 枚举 {@link ImMessageStatusEnum}
     */
    private Integer messageStatus;

}