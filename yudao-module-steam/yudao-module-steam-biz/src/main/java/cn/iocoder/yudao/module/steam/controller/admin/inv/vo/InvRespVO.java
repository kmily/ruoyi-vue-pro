package cn.iocoder.yudao.module.steam.controller.admin.inv.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - steam用户库存储 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InvRespVO {

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "13481")
    @ExcelProperty("Primary Key")
    private Integer id;

    @Schema(description = "appid", example = "24254")
    @ExcelProperty("appid")
    private Integer appid;

    @Schema(description = "assetid", example = "29566")
    @ExcelProperty("assetid")
    private String assetid;

    @Schema(description = "classid", example = "6035")
    @ExcelProperty("classid")
    private String classid;

    @Schema(description = "instanceid", example = "30735")
    @ExcelProperty("instanceid")
    private String instanceid;

    @Schema(description = "amount")
    @ExcelProperty("amount")
    private String amount;

    @Schema(description = "steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "5752")
    @ExcelProperty("steamId")
    private String steamId;

    @Schema(description = "启用", example = "1")
    @ExcelProperty("启用")
    private String status;

}