package cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 提现银行卡分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CareBankCardAppPageReqVO extends PageParam {

    @Schema(description = "用户编号", example = "26294")
    private Long careId;

    @Schema(description = "持卡人", example = "李四")
    private String name;

    @Schema(description = "卡号")
    private String cardNo;

    @Schema(description = "开户行")
    private String bank;

    @Schema(description = "身份证")
    private String idCard;

    @Schema(description = "预留手机号")
    private String mobile;

    @Schema(description = "是否默认", example = "1")
    private Boolean defaultStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
