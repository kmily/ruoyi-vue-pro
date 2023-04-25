package cn.iocoder.yudao.module.oa.controller.admin.feedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 产品反馈 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class FeedbackBaseVO {

    @Schema(description = "客户名称", example = "王五")
    private String customerName;

    @Schema(description = "反馈内容")
    private String feedbackContent;

    @Schema(description = "联系人", example = "王五")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "附件")
    private String appendFiles;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "更新者")
    private String updateBy;

}
