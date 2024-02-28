package cn.iocoder.yudao.module.steam.controller.admin.selling.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 在售饰品新增/修改 Request VO")
@Data
public class SellingSaveReqVO {

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "27975")
    private Long id;

    @Schema(description = "csgoid", example = "18471")
    private Integer appid;

    @Schema(description = "资产id(饰品唯一)", example = "7558")
    private String assetid;

    @Schema(description = "classid", example = "15926")
    private String classid;

    @Schema(description = "instanceid", example = "28349")
    private String instanceid;

    @Schema(description = "amount")
    private String amount;

    @Schema(description = "steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "23183")
    @NotEmpty(message = "steamId不能为空")
    private String steamId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "出售价格单价分", example = "30804")
    private Integer price;

    @Schema(description = "发货状态(0代表未出售，1代表出售中 )", example = "2")
    private Integer transferStatus;

    @Schema(description = "平台用户ID", example = "12743")
    private Long userId;

    @Schema(description = "用户类型(前后台用户)", example = "2")
    private Integer userType;

    @Schema(description = "绑定用户ID", example = "30171")
    private Long bindUserId;

    @Schema(description = "contextid", example = "12276")
    private String contextid;

}