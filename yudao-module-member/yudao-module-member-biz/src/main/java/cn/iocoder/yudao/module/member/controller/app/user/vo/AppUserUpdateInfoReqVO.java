package cn.iocoder.yudao.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whycode
 * @title: AppUserUpdateInfoReqVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/811:28
 */

@Schema(description = "用户 APP - 修改用户信息 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserUpdateInfoReqVO {

    @Schema(description = "用户昵称", required = true, example = "张三")
    private String nickname;

    @Schema(description = "用户出生日期", required = true, example = "1900-01-01")
    private String birthday;

    @Schema(description = "用户性别", required = true, example = "0")
    private Byte sex;

}
