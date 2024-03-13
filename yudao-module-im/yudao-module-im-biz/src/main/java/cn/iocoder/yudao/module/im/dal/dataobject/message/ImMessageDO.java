package cn.iocoder.yudao.module.im.dal.dataobject.message;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 消息 DO
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
     * 发送人编号
     */
    private Long senderId;
    /**
     * 接收人编号
     */
    private Long receiverId;
    /**
     * 发送人昵称
     */
    private String senderNickname;
    /**
     * 发送人头像
     */
    private String senderAvatar;
    /**
     * 会话类型
     */
    private Integer conversationType;
    /**
     * 会话标志
     */
    private String conversationNo;
    /**
     * 消息类型
     */
    private Integer contentType;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    /**
     * 消息来源 100-用户发送；200-系统发送（一般是通知）；
     */
    private Integer sendFrom;

    /**
     * 消息状态 1 发送中、2 发送成功、3 发送失败、4 已删除、5 已撤回
     */
    private Integer messageStatus;

}