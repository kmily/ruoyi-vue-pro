package cn.iocoder.yudao.module.member.controller.admin.visitpage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 页面访问数据更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VisitPageUpdateReqVO extends VisitPageBaseVO {

    @Schema(description = "主键", required = true, example = "28575")
    @NotNull(message = "主键不能为空")
    private Long id;

}
