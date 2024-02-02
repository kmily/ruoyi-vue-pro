package cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 开放平台用户分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DevAccountPageReqVO extends PageParam {

    @Schema(description = "api用户名", example = "张三")
    private String userName;

    @Schema(description = "steam用户 ID", example = "29689")
    private String steamId;

    @Schema(description = "状态", example = "2")
    private Integer status;

}