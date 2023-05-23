package com.cw.module.trade.dal.dataobject.account;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 交易账号 DO
 *
 * @author chengjiale
 */
@TableName("trade_account")
@KeySequence("trade_account_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 账号名称
     */
    private String name;
    /**
     * api访问key
     */
    private String appKey;
    /**
     * api访问秘钥
     */
    private String appSecret;
    /**
     * 余额（第三方查询返回信息）
     */
    private String balance;
    /**
     * 最后一次余额查询时间(时间戳)
     */
    private Long lastBalanceQueryTime;
    /**
     * 账号管理用户ID
     */
    private Long relateUser;

    /** 关联配置 */
    private String followCfg;
    
    /** 关联账号 */
    private Long followAccount;
}
