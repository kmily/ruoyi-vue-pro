package cn.iocoder.yudao.module.oa.dal.mysql.attendance;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.AttendanceExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.AttendancePageReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.AttendanceTypeTimeRangePageReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 考勤打卡 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface AttendanceMapper extends BaseMapperX<AttendanceDO> {

    default PageResult<AttendanceDO> selectPage(AttendancePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AttendanceDO>()
                .eqIfPresent(AttendanceDO::getAttendanceType, reqVO.getAttendanceType())
                .eqIfPresent(AttendanceDO::getCreator, reqVO.getCreator())
                .orderByDesc(AttendanceDO::getId));
    }


    default PageResult<AttendanceDO> selectPageByTypeTimeRange(AttendanceTypeTimeRangePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AttendanceDO>()
                .eqIfPresent(AttendanceDO::getAttendanceType, reqVO.getAttendanceType())
                .eqIfPresent(AttendanceDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(AttendanceDO::getCreateTime, reqVO.getCreateTime()));
    }

    default List<AttendanceDO> selectList(AttendanceExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AttendanceDO>()
                .eqIfPresent(AttendanceDO::getAttendanceType, reqVO.getAttendanceType())
                .eqIfPresent(AttendanceDO::getCreator, reqVO.getCreator())
                .orderByDesc(AttendanceDO::getId));
    }

}
