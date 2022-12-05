package cn.iocoder.yudao.module.pay.controller.admin.merchant.vo.app;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(title = "管理后台 - 支付应用信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayAppPageReqVO extends PageParam {

    @Schema(title  = "应用名")
    private String name;

    @Schema(title  = "开启状态")
    private Integer status;

    @Schema(title  = "备注")
    private String remark;

    @Schema(title  = "支付结果的回调地址")
    private String payNotifyUrl;

    @Schema(title  = "退款结果的回调地址")
    private String refundNotifyUrl;

    @Schema(title  = "商户名称")
    private String merchantName;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @Schema(title  = "创建时间")
    private LocalDateTime[] createTime;

}
