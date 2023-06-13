package cn.iocoder.yudao.module.oa.service.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.springframework.data.annotation.CreatedDate;

/**
 * 考勤打卡 Service 接口
 *
 * @author 东海
 */
public interface AttendanceService {

    /**
     * 创建考勤打卡
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAttendance(@Valid AttendanceCreateReqVO createReqVO);

    /**
     * 更新考勤打卡
     *
     * @param updateReqVO 更新信息
     */
    int updateAttendance(@Valid AttendanceUpdateReqVO updateReqVO);

    /**
     * 删除考勤打卡
     *
     * @param id 编号
     */
    void deleteAttendance(Long id);

    /**
     * 获得考勤打卡
     *
     * @param id 编号
     * @return 考勤打卡
     */
    AttendanceDO getAttendance(Long id);

    /**
     * 获得考勤打卡列表
     *
     * @param ids 编号
     * @return 考勤打卡列表
     */
    List<AttendanceDO> getAttendanceList(Collection<Long> ids);

    /**
     * 获得考勤打卡分页
     *
     * @param pageReqVO 分页查询
     * @return 考勤打卡分页
     */
    PageResult<AttendanceDO> getAttendancePage(AttendancePageReqVO pageReqVO);

    /**
     * 获得考勤打卡列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 考勤打卡列表
     */
    List<AttendanceDO> getAttendanceList(AttendanceExportReqVO exportReqVO);


    PageResult<AttendanceDO> getAttendancePage(AttendanceTypeTimeRangePageReqVO timeVO);

    PageResult<AttendanceDO>  validateAttenanceExists(int attendancePeriod, String userId );
}
