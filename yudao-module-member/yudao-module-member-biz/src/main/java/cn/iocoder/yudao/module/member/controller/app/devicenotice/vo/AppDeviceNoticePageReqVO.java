package cn.iocoder.yudao.module.member.controller.app.devicenotice.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 设备通知分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDeviceNoticePageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "31640")
    private Long userId;

    @Schema(description = "家庭ID", example = "32186")
    private Long familyId;

    @Schema(description = "设备ID", example = "14012")
    private Long deviceId;

    @Schema(description = "卡片名称")
    private String content;

    @Schema(description = "设备类型", example = "1")
    private Byte type;

    @Schema(description = "状态（0未读 1已读）", example = "1")
    private Byte status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
