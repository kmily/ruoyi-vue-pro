package cn.iocoder.yudao.module.steam.controller.admin.binduser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 -  steam用户绑定 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BindUserRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25045")
    @ExcelProperty("ID")
    private Integer id;

    @Schema(description = "steam名称", example = "王五")
    @ExcelProperty("steam名称")
    private String steamName;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16812")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "SteamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "10768")
    @ExcelProperty("SteamId")
    private String steamId;

    @Schema(description = "交易链接", example = "https://www.iocoder.cn")
    @ExcelProperty("交易链接")
    private String tradeUrl;

    @Schema(description = "API KEY")
    @ExcelProperty("API KEY")
    private String apiKey;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}