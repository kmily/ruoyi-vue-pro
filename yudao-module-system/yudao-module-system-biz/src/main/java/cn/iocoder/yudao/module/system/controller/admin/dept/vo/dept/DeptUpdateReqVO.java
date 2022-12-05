package cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(title = "管理后台 - 部门更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptUpdateReqVO extends DeptBaseVO {

    @Schema(title  = "部门编号", required = true, example = "1024")
    @NotNull(message = "部门编号不能为空")
    private Long id;

}
