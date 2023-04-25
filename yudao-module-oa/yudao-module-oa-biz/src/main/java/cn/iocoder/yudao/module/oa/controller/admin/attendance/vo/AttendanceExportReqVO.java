package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 考勤打卡 Excel 导出 Request VO，参数和 AttendancePageReqVO 是一致的")
@Data
public class AttendanceExportReqVO {

    @Schema(description = "打卡类型", example = "1")
    private String attendanceType;

    @Schema(description = "创建者")
    private String createBy;

}
