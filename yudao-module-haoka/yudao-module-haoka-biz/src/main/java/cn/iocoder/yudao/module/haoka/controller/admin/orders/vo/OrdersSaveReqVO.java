package cn.iocoder.yudao.module.haoka.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 订单新增/修改 Request VO")
@Data
public class OrdersSaveReqVO {

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12955")
    private Long id;

    @Schema(description = "生产商ID", example = "15873")
    private Long producerId;

    @Schema(description = "产品ID", example = "14115")
    private Long productId;

    @Schema(description = "供应商-商品名称", example = "芋艿")
    private String supplierProductName;

    @Schema(description = "供应商-商品编码SKU")
    private String supplierProductSku;

    @Schema(description = "外部订单编号", example = "1396")
    private String sourceId;

    @Schema(description = "分享ID", example = "26263")
    private Long shareId;

    @Schema(description = "用户ID", example = "31769")
    private Long userId;

    @Schema(description = "产品SKU")
    private String productSku;

    @Schema(description = "外部SKU")
    private String sourceSku;

    @Schema(description = "计划手机号")
    private String planMobile;

    @Schema(description = "生产手机号")
    private String planMobileProduced;

    @Schema(description = "证件姓名", example = "李四")
    private String idCardName;

    @Schema(description = "证件号码")
    private String idCardNum;

    @Schema(description = "地址省编码")
    private String addressProvinceCode;

    @Schema(description = "地址市编码")
    private String addressCityCode;

    @Schema(description = "地址区编码")
    private String addressDistrictCode;

    @Schema(description = "地址省")
    private String addressProvince;

    @Schema(description = "地址市")
    private String addressCity;

    @Schema(description = "地址区")
    private String addressDistrict;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "收件人电话")
    private String addressMobile;

    @Schema(description = "收件人姓名", example = "李四")
    private String addressName;

    @Schema(description = "物流公司ID", example = "29667")
    private Long trackingCompanyId;

    @Schema(description = "物流单号")
    private String trackingNumber;

    @Schema(description = "买家备注", example = "你说的对")
    private String buyerMemo;

    @Schema(description = "卖家备注", example = "随便")
    private String sellerMemo;

    @Schema(description = "生产备注", example = "你猜")
    private String producerMemo;

    @Schema(description = "订单状态码", example = "1")
    private Long status;

    @Schema(description = "标志")
    private Long flag;

    @Schema(description = "预警区域")
    private String warnArea;

    @Schema(description = "原因", example = "不对")
    private String reason;

    @Schema(description = "订单来源")
    private String source;

    @Schema(description = "用户下单时间")
    private LocalDateTime orderedAt;

    @Schema(description = "生产时间")
    private LocalDateTime producedAt;

    @Schema(description = "发货时间")
    private LocalDateTime deliveredAt;

    @Schema(description = "激活时间")
    private LocalDateTime activatedAt;

    @Schema(description = "充值时间")
    private LocalDateTime rechargedAt;

    @Schema(description = "卖家备注", example = "你猜")
    private String memo;

    @Schema(description = "数量")
    private String amount;

    @Schema(description = "状态变更时间")
    private LocalDateTime statusUpdatedAt;

    @Schema(description = "退款状态", example = "1")
    private String refundStatus;

    @Schema(description = "激活状态", example = "1")
    private String activeStatus;

    @Schema(description = "ICCID", example = "2781")
    private String iccid;

    @Schema(description = "真实外部订单编号", example = "25683")
    private String realSourceId;

    @Schema(description = "图片大小")
    private Long picSize;

    @Schema(description = "归属地省")
    private String regionP;

    @Schema(description = "归属地市")
    private String regionC;

    @Schema(description = "分销商名称", example = "王五")
    private String merchantName;

    @Schema(description = "上游状态", example = "2")
    private String upStatus;

    @Schema(description = "上游订单号", example = "21235")
    private String upstreamOrderId;

    @Schema(description = "镇/乡")
    private String town;

    @Schema(description = "物流公司名称")
    private String trackingCompany;

    @Schema(description = "订单状态名称", example = "王五")
    private String statusName;

    @Schema(description = "加密收货电话")
    private String encryptAddressMobile;

    @Schema(description = "加密收货人姓名", example = "赵六")
    private String encryptAddressName;

    @Schema(description = "加密证件姓名", example = "李四")
    private String encryptIdCardName;

    @Schema(description = "加密证件号码")
    private String encryptIdCardNum;

    @Schema(description = "加密详细地址")
    private String encryptAddress;

}