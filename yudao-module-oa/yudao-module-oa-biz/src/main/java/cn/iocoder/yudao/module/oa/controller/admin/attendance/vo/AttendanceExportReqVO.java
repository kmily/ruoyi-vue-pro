package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 考勤打卡 Excel 导出 Request VO，参数和 AttendancePageReqVO 是一致的")
@Data
public class AttendanceExportReqVO {

    @Schema(description = "打卡类型", example = "1")
    private int attendanceType;

    @Schema(description = "创建者")
    private String creator;

}
