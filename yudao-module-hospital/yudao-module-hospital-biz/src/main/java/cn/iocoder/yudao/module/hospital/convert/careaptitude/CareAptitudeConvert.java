package cn.iocoder.yudao.module.hospital.convert.careaptitude;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;

/**
 * 医护资质 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface CareAptitudeConvert {

    CareAptitudeConvert INSTANCE = Mappers.getMapper(CareAptitudeConvert.class);

    CareAptitudeDO convert(AppCareAptitudeCreateReqVO bean);

    CareAptitudeDO convert(AppCareAptitudeUpdateReqVO bean);

    AppCareAptitudeRespVO convert(CareAptitudeDO bean);

    List<AppCareAptitudeRespVO> convertList(List<CareAptitudeDO> list);

    PageResult<AppCareAptitudeRespVO> convertPage(PageResult<CareAptitudeDO> page);

    List<AppCareAptitudeExcelVO> convertList02(List<CareAptitudeDO> list);

}
