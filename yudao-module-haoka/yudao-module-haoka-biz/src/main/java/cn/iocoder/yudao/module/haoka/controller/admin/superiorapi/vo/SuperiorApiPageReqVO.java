package cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 上游API接口分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SuperiorApiPageReqVO extends PageParam {

    @Schema(description = "名字")
    private String name;

    @Schema(description = "是否有选号功能")
    private Boolean enableSelectNum;

    @Schema(description = "异常订单处理方式")
    private Integer abnormalOrderHandleMethod;

    @Schema(description = "接口状态", example = "1")
    private Integer status;

    @Schema(description = "发布日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] publishTime;

    @Schema(description = "是否已配置开发")
    private Boolean isDevConfined;

    @Schema(description = "是否已配置产品")
    private Boolean isSkuConfined;

    @Schema(description = "部门ID", example = "12247")
    private Long deptId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}