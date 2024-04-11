package cn.iocoder.yudao.module.pay.dal.dataobject.sign;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@TableName("steam_alipay_isv")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SteamAlipayIsvDO {


    @TableId
    /**
     * 主键ID
     */
    private Long id;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 租户编号
     */
    private Long tenantId;

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
     * 产品签约状态对象
     */
    private String signStatusList;

    /**
     * NONE：未签约，表示还没有签约该产品
     */
    private String signStatus;

    /**
     * 代扣协议中标示用户的唯一签约号(确保在商户系统中 唯一)。 格式规则:支持大写小写字母和数字，最长 32 位。
     */
    private String externalAgreementNo;

    /**
     * 支付宝系统中用以唯一标识用户签约记录的编号（用户签约成功后的协议号 ） ，如果传了该参数，其他参数会被忽略
     */
    private String agreementNo;

    /**
     * isv签约成功之后的跳转页面，商户网页页面
     */
    private String isvReturnUrl;
}