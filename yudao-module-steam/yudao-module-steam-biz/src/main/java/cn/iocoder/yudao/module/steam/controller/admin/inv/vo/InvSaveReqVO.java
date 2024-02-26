package cn.iocoder.yudao.module.steam.controller.admin.inv.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 用户库存储新增/修改 Request VO")
@Data
public class InvSaveReqVO {

    @Schema(description = "classid", example = "31967")
    private String classid;

    @Schema(description = "instanceid", example = "10375")
    private String instanceid;

    @Schema(description = "amount")
    private String amount;

    @Schema(description = "steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "24553")
    @NotEmpty(message = "steamId不能为空")
    private String steamId;

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "10811")
    private Long id;

    @Schema(description = "csgoid", example = "6292")
    private Integer appid;

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19819")
    private Long tenantId;

    @Schema(description = "绑定用户ID", example = "19319")
    private Long bindUserId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "发货状态(0代表未出售，1代表已出售 )", example = "2")
    private Integer transferStatus;

    @Schema(description = "平台用户ID", example = "20764")
    private Long userId;

    @Schema(description = "用户类型(前后台用户)", example = "1")
    private Integer userType;

    @Schema(description = "资产id(饰品唯一)", example = "27001")
    private String assetid;

    @Schema(description = "contextid", example = "31061")
    private String contextid;

    @Schema(description = "出售价格单价分", example = "3557")
    private Integer price;

}