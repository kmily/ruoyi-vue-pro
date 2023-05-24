package cn.iocoder.yudao.module.oa.controller.admin.attendance.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 考勤打卡分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttendancePageReqVO extends PageParam {

    @Schema(description = "打卡类型", example = "1")
    private int attendanceType;

    @Schema(description = "创建者")
    private String creator;

}
