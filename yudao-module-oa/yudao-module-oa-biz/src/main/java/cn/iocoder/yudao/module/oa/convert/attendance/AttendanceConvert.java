package cn.iocoder.yudao.module.oa.convert.attendance;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.AttendanceCreateReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.AttendanceExcelVO;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.AttendanceRespVO;
import cn.iocoder.yudao.module.oa.controller.admin.attendance.vo.AttendanceUpdateReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.attendance.AttendanceDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 考勤打卡 Convert
 *
 * @author 东海
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

    AttendanceUpdateReqVO convertUpdate(AttendanceCreateReqVO createReqVO, Long id);

}
