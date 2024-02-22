package cn.iocoder.yudao.module.steam.controller.app.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 -  steam用户绑定分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Test extends PageParam {


    @Schema(description = "steam密码", example = "123456")
    @NotNull(message = "steam密码不能为空")
    private String password;
    @Schema(description = "bindUserId", example = "123456")
    @NotNull(message = "bindUserId不能为空")
    private Integer bindUserId;

}