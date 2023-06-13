package cn.iocoder.yudao.module.oa.service.attendance;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.*;
import cn.iocoder.yudao.module.oa.convert.attendance.AttendanceConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import cn.iocoder.yudao.module.oa.dal.mysql.attendance.AttendanceMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.ATTENDANCE_NOT_EXISTS;

/**
 * 考勤打卡 Service 实现类
 *
 * @author 东海
 */
@Service
@Validated
public class AttendanceServiceImpl implements AttendanceService {

    @Resource
    private AttendanceMapper attendanceMapper;

    @Override
    public Long createAttendance(AttendanceCreateReqVO createReqVO) {
        // 插入
        AttendanceDO attendance = AttendanceConvert.INSTANCE.convert(createReqVO);
        attendanceMapper.insert(attendance);
        // 返回
        return attendance.getId();
    }

    @Override
    public int updateAttendance(AttendanceUpdateReqVO updateReqVO) {
        // 校验存在
        validateAttendanceExists(updateReqVO.getId());
        // 更新
        AttendanceDO updateObj = AttendanceConvert.INSTANCE.convert(updateReqVO);
        return attendanceMapper.updateById(updateObj);
    }

    @Override
    public void deleteAttendance(Long id) {
        // 校验存在
        validateAttendanceExists(id);
        // 删除
        attendanceMapper.deleteById(id);
    }

    private void validateAttendanceExists(Long id) {
        if (attendanceMapper.selectById(id) == null) {
            throw exception(ATTENDANCE_NOT_EXISTS);
        }
    }

    public PageResult<AttendanceDO> validateAttendanceExists(String userId){
        AttendanceTypeTimeRangePageReqVO tmp = new AttendanceTypeTimeRangePageReqVO();
        tmp.setCreateTime(new LocalDateTime[]{LocalDateTimeUtils.getTodayStart(), LocalDateTimeUtils.getTodayEnd()});
        tmp.setCreator(userId);
        return attendanceMapper.selectPageByTime(tmp);
    }

    @Override
    public AttendanceDO getAttendance(Long id) {
        return attendanceMapper.selectById(id);
    }

    @Override
    public List<AttendanceDO> getAttendanceList(Collection<Long> ids) {
        return attendanceMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<AttendanceDO> getAttendancePage(AttendancePageReqVO pageReqVO) {
        return attendanceMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AttendanceDO> getAttendanceList(AttendanceExportReqVO exportReqVO) {
        return attendanceMapper.selectList(exportReqVO);
    }


    @Override
    public PageResult<AttendanceDO> getAttendancePage(AttendanceTypeTimeRangePageReqVO typeTimeRangePageReqVO) {
//        LocalDateTime[] createTime = typeTimeRangePageReqVO.getCreateTime();
//        LocalDateTime startDate = createTime[0];
//        LocalDateTime endDate = createTime[1];
//
//
//        Page<AttendanceDO> attendancePage = attendanceMapper.selectPageByTime(startDate, endDate);

        return attendanceMapper.selectPageByTime(typeTimeRangePageReqVO);
    }
}
