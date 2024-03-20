package cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 签约ISV用户 DO
 *
 * @author 管理员
 */
@TableName("steam_alipay_isv")
@KeySequence("steam_alipay_isv_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlipayIsvDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long systemUserId;
    /**
     * 用户类型
     */
    private Integer systemUserType;
    /**
     * isv业务系统的申请单id
     */
    private String isvBizId;
    /**
     * 协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。
     */
    private String appAuthToken;
    /**
     * 支付宝分配给开发者的应用Id
     */
    private String appId;
    /**
     * 支付宝分配给商户的应用Id
     */
    private String authAppId;
    /**
     * 被授权用户id
     */
    private String userId;
    /**
     * 商家支付宝账号对应的ID，2088开头
     */
    private String merchantPid;
    /**
     * NONE：未签约，表示还没有签约该产品
     */
    private String signStatus;
    /**
     * 产品签约状态对象
     */
    private String signStatusList;

}