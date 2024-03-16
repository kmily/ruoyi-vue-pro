package cn.iocoder.yudao.module.im.controller.admin.group.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 群分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImGroupPageReqVO extends PageParam {

    @Schema(description = "群名字", example = "芋艿")
    private String groupName;

    @Schema(description = "群主id", example = "31460")
    private Long ownerId;

    @Schema(description = "群头像")
    private String headImage;

    @Schema(description = "群头像缩略图")
    private String headImageThumb;

    @Schema(description = "群公告")
    private String notice;

    @Schema(description = "群备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}