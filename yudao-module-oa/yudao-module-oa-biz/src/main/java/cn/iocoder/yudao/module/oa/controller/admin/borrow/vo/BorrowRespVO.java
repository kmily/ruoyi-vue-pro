package cn.iocoder.yudao.module.oa.controller.admin.borrow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 借支申请 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BorrowRespVO extends BorrowBaseVO {

    @Schema(description = "id", required = true, example = "23865")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
