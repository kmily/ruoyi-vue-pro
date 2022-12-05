package cn.iocoder.yudao.module.system.controller.admin.sms.vo.channel;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(title = "管理后台 - 短信渠道精简 Response VO")
@Data
public class SmsChannelSimpleRespVO {

    @Schema(title  = "编号", required = true, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(title  = "短信签名", required = true, example = "芋道源码")
    @NotNull(message = "短信签名不能为空")
    private String signature;

    @Schema(title  = "渠道编码", required = true, example = "YUN_PIAN", description = "参见 SmsChannelEnum 枚举类")
    private String code;

}
