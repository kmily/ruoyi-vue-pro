package cn.iocoder.yudao.module.steam.controller.admin.alipayisv.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 签约ISV用户 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AlipayIsvRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25153")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3306")
    @ExcelProperty("用户ID")
    private Long systemUserId;

    @Schema(description = "用户类型", example = "2")
    @ExcelProperty("用户类型")
    private Integer systemUserType;

    @Schema(description = "isv业务系统的申请单id", example = "8756")
    @ExcelProperty("isv业务系统的申请单id")
    private String isvBizId;

    @Schema(description = "协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。")
    @ExcelProperty("协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。")
    private String appAuthToken;

    @Schema(description = "支付宝分配给开发者的应用Id", example = "31752")
    @ExcelProperty("支付宝分配给开发者的应用Id")
    private String appId;

    @Schema(description = "支付宝分配给商户的应用Id", example = "26474")
    @ExcelProperty("支付宝分配给商户的应用Id")
    private String authAppId;

    @Schema(description = "被授权用户id", example = "20898")
    @ExcelProperty("被授权用户id")
    private String userId;

    @Schema(description = "商家支付宝账号对应的ID，2088开头", example = "24476")
    @ExcelProperty("商家支付宝账号对应的ID，2088开头")
    private String merchantPid;

    @Schema(description = "NONE：未签约，表示还没有签约该产品", example = "1")
    @ExcelProperty("NONE：未签约，表示还没有签约该产品")
    private String signStatus;

    @Schema(description = "产品签约状态对象")
    @ExcelProperty("产品签约状态对象")
    private String signStatusList;

}