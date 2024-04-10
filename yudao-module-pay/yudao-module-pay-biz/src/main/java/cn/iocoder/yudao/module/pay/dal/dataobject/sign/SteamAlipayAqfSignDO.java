package cn.iocoder.yudao.module.pay.dal.dataobject.sign;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@TableName("steam_alipay_aqf_sign")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SteamAlipayAqfSignDO {

    public SteamAlipayAqfSignDO(Integer createUserId,Integer tenantId,String externalAgreementNo){
        this.createUserId=createUserId;
        this.tenantId=tenantId;
        this.externalAgreementNo=externalAgreementNo;
    }

    public SteamAlipayAqfSignDO(Integer isAccountbook,String accountBookId){
        this.isAccountbook=isAccountbook;
        this.accountBookId=accountBookId;
    }

    public SteamAlipayAqfSignDO(String status,String signTime,String validTime,String alipayLogonId,String invalidTime,String agreementNo) throws ParseException {
        // 创建SimpleDateFormat对象并设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.status=status;
        this.signTime=sdf.parse(signTime);
        this.validTime=sdf.parse(validTime);
        this.alipayLogonId=alipayLogonId;
        this.invalidTime=sdf.parse(invalidTime);
        this.agreementNo=agreementNo;
    }

    @TableId
    /**
     * 主键
     */
    private Integer id;

    /**
     * 创建签约的用户id
     */
    private Integer createUserId;

    /**
     * 发起签约的时间
     */
    private Date createTime;

    /**
     * 签约状态(默认值:1:暂存,2:正常,3:停止)
     */
    private String status;

    /**
     * 签约成功的时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    /**
     * 用户代扣协议的实际生效时间
     */
    private Date validTime;

    /**
     * 签约脱敏的支付宝账号
     */
    private String alipayLogonId;

    /**
     * 协议失效时间，格式为 yyyy-MM-dd HH:mm:ss
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invalidTime;

    /**
     * 用户签约成功后的协议号
     */
    private String agreementNo;

    /**
     * 商户签约号，协议中标示用户的唯一签约号
     */
    private String externalAgreementNo;

    /**
     * 租户id
     * @return
     */
    private Integer tenantId;

    /**
     * 是否开通记账本
     */
    private Integer isAccountbook;

    /**
     * 记账本id
     */
    private String accountBookId;

}