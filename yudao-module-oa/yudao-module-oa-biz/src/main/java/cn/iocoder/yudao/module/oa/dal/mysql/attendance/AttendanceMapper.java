package cn.iocoder.yudao.module.oa.dal.mysql.attendance;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.*;

/**
 * 考勤打卡 Mapper
 *
 * @author 东海
 */
@Mapper
public interface AttendanceMapper extends BaseMapperX<AttendanceDO> {

    default PageResult<AttendanceDO> selectPage(AttendancePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AttendanceDO>()
                .eqIfPresent(AttendanceDO::getAttendanceType, reqVO.getAttendanceType())
                .eqIfPresent(AttendanceDO::getAttendancePeriod, reqVO.getAttendancePeriod())
                .eqIfPresent(AttendanceDO::getWorkContent, reqVO.getWorkContent())
                .eqIfPresent(AttendanceDO::getAddress, reqVO.getAddress())
                .eqIfPresent(AttendanceDO::getLongitude, reqVO.getLongitude())
                .eqIfPresent(AttendanceDO::getLatitude, reqVO.getLatitude())
                .eqIfPresent(AttendanceDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(AttendanceDO::getVisitType, reqVO.getVisitType())
                .eqIfPresent(AttendanceDO::getVisitReason, reqVO.getVisitReason())
                .betweenIfPresent(AttendanceDO::getLeaveBeginTime, reqVO.getLeaveBeginTime())
                .betweenIfPresent(AttendanceDO::getLeaveEndTime, reqVO.getLeaveEndTime())
                .eqIfPresent(AttendanceDO::getLeaveReason, reqVO.getLeaveReason())
                .eqIfPresent(AttendanceDO::getLeaveHandover, reqVO.getLeaveHandover())
                .betweenIfPresent(AttendanceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AttendanceDO::getId));
    }

    default List<AttendanceDO> selectList(AttendanceExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AttendanceDO>()
                .eqIfPresent(AttendanceDO::getAttendanceType, reqVO.getAttendanceType())
                .eqIfPresent(AttendanceDO::getAttendancePeriod, reqVO.getAttendancePeriod())
                .eqIfPresent(AttendanceDO::getWorkContent, reqVO.getWorkContent())
                .eqIfPresent(AttendanceDO::getAddress, reqVO.getAddress())
                .eqIfPresent(AttendanceDO::getLongitude, reqVO.getLongitude())
                .eqIfPresent(AttendanceDO::getLatitude, reqVO.getLatitude())
                .eqIfPresent(AttendanceDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(AttendanceDO::getVisitType, reqVO.getVisitType())
                .eqIfPresent(AttendanceDO::getVisitReason, reqVO.getVisitReason())
                .betweenIfPresent(AttendanceDO::getLeaveBeginTime, reqVO.getLeaveBeginTime())
                .betweenIfPresent(AttendanceDO::getLeaveEndTime, reqVO.getLeaveEndTime())
                .eqIfPresent(AttendanceDO::getLeaveReason, reqVO.getLeaveReason())
                .eqIfPresent(AttendanceDO::getLeaveHandover, reqVO.getLeaveHandover())
                .betweenIfPresent(AttendanceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AttendanceDO::getId));
    }

}
