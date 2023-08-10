package cn.iocoder.yudao.module.member.controller.app.homepage.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 首页配置 Excel 导出 Request VO，参数和 HomePagePageReqVO 是一致的")
@Data
public class AppHomePageExportReqVO {

    @Schema(description = "用户ID", example = "10081")
    private Long userId;

    @Schema(description = "家庭ID", example = "23700")
    private Long familyId;

    @Schema(description = "房间名称", example = "王五")
    private String name;

    @Schema(description = "数据类型 0-睡眠,1-如厕,2-跌倒,3-离/回家", example = "1")
    private Byte type;

    @Schema(description = "状态（0正常 1停用）", example = "2")
    private Byte status;

    @Schema(description = "排序")
    private Byte sort;

    @Schema(description = "模式，0-系统, 1-自定义")
    private Byte mold;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
    @Schema(description = "绑定设备, 多个','隔开", example = "11,22")
    private List<Map<String, Object>> devices;
}
