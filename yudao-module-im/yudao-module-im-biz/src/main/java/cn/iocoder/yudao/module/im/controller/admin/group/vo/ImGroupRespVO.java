package cn.iocoder.yudao.module.im.controller.admin.group.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 群 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ImGroupRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1003")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "群名字", example = "芋艿")
    @ExcelProperty("群名字")
    private String groupName;

    @Schema(description = "群主id", requiredMode = Schema.RequiredMode.REQUIRED, example = "31460")
    @ExcelProperty("群主id")
    private Long ownerId;

    @Schema(description = "群头像")
    @ExcelProperty("群头像")
    private String headImage;

    @Schema(description = "群头像缩略图")
    @ExcelProperty("群头像缩略图")
    private String headImageThumb;

    @Schema(description = "群公告")
    @ExcelProperty("群公告")
    private String notice;

    @Schema(description = "群备注", example = "你说的对")
    @ExcelProperty("群备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}