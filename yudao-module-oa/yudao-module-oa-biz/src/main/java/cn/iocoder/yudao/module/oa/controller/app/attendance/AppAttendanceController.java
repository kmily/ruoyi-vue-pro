package cn.iocoder.yudao.module.oa.controller.app.attendance;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "用户 App - 打卡")
@RestController
@RequestMapping("/oa/attendance")
@Validated
public class AppAttendanceController {

    @Resource
    private AttendanceService attendanceService;

    @PostMapping("/create")
    @Operation(summary = "创建考勤打卡")
    @PreAuthenticated
    public CommonResult<Long> createAttendance(@Valid @RequestBody AttendanceCreateReqVO createReqVO) {
        // 校验是否已经打卡


        PageResult<AttendanceDO> list = attendanceService.validateAttendanceExists(createReqVO.getAttendancePeriod(), WebFrameworkUtils.getLoginUserId().toString());
        if (list.getTotal() > 0){
            AttendanceUpdateReqVO tmp = AttendanceConvert.INSTANCE.convertUpdate(createReqVO, list.getList().get(0).getId());
            return success(Long.valueOf(attendanceService.updateAttendance(tmp)));
        }
        return success(attendanceService.createAttendance(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新考勤打卡")
    @PreAuthenticated
    public CommonResult<Integer> updateAttendance(@Valid @RequestBody AttendanceUpdateReqVO updateReqVO) {

        return success( attendanceService.updateAttendance(updateReqVO));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除考勤打卡")
    @PreAuthenticated
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthenticated
    public CommonResult<Boolean> deleteAttendance(@RequestParam("id") Long id) {
        attendanceService.deleteAttendance(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得考勤打卡")
    @PreAuthenticated
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthenticated
    public CommonResult<AttendanceRespVO> getAttendance(@RequestParam("id") Long id) {
        AttendanceDO attendance = attendanceService.getAttendance(id);
        return success(AttendanceConvert.INSTANCE.convert(attendance));
    }

    @GetMapping("/list")
    @Operation(summary = "获得考勤打卡列表")
    @PreAuthenticated
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthenticated
    public CommonResult<List<AttendanceRespVO>> getAttendanceList(@RequestParam("ids") Collection<Long> ids) {
        List<AttendanceDO> list = attendanceService.getAttendanceList(ids);
        return success(AttendanceConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得考勤打卡分页")
    @PreAuthenticated
    public CommonResult<PageResult<AttendanceRespVO>> getAttendancePage(@Valid AttendancePageReqVO pageVO) {
        PageResult<AttendanceDO> pageResult = attendanceService.getAttendancePage(pageVO);
        return success(AttendanceConvert.INSTANCE.convertPage(pageResult));
    }
//    @GetMapping("/getByTime")
//    @Operation(summary = "通过时间段获取打卡信息")
//    public CommonResult<PageResult<AttendanceRespVO>> getAttendancePageByTime(@Valid AttendancePageReqVO pageVO) {
//        PageResult<AttendanceDO> pageResult = attendanceService.getAttendancePage(pageVO);
//        return success(AttendanceConvert.INSTANCE.convertPage(pageResult));
//    }
    @GetMapping("/time-page")
    @Operation(summary = "获得考勤条件查询分页")
    @PreAuthenticated
    public CommonResult<PageResult<AttendanceRespVO>> findByDateBetween(@Valid AttendanceTypeTimeRangePageReqVO timeVO) {
            timeVO.setCreator(WebFrameworkUtils.getLoginUserId().toString());
            PageResult<AttendanceDO> pageResult = attendanceService.getAttendancePage(timeVO);
            return success(AttendanceConvert.INSTANCE.convertPage(pageResult));
    }
    @GetMapping("/export-excel")
    @Operation(summary = "导出考勤打卡 Excel")
    @OperateLog(type = EXPORT)
    @PreAuthenticated
    public void exportAttendanceExcel(@Valid AttendanceExportReqVO exportReqVO,
                                      HttpServletResponse response) throws IOException {
        List<AttendanceDO> list = attendanceService.getAttendanceList(exportReqVO);
        // 导出 Excel
        List<AttendanceExcelVO> datas = AttendanceConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "考勤打卡.xls", "数据", AttendanceExcelVO.class, datas);
    }
}
