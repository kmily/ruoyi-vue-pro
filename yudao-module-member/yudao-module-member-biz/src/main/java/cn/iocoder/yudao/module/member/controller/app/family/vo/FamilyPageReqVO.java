package cn.iocoder.yudao.module.member.controller.app.family.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户家庭分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FamilyPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "2371")
    private Long userId;

    @Schema(description = "家庭名称", example = "赵六")
    private String name;

    @Schema(description = "告警手机号", required = true, example = "['111', '2222']")
    private List<String> phones;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
