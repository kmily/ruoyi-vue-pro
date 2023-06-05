package com.cw.module.trade.dal.dataobject.followrecord;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
     * 第三方订单id
     */
    private Long thirdOrderId;
    /**
     * 跟随第三方订单id
     */
    private Long followThridOrderId;
    /**
     * 操作账号
     */
    private Long operateAccount;
    /**
     * 操作时间
     */
    private Date operateTime;
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
