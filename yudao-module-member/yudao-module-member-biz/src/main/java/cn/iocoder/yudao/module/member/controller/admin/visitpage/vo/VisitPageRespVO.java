package cn.iocoder.yudao.module.member.controller.admin.visitpage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 页面访问数据 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VisitPageRespVO extends VisitPageBaseVO {

    @Schema(description = "主键", required = true, example = "28575")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
