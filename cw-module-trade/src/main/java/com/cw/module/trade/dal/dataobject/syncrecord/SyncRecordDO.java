package com.cw.module.trade.dal.dataobject.syncrecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 账号同步记录 DO
 *
 * @author chengjiale
 */
@TableName("account_sync_record")
@KeySequence("account_sync_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 账户id
     */
    private Long accountId;
    /**
     * 同步类型
     */
    private String type;
    /**
     * 第三方数据
     */
    private String thirdData;

}
