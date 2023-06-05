package com.cw.module.trade.dal.dataobject.account;

import lombok.*;
import java.util.*;

import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tb.utils.json.JsonUtil;

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
    
    /** 每日凌晨余额，为了计算每日亏损是否超标 */
    private String zeroBalance;
    
    public BigDecimal formatTypeBalance(String asset) {
        BigDecimal assetBalance = new BigDecimal(0);
        if(Strings.isBlank(asset) || Strings.isBlank(this.balance)) {
            return assetBalance;
        }
        ArrayNode notifyBalance = JsonUtil.string2ArrayNode(this.balance);
        for(JsonNode balance :  notifyBalance) {
            if(asset.equals(balance.get("asset").asText())) {
                return new BigDecimal(balance.get("balance").asText());
            }
        }
        return assetBalance;
    }
    
    public BigDecimal formatTypeAvailableBalance(String asset) {
        BigDecimal assetBalance = new BigDecimal(0);
        if(Strings.isBlank(asset) || Strings.isBlank(this.balance)) {
            return assetBalance;
        }
        ArrayNode notifyBalance = JsonUtil.string2ArrayNode(this.balance);
        for(JsonNode balance :  notifyBalance) {
            if(asset.equals(balance.get("asset").asText())) {
                return new BigDecimal(balance.get("withdrawAvailable").asText());
            }
        }
        return assetBalance;
    }
    
}
