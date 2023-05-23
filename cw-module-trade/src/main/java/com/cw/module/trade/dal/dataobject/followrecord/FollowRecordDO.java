package com.cw.module.trade.dal.dataobject.followrecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 账号跟随记录 DO
 *
 * @author chengjiale
 */
@TableName("account_follow_record")
@KeySequence("account_follow_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 账号通知信息
     */
    private Long accountNotifyId;
    /**
     * 跟随账号
     */
    private Long followAccount;
    /**
     * 操作账号
     */
    private Long operateAccount;
    /**
     * 操作时间
     */
    private LocalDateTime operateTime;
    /**
     * 操作内容
     */
    private String operateInfo;
    /**
     * 操作结果
     */
    private Boolean operateSuccess;
    /**
     * 操作结果响应数据
     */
    private String operateResult;
    /**
     * 跟单操作描述
     */
    private String operateDesc;

}
