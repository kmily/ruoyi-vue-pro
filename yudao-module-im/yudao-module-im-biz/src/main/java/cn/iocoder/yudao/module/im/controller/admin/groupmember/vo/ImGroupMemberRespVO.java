package cn.iocoder.yudao.module.im.controller.admin.groupmember.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 群成员 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ImGroupMemberRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "17071")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "群 id", example = "13279")
    @ExcelProperty("群 id")
    private Long groupId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21730")
    @ExcelProperty("用户id")
    private Long userId;

    @Schema(description = "昵称", example = "芋艿")
    @ExcelProperty("昵称")
    private String nickname;

    @Schema(description = "头像")
    @ExcelProperty("头像")
    private String avatar;

    @Schema(description = "组内显示名称", example = "芋艿")
    @ExcelProperty("组内显示名称")
    private String aliasName;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}