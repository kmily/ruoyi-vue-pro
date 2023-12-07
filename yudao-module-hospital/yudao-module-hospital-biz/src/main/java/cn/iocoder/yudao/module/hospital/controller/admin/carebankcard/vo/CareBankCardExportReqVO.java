package cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 提现银行卡 Excel 导出 Request VO，参数和 CareBankCardPageReqVO 是一致的")
@Data
public class CareBankCardExportReqVO {

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
