package cn.iocoder.yudao.module.hospital.convert.careaptitude;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeCheckLogDO;

/**
 * 医护资质审核记录 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface CareAptitudeCheckLogConvert {

    CareAptitudeCheckLogConvert INSTANCE = Mappers.getMapper(CareAptitudeCheckLogConvert.class);

    CareAptitudeCheckLogDO convert(CareAptitudeCheckLogCreateReqVO bean);

    CareAptitudeCheckLogDO convert(CareAptitudeCheckLogUpdateReqVO bean);

    CareAptitudeCheckLogRespVO convert(CareAptitudeCheckLogDO bean);

    List<CareAptitudeCheckLogRespVO> convertList(List<CareAptitudeCheckLogDO> list);

    PageResult<CareAptitudeCheckLogRespVO> convertPage(PageResult<CareAptitudeCheckLogDO> page);

    List<CareAptitudeCheckLogExcelVO> convertList02(List<CareAptitudeCheckLogDO> list);

}
