package cn.iocoder.yudao.module.member.controller.app.devicenotice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 设备通知 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDeviceNoticeRespVO extends AppDeviceNoticeBaseVO {

    @Schema(description = "自增编号", required = true, example = "16128")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;


    /**
     * 家庭名称
     */
    private String familyName;

    /**
     * 房间名称
     */
    private String roomName;

}
