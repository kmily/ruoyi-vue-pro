package cn.iocoder.yudao.module.oa.convert.attendance;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;

/**
 * 考勤打卡 Convert
 *
 * @author 管理员
 */
@Mapper
public interface AttendanceConvert {

    AttendanceConvert INSTANCE = Mappers.getMapper(AttendanceConvert.class);

    AttendanceDO convert(AttendanceCreateReqVO bean);

    AttendanceDO convert(AttendanceUpdateReqVO bean);

    AttendanceRespVO convert(AttendanceDO bean);

    List<AttendanceRespVO> convertList(List<AttendanceDO> list);

    PageResult<AttendanceRespVO> convertPage(PageResult<AttendanceDO> page);

    List<AttendanceExcelVO> convertList02(List<AttendanceDO> list);

}
