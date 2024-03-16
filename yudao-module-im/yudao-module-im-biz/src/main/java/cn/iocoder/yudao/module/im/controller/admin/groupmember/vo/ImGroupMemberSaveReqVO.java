package cn.iocoder.yudao.module.im.controller.admin.groupmember.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 群成员新增/修改 Request VO")
@Data
public class ImGroupMemberSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "17071")
    private Long id;

    @Schema(description = "群 id", example = "13279")
    private Long groupId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21730")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "昵称", example = "芋艿")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "组内显示名称", example = "芋艿")
    private String aliasName;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}