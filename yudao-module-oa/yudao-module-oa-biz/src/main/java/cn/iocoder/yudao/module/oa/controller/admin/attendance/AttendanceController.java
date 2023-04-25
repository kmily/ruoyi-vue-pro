package cn.iocoder.yudao.module.oa.controller.admin.attendance;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import cn.iocoder.yudao.module.oa.convert.attendance.AttendanceConvert;
import cn.iocoder.yudao.module.oa.service.attendance.AttendanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理后台 - 考勤打卡")
@RestController
@RequestMapping("/oa/attendance")
@Validated
public class AttendanceController {

    @Resource
    private AttendanceService attendanceService;

    @PostMapping("/create")
    @Operation(summary = "创建考勤打卡")
    @PreAuthorize("@ss.hasPermission('oa:attendance:create')")
    public CommonResult<Long> createAttendance(@Valid @RequestBody AttendanceCreateReqVO createReqVO) {
        return success(attendanceService.createAttendance(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新考勤打卡")
    @PreAuthorize("@ss.hasPermission('oa:attendance:update')")
    public CommonResult<Boolean> updateAttendance(@Valid @RequestBody AttendanceUpdateReqVO updateReqVO) {
        attendanceService.updateAttendance(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除考勤打卡")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('oa:attendance:delete')")
    public CommonResult<Boolean> deleteAttendance(@RequestParam("id") Long id) {
        attendanceService.deleteAttendance(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得考勤打卡")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('oa:attendance:query')")
    public CommonResult<AttendanceRespVO> getAttendance(@RequestParam("id") Long id) {
        AttendanceDO attendance = attendanceService.getAttendance(id);
        return success(AttendanceConvert.INSTANCE.convert(attendance));
    }

    @GetMapping("/list")
    @Operation(summary = "获得考勤打卡列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('oa:attendance:query')")
    public CommonResult<List<AttendanceRespVO>> getAttendanceList(@RequestParam("ids") Collection<Long> ids) {
        List<AttendanceDO> list = attendanceService.getAttendanceList(ids);
        return success(AttendanceConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得考勤打卡分页")
    @PreAuthorize("@ss.hasPermission('oa:attendance:query')")
    public CommonResult<PageResult<AttendanceRespVO>> getAttendancePage(@Valid AttendancePageReqVO pageVO) {
        PageResult<AttendanceDO> pageResult = attendanceService.getAttendancePage(pageVO);
        return success(AttendanceConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出考勤打卡 Excel")
    @PreAuthorize("@ss.hasPermission('oa:attendance:export')")
    @OperateLog(type = EXPORT)
    public void exportAttendanceExcel(@Valid AttendanceExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<AttendanceDO> list = attendanceService.getAttendanceList(exportReqVO);
        // 导出 Excel
        List<AttendanceExcelVO> datas = AttendanceConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "考勤打卡.xls", "数据", AttendanceExcelVO.class, datas);
    }

}
