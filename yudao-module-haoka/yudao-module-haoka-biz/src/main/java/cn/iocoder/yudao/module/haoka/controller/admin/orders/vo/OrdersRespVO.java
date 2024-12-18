package cn.iocoder.yudao.module.haoka.controller.admin.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrdersRespVO {

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12955")
    @ExcelProperty("订单ID")
    private Long id;

    @Schema(description = "生产商ID", example = "15873")
    @ExcelProperty("生产商ID")
    private Long producerId;

    @Schema(description = "产品ID", example = "14115")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "供应商-商品名称", example = "芋艿")
    @ExcelProperty("供应商-商品名称")
    private String supplierProductName;

    @Schema(description = "供应商-商品编码SKU")
    @ExcelProperty("供应商-商品编码SKU")
    private String supplierProductSku;

    @Schema(description = "外部订单编号", example = "1396")
    @ExcelProperty("外部订单编号")
    private String sourceId;

    @Schema(description = "分享ID", example = "26263")
    @ExcelProperty("分享ID")
    private Long shareId;

    @Schema(description = "用户ID", example = "31769")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "产品SKU")
    @ExcelProperty("产品SKU")
    private String productSku;

    @Schema(description = "外部SKU")
    @ExcelProperty("外部SKU")
    private String sourceSku;

    @Schema(description = "计划手机号")
    @ExcelProperty("计划手机号")
    private String planMobile;

    @Schema(description = "生产手机号")
    @ExcelProperty("生产手机号")
    private String planMobileProduced;

    @Schema(description = "证件姓名", example = "李四")
    @ExcelProperty("证件姓名")
    private String idCardName;

    @Schema(description = "证件号码")
    @ExcelProperty("证件号码")
    private String idCardNum;

    @Schema(description = "地址省编码")
    @ExcelProperty("地址省编码")
    private String addressProvinceCode;

    @Schema(description = "地址市编码")
    @ExcelProperty("地址市编码")
    private String addressCityCode;

    @Schema(description = "地址区编码")
    @ExcelProperty("地址区编码")
    private String addressDistrictCode;

    @Schema(description = "地址省")
    @ExcelProperty("地址省")
    private String addressProvince;

    @Schema(description = "地址市")
    @ExcelProperty("地址市")
    private String addressCity;

    @Schema(description = "地址区")
    @ExcelProperty("地址区")
    private String addressDistrict;

    @Schema(description = "详细地址")
    @ExcelProperty("详细地址")
    private String address;

    @Schema(description = "收件人电话")
    @ExcelProperty("收件人电话")
    private String addressMobile;

    @Schema(description = "收件人姓名", example = "李四")
    @ExcelProperty("收件人姓名")
    private String addressName;

    @Schema(description = "物流公司ID", example = "29667")
    @ExcelProperty("物流公司ID")
    private Long trackingCompanyId;

    @Schema(description = "物流单号")
    @ExcelProperty("物流单号")
    private String trackingNumber;

    @Schema(description = "买家备注", example = "你说的对")
    @ExcelProperty("买家备注")
    private String buyerMemo;

    @Schema(description = "卖家备注", example = "随便")
    @ExcelProperty("卖家备注")
    private String sellerMemo;

    @Schema(description = "生产备注", example = "你猜")
    @ExcelProperty("生产备注")
    private String producerMemo;

    @Schema(description = "订单状态码", example = "1")
    @ExcelProperty("订单状态码")
    private Long status;

    @Schema(description = "标志")
    @ExcelProperty("标志")
    private Long flag;

    @Schema(description = "预警区域")
    @ExcelProperty("预警区域")
    private String warnArea;

    @Schema(description = "原因", example = "不对")
    @ExcelProperty("原因")
    private String reason;

    @Schema(description = "订单来源")
    @ExcelProperty("订单来源")
    private String source;

    @Schema(description = "用户下单时间")
    @ExcelProperty("用户下单时间")
    private LocalDateTime orderedAt;

    @Schema(description = "生产时间")
    @ExcelProperty("生产时间")
    private LocalDateTime producedAt;

    @Schema(description = "发货时间")
    @ExcelProperty("发货时间")
    private LocalDateTime deliveredAt;

    @Schema(description = "激活时间")
    @ExcelProperty("激活时间")
    private LocalDateTime activatedAt;

    @Schema(description = "充值时间")
    @ExcelProperty("充值时间")
    private LocalDateTime rechargedAt;

    @Schema(description = "卖家备注", example = "你猜")
    @ExcelProperty("卖家备注")
    private String memo;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private String amount;

    @Schema(description = "状态变更时间")
    @ExcelProperty("状态变更时间")
    private LocalDateTime statusUpdatedAt;

    @Schema(description = "退款状态", example = "1")
    @ExcelProperty("退款状态")
    private String refundStatus;

    @Schema(description = "激活状态", example = "1")
    @ExcelProperty("激活状态")
    private String activeStatus;

    @Schema(description = "ICCID", example = "2781")
    @ExcelProperty("ICCID")
    private String iccid;

    @Schema(description = "真实外部订单编号", example = "25683")
    @ExcelProperty("真实外部订单编号")
    private String realSourceId;

    @Schema(description = "图片大小")
    @ExcelProperty("图片大小")
    private Long picSize;

    @Schema(description = "归属地省")
    @ExcelProperty("归属地省")
    private String regionP;

    @Schema(description = "归属地市")
    @ExcelProperty("归属地市")
    private String regionC;

    @Schema(description = "分销商名称", example = "王五")
    @ExcelProperty("分销商名称")
    private String merchantName;

    @Schema(description = "上游状态", example = "2")
    @ExcelProperty("上游状态")
    private String upStatus;

    @Schema(description = "上游订单号", example = "21235")
    @ExcelProperty("上游订单号")
    private String upstreamOrderId;

    @Schema(description = "镇/乡")
    @ExcelProperty("镇/乡")
    private String town;

    @Schema(description = "物流公司名称")
    @ExcelProperty("物流公司名称")
    private String trackingCompany;

    @Schema(description = "订单状态名称", example = "王五")
    @ExcelProperty("订单状态名称")
    private String statusName;

    @Schema(description = "加密收货电话")
    @ExcelProperty("加密收货电话")
    private String encryptAddressMobile;

    @Schema(description = "加密收货人姓名", example = "赵六")
    @ExcelProperty("加密收货人姓名")
    private String encryptAddressName;

    @Schema(description = "加密证件姓名", example = "李四")
    @ExcelProperty("加密证件姓名")
    private String encryptIdCardName;

    @Schema(description = "加密证件号码")
    @ExcelProperty("加密证件号码")
    private String encryptIdCardNum;

    @Schema(description = "加密详细地址")
    @ExcelProperty("加密详细地址")
    private String encryptAddress;

}