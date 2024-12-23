package cn.iocoder.yudao.module.haoka.controller.admin.ordersource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 订单来源配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrderSourceRespVO {

    @Schema(description = "来源ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25277")
    @ExcelProperty("来源ID")
    private Long id;

    @Schema(description = "来源备注", example = "你猜")
    @ExcelProperty("来源备注")
    private String sourceRemark;

    @Schema(description = "渠道ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "渠道ID", converter = DictConvert.class)
    @DictFormat("haoka_order_channel") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Long channel;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}