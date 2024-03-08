package cn.iocoder.yudao.module.steam.controller.admin.bindipaddress.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 绑定用户IP地址池 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BindIpaddressRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15530")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "端口")
    @ExcelProperty("端口")
    private Long port;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "地址池id", example = "5584")
    @ExcelProperty("地址池id")
    private Long addressId;

    @Schema(description = "ip地址")
    @ExcelProperty("ip地址")
    private String ipAddress;

}