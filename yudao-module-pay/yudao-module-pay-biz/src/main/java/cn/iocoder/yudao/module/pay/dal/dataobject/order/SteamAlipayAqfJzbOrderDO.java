package cn.iocoder.yudao.module.pay.dal.dataobject.order;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@TableName(value = "steam_alipay_aqf_jzb_order",autoResultMap = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SteamAlipayAqfJzbOrderDO {

    public SteamAlipayAqfJzbOrderDO(Integer createUserId,String outBizNo,String transAmount,
                                    String remark,String orderTitle,String timeExpire,String identity, String extInfo,Integer tenantId) throws ParseException {
        // 创建SimpleDateFormat对象并设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.createUserId=createUserId;
        this.outBizNo=outBizNo;
        this.transAmount=transAmount;
        this.remark=remark;
        this.orderTitle=orderTitle;
        this.timeExpire=sdf.parse(timeExpire);
        this.identity=identity;
        this.extInfo=extInfo;
        this.tenantId=tenantId;
    }

    /**
     * 自增主键
     */
    @TableId
    private Integer id;

    /**
     * 创建记账本充值人的id
     */
    private Integer createUserId;

    /**
     * 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯
     */
    private String outBizNo;

    /**
     * 充值总金额，单位为元，精确到小数点后两位，取值范围[0.01,9999999999999.99]
     */
    private String transAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 充值订单的标题
     */
    private String orderTitle;

    /**
     * 充值的超时时间
     */
    private Date timeExpire;

    /**
     * 收款方的记账本 id
     */
    private String identity;

    /**
     * 收款方的支付宝协议id
     */
    private String extInfo;

    /**
     * 当前充值状态默认 0，已完成 1
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 支付时间
     */
    private Date payDate;

    private Integer tenantId;

}