package cn.iocoder.yudao.module.scan.controller.admin.shape.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 体型 Excel 导出 Request VO", description = "参数和 ShapePageReqVO 是一致的")
@Data
public class ShapeExportReqVO {

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @ApiModelProperty(value = "体型名称", example = "赵六")
    private String shapeName;

    @ApiModelProperty(value = "体型值")
    private String shapeValue;

}
