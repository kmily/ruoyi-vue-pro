package cn.iocoder.yudao.module.steam.controller.admin.binduser.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 -  steam用户绑定分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BindUserPageReqVO extends PageParam {

    @Schema(description = "steam名称", example = "王五")
    private String steamName;

    @Schema(description = "用户ID", example = "16812")
    private Long userId;

    @Schema(description = "SteamId", example = "10768")
    private String steamId;

    @Schema(description = "交易链接", example = "https://www.iocoder.cn")
    private String tradeUrl;

    @Schema(description = "API KEY")
    private String apiKey;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}