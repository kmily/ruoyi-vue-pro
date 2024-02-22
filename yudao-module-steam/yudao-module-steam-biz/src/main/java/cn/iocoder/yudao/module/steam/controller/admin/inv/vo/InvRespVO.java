package cn.iocoder.yudao.module.steam.controller.admin.inv.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 用户库存储 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InvRespVO {

    @Schema(description = "assetid", example = "7883")
    @ExcelProperty("assetid")
    private String assetid;

    @Schema(description = "classid", example = "31967")
    @ExcelProperty("classid")
    private String classid;

    @Schema(description = "instanceid", example = "10375")
    @ExcelProperty("instanceid")
    private String instanceid;

    @Schema(description = "amount")
    @ExcelProperty("amount")
    private String amount;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "24553")
    @ExcelProperty("steamId")
    private String steamId;

    @Schema(description = "出售价格单价分", example = "26052")
    @ExcelProperty("出售价格单价分")
    private Integer price;

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "10811")
    @ExcelProperty("Primary Key")
    private Long id;

    @Schema(description = "csgoid", example = "6292")
    @ExcelProperty("csgoid")
    private Integer appid;

    @Schema(description = "绑定用户ID", example = "19319")
    @ExcelProperty("绑定用户ID")
    private Long bindUserId;

    @Schema(description = "状态", example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "发货状态(0代表未出售，1代表已出售 )", example = "2")
    @ExcelProperty("发货状态(0代表未出售，1代表已出售 )")
    private Integer transferStatus;

    @Schema(description = "平台用户ID", example = "20764")
    @ExcelProperty("平台用户ID")
    private Long userId;

    @Schema(description = "用户类型(前后台用户)", example = "1")
    @ExcelProperty(value = "用户类型(前后台用户)", converter = DictConvert.class)
    @DictFormat("user_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer userType;

}