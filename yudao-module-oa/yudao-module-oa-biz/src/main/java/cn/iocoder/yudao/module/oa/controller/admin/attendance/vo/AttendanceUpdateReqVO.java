package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 考勤打卡更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttendanceUpdateReqVO extends AttendanceBaseVO {

    @Schema(description = "id", required = true, example = "25123")
    @NotNull(message = "id不能为空")
    private Long id;

}
