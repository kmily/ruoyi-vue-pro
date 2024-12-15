package cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 上游API接口开发配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SuperiorApiDevConfigPageReqVO extends PageParam {

    @Schema(description = "ID", example = "627")
    private Long haokaSuperiorApiId;

    @Schema(description = "标识")
    private String code;

    @Schema(description = "名字", example = "王五")
    private String name;

    @Schema(description = "值")
    private String value;

    @Schema(description = "说明")
    private String remarks;

    @Schema(description = "输入类型", example = "2")
    private Integer inputType;

    @Schema(description = "选项(逗号,分割)")
    private String inputSelectValues;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}