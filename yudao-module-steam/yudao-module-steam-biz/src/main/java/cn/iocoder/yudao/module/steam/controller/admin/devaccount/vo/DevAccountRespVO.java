package cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 开放平台用户 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DevAccountRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32763")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "用户ID", example = "19141")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "api用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("api用户名")
    private String userName;

    @Schema(description = "steam用户 ID", example = "29689")
    @ExcelProperty("steam用户 ID")
    private String steamId;

    @Schema(description = "状态", example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "用户类型", example = "1")
    @ExcelProperty(value = "用户类型", converter = DictConvert.class)
    @DictFormat("user_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer userType;

}