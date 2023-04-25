package cn.iocoder.yudao.module.system.controller.admin.signreq.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 注册申请 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SignReqRespVO extends SignReqBaseVO {

    @Schema(description = "申请id,主键(自增策略)", required = true, example = "533")
    private Long id;

    @Schema(description = "提交时间")
    private LocalDateTime createTime;

}
