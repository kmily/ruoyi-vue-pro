package cn.iocoder.yudao.module.member.controller.app.serverperson.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 被服务人分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppServerPersonPageReqVO extends PageParam {

    @Schema(description = "会员编号", hidden = true)
    private Long memberId;

    @Schema(description = "被服务人姓名", example = "赵六")
    private String name;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "联系方式")
    private String mobile;

    @Schema(description = "出生日期")
    private LocalDateTime birthday;

    @Schema(description = "用户性别")
    private Byte sex;

    @Schema(description = "紧急联系人")
    private String critical;

    @Schema(description = "是否实名 NO-未实名，YES-实名", example = "李四")
    private String realname;

    @Schema(description = "医保卡正面")
    private String medicalCardFront;

    @Schema(description = "医保卡反面")
    private String medicalCardBack;

    @Schema(description = "病历资料路径 [xxx, xxx]")
    private String medicalRecord;

    @Schema(description = "特殊情况路径[xxx，xxxx]")
    private String special;

    @Schema(description = "状态", example = "1")
    private String status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
