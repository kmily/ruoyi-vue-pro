package cn.iocoder.yudao.module.im.controller.admin.inbox.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

// TODO @hao：这个是不是删除掉，应该不会有这个 VO 哈；应该给前端的，是要屏蔽掉这个表，最终返回的都是 Message 哈；说白了，这个 inbox 是后端的内部实现
@Schema(description = "管理后台 - 收件箱 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ImInboxRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18389")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3979")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12454")
    @ExcelProperty("消息编号")
    private Long messageId;

    @Schema(description = "序号，按照 user 递增", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("序号，按照 user 递增")
    private Long sequence;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}