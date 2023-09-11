package cn.iocoder.yudao.module.member.controller.admin.visitpage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 页面访问数据 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class VisitPageBaseVO {

    @Schema(description = "用户ID", required = false, example = "18793")
    private Long userId;

    @Schema(description = "访问页面", example = "首页")
    private String pageName;

    @Schema(description = "页面标记", example = "index")
    private String pageKey;

    @Schema(description = "进入时间", required = false)
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "离开时间", required = false)
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "访问用时单位毫秒", required = false)
    private Long useTime;

}
