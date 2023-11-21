package cn.iocoder.yudao.module.hospital.convert.medicalcare;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;

/**
 * 医护管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface MedicalCareConvert {

    MedicalCareConvert INSTANCE = Mappers.getMapper(MedicalCareConvert.class);

    MedicalCareDO convert(MedicalCareCreateReqVO bean);

    MedicalCareDO convert(MedicalCareUpdateReqVO bean);

    MedicalCareRespVO convert(MedicalCareDO bean);

    List<MedicalCareRespVO> convertList(List<MedicalCareDO> list);

    PageResult<MedicalCareRespVO> convertPage(PageResult<MedicalCareDO> page);

    List<MedicalCareExcelVO> convertList02(List<MedicalCareDO> list);

}
