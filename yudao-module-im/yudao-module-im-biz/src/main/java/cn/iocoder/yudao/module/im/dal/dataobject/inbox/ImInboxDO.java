package cn.iocoder.yudao.module.im.dal.dataobject.inbox;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

// TODO 我们要不要改成 ImMessageQueue 队列？从理解上，概念上，可能都更清晰一点哈。每个用户一个消息队列；

/**
 * IM 收件箱 DO
 *
 * @author 芋道源码
 */
@TableName("im_inbox")
@KeySequence("im_inbox_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImInboxDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     * <p>
     * 关联 {@link ImMessageDO#getSenderId()}  或者 {@link ImMessageDO#getReceiverId()}
     */
    private Long userId;
    /**
     * 消息编号
     * <p>
     * 关联 {@link ImMessageDO#getId()}
     */
    private Long messageId;
    /**
     * 序号，按照 user 递增
     */
    private Long sequence;

}