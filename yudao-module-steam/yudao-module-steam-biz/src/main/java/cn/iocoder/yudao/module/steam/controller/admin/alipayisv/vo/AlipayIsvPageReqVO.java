package cn.iocoder.yudao.module.steam.controller.admin.alipayisv.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 签约ISV用户分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AlipayIsvPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "用户ID", example = "3306")
    private Long systemUserId;

    @Schema(description = "用户类型", example = "2")
    private Integer systemUserType;

    @Schema(description = "isv业务系统的申请单id", example = "8756")
    private String isvBizId;

    @Schema(description = "协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码。")
    private String appAuthToken;

    @Schema(description = "支付宝分配给开发者的应用Id", example = "31752")
    private String appId;

    @Schema(description = "支付宝分配给商户的应用Id", example = "26474")
    private String authAppId;

    @Schema(description = "被授权用户id", example = "20898")
    private String userId;

    @Schema(description = "商家支付宝账号对应的ID，2088开头", example = "24476")
    private String merchantPid;

    @Schema(description = "NONE：未签约，表示还没有签约该产品", example = "1")
    private String signStatus;

    @Schema(description = "产品签约状态对象")
    private String signStatusList;

}