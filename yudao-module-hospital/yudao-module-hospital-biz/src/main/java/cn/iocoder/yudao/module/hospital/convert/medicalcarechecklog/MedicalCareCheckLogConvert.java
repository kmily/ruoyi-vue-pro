package cn.iocoder.yudao.module.hospital.convert.medicalcarechecklog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcarechecklog.MedicalCareCheckLogDO;

/**
 * 医护审核记录 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface MedicalCareCheckLogConvert {

    MedicalCareCheckLogConvert INSTANCE = Mappers.getMapper(MedicalCareCheckLogConvert.class);

    MedicalCareCheckLogDO convert(MedicalCareCheckLogCreateReqVO bean);

    MedicalCareCheckLogDO convert(MedicalCareCheckLogUpdateReqVO bean);

    MedicalCareCheckLogRespVO convert(MedicalCareCheckLogDO bean);

    List<MedicalCareCheckLogRespVO> convertList(List<MedicalCareCheckLogDO> list);

    PageResult<MedicalCareCheckLogRespVO> convertPage(PageResult<MedicalCareCheckLogDO> page);

    List<MedicalCareCheckLogExcelVO> convertList02(List<MedicalCareCheckLogDO> list);

}
