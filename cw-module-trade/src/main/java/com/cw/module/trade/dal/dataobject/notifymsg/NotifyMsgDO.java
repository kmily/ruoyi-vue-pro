package com.cw.module.trade.dal.dataobject.notifymsg;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 账号通知记录 DO
 *
 * @author chengjiale
 */
@TableName("account_notify_msg")
@KeySequence("account_notify_msg_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyMsgDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 接受时间
     */
    private LocalDateTime acceptTime;
    /**
     * 接受内容
     */
    private String acceptInfo;
    /**
     * 关联交易账号
     */
    private Long tradeAccount;

}
