package cn.iocoder.yudao.module.system.controller.admin.permission.vo.role;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(title = "管理后台 - 角色更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleUpdateReqVO extends RoleBaseVO {

    @Schema(title  = "角色编号", required = true, example = "1024")
    @NotNull(message = "角色编号不能为空")
    private Long id;

}
