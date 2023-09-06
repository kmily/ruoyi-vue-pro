package cn.iocoder.yudao.module.member.controller.app.homepage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author whycode
 * @title: AppHomePageBindReqVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/510:27
 */
@Schema(description = "用户 APP - 首页绑定设备 Request VO")
@Data
public class AppHomePageBindReqVO {

    @Schema(name = "id", description = "卡片Id", required = true)
    @NotNull(message = "Id不能为空")
    private Long id;

    @Schema(name = "devices", description = "设备ID", required = true)
    @NotNull(message = "设备ID不能为空")
    @Size(min = 1, message = "设备ID不能为空")
    private Set<Long> devices;

}
