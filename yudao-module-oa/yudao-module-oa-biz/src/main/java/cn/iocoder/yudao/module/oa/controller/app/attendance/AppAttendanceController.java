package cn.iocoder.yudao.module.oa.controller.app.attendance;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.*;
import cn.iocoder.yudao.module.oa.convert.attendance.AttendanceConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import cn.iocoder.yudao.module.oa.service.attendance.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 打卡")
@RestController
@RequestMapping("/oa/attendance")
@Validated
public class AppAttendanceController {

    @Resource
    private AttendanceService attendanceService;

    @PostMapping("/create")
    @Operation(summary = "创建考勤打卡")
    public CommonResult<Long> createAttendance(@Valid @RequestBody AttendanceCreateReqVO createReqVO) {
        return success(attendanceService.createAttendance(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新考勤打卡")
    public CommonResult<Boolean> updateAttendance(@Valid @RequestBody AttendanceUpdateReqVO updateReqVO) {
        attendanceService.updateAttendance(updateReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得考勤打卡")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AttendanceRespVO> getAttendance(@RequestParam("id") Long id) {
        AttendanceDO attendance = attendanceService.getAttendance(id);
        return success(AttendanceConvert.INSTANCE.convert(attendance));
    }

    @GetMapping("/page")
    @Operation(summary = "获得考勤打卡(一段时间内)")
    public CommonResult<PageResult<AttendanceRespVO>> getAttendancePageByTypeTimeRange(@Valid AttendanceTypeTimeRangePageReqVO ReqVO) {
        PageResult<AttendanceDO> pageResult = attendanceService.getAttendanceByTypeTimeRangePage(ReqVO);
        return success(AttendanceConvert.INSTANCE.convertPage(pageResult));
    }
}
