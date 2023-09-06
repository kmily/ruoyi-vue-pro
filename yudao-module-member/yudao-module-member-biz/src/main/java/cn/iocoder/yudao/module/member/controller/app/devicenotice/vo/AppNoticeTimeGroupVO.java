package cn.iocoder.yudao.module.member.controller.app.devicenotice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author whycode
 * @title: AppNoticeTimeGroupVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/309:34
 */

@Schema(description = "用户 APP - 设备通知更新 Request VO")
@Data
@ToString(callSuper = true)
public class AppNoticeTimeGroupVO {

    @Schema(description = "时间", example = "2023-08-30 12:11")
    private String time;

    @Schema(description = "消息列表")
    private List<AppDeviceNoticeRespVO> respVOS;

}
