package cn.iocoder.yudao.module.haoka.dal.dataobject.orders;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单 DO
 *
 * @author xiongxiong
 */
@TableName("haoka_orders")
@KeySequence("haoka_orders_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDO extends BaseDO {

    /**
     * 订单ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 生产商ID
     */
    private Long producerId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 供应商-商品名称
     */
    private String supplierProductName;
    /**
     * 供应商-商品编码SKU
     */
    private String supplierProductSku;
    /**
     * 外部订单编号
     */
    private String sourceId;
    /**
     * 分享ID
     */
    private Long shareId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 产品SKU
     */
    private String productSku;
    /**
     * 外部SKU
     */
    private String sourceSku;
    /**
     * 计划手机号
     */
    private String planMobile;
    /**
     * 生产手机号
     */
    private String planMobileProduced;
    /**
     * 证件姓名
     */
    private String idCardName;
    /**
     * 证件号码
     */
    private String idCardNum;
    /**
     * 地址省编码
     */
    private String addressProvinceCode;
    /**
     * 地址市编码
     */
    private String addressCityCode;
    /**
     * 地址区编码
     */
    private String addressDistrictCode;
    /**
     * 地址省
     */
    private String addressProvince;
    /**
     * 地址市
     */
    private String addressCity;
    /**
     * 地址区
     */
    private String addressDistrict;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 收件人电话
     */
    private String addressMobile;
    /**
     * 收件人姓名
     */
    private String addressName;
    /**
     * 物流公司ID
     */
    private Long trackingCompanyId;
    /**
     * 物流单号
     */
    private String trackingNumber;
    /**
     * 买家备注
     */
    private String buyerMemo;
    /**
     * 卖家备注
     */
    private String sellerMemo;
    /**
     * 生产备注
     */
    private String producerMemo;
    /**
     * 订单状态码
     */
    private Long status;
    /**
     * 标志
     */
    private Long flag;
    /**
     * 预警区域
     */
    private String warnArea;
    /**
     * 原因
     */
    private String reason;
    /**
     * 订单来源
     */
    private String source;
    /**
     * 用户下单时间
     */
    private LocalDateTime orderedAt;
    /**
     * 生产时间
     */
    private LocalDateTime producedAt;
    /**
     * 发货时间
     */
    private LocalDateTime deliveredAt;
    /**
     * 激活时间
     */
    private LocalDateTime activatedAt;
    /**
     * 充值时间
     */
    private LocalDateTime rechargedAt;
    /**
     * 卖家备注
     */
    private String memo;
    /**
     * 数量
     */
    private String amount;
    /**
     * 状态变更时间
     */
    private LocalDateTime statusUpdatedAt;
    /**
     * 退款状态
     */
    private String refundStatus;
    /**
     * 激活状态
     */
    private String activeStatus;
    /**
     * ICCID
     */
    private String iccid;
    /**
     * 真实外部订单编号
     */
    private String realSourceId;
    /**
     * 图片大小
     */
    private Long picSize;
    /**
     * 归属地省
     */
    private String regionP;
    /**
     * 归属地市
     */
    private String regionC;
    /**
     * 分销商名称
     */
    private String merchantName;
    /**
     * 上游状态
     */
    private String upStatus;
    /**
     * 上游订单号
     */
    private String upstreamOrderId;
    /**
     * 镇/乡
     */
    private String town;
    /**
     * 物流公司名称
     */
    private String trackingCompany;
    /**
     * 订单状态名称
     */
    private String statusName;
    /**
     * 加密收货电话
     */
    private String encryptAddressMobile;
    /**
     * 加密收货人姓名
     */
    private String encryptAddressName;
    /**
     * 加密证件姓名
     */
    private String encryptIdCardName;
    /**
     * 加密证件号码
     */
    private String encryptIdCardNum;
    /**
     * 加密详细地址
     */
    private String encryptAddress;

}