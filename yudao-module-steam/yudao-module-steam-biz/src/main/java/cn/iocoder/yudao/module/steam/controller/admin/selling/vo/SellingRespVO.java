package cn.iocoder.yudao.module.steam.controller.admin.selling.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 在售饰品 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SellingRespVO {

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "27975")
    @ExcelProperty("Primary Key")
    private Long id;

    @Schema(description = "csgoid", example = "18471")
    @ExcelProperty("csgoid")
    private Integer appid;

    @Schema(description = "资产id(饰品唯一)", example = "7558")
    @ExcelProperty("资产id(饰品唯一)")
    private String assetid;

    @Schema(description = "classid", example = "15926")
    @ExcelProperty("classid")
    private String classid;

    @Schema(description = "instanceid", example = "28349")
    @ExcelProperty("instanceid")
    private String instanceid;

    @Schema(description = "amount")
    @ExcelProperty("amount")
    private String amount;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "23183")
    @ExcelProperty("steamId")
    private String steamId;

    @Schema(description = "状态", example = "2")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "出售价格单价分", example = "30804")
    @ExcelProperty("出售价格单价分")
    private Integer price;

    @Schema(description = "平台用户ID", example = "12743")
    @ExcelProperty("平台用户ID")
    private Long userId;

    @Schema(description = "用户类型(前后台用户)", example = "2")
    @ExcelProperty("用户类型(前后台用户)")
    private Integer userType;

    @Schema(description = "绑定用户ID", example = "30171")
    @ExcelProperty("绑定用户ID")
    private Long bindUserId;

    @Schema(description = "contextid", example = "12276")
    @ExcelProperty("contextid")
    private String contextid;

    @Schema(description = "inv_desc_id", example = "3271")
    @ExcelProperty("inv_desc_id")
    private Long invDescId;

    @Schema(description = "发货状态(0代表未出售，1代表出售中，2代表已出售 )", example = "1")
    @ExcelProperty("发货状态(0代表未出售，1代表出售中，2代表已出售 )")
    private Integer transferStatus;

}