package cn.iocoder.yudao.module.member.controller.app.family.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

/**
* 用户家庭 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class FamilyBaseVO {

    //@Schema(description = "用户ID", required = true, example = "2371")
    //@NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "家庭名称", required = true, example = "赵六")
    @NotNull(message = "家庭名称不能为空")
    private String name;

    @Schema(description = "告警手机号", required = true, example = "['111', '2222']")
    private List<String> phones;

}
