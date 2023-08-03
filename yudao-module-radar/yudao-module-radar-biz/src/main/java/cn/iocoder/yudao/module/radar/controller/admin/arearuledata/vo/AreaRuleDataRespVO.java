package cn.iocoder.yudao.module.radar.controller.admin.arearuledata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 区域数据 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AreaRuleDataRespVO extends AreaRuleDataBaseVO {

    @Schema(description = "自增编号", required = true, example = "22104")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
