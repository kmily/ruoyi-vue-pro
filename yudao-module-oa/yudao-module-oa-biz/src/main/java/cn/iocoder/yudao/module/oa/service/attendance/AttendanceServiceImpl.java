package cn.iocoder.yudao.module.oa.service.attendance;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.*;
import cn.iocoder.yudao.module.oa.convert.attendance.AttendanceConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import cn.iocoder.yudao.module.oa.dal.mysql.attendance.AttendanceMapper;
import cn.iocoder.yudao.module.oa.enums.attendance.AttendanceTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.ATTENDANCE_NOT_EXISTS;


/**
 * 考勤打卡 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class AttendanceServiceImpl implements AttendanceService {

    @Resource
    private AttendanceMapper attendanceMapper;

    @Override
    public Long createAttendance(AttendanceCreateReqVO createReqVO) {
        //不同的类型检查插入方式不一样
        if(createReqVO.getAttendanceType() == AttendanceTypeEnum.WORK.getType()){
            //
        }
        // 插入
        AttendanceDO attendance = AttendanceConvert.INSTANCE.convert(createReqVO);
        attendanceMapper.insert(attendance);
        // 返回
        return attendance.getId();
    }

    @Override
    public void updateAttendance(AttendanceUpdateReqVO updateReqVO) {
        // 校验存在
        validateAttendanceExists(updateReqVO.getId());
        // 更新
        AttendanceDO updateObj = AttendanceConvert.INSTANCE.convert(updateReqVO);
        attendanceMapper.updateById(updateObj);
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
    public PageResult<AttendanceDO> getAttendanceByTypeTimeRangePage(AttendanceTypeTimeRangePageReqVO pageReqVO) {
        return attendanceMapper.selectPageByTypeTimeRange(pageReqVO);
    }

    @Override
    public List<AttendanceDO> getAttendanceList(AttendanceExportReqVO exportReqVO) {
        return attendanceMapper.selectList(exportReqVO);
    }

}
