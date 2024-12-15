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

    @Schema(description = "ID", example = "2733")
    private Long haokaSuperiorApiId;

    @Schema(description = "标识")
    private String code;

    @Schema(description = "名字", example = "赵六")
    private String name;

    @Schema(description = "值")
    private String value;

    @Schema(description = "是否必填")
    private Boolean required;

    @Schema(description = "说明")
    private String remarks;

    @Schema(description = "输入类型")
    private Integer inputType;

    @Schema(description = "选项(逗号,分割)")
    private String inputSelectValues;

    @Schema(description = "部门ID", example = "6005")
    private Long deptId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}