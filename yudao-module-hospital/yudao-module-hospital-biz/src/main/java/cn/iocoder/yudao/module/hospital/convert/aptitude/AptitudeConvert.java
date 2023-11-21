package cn.iocoder.yudao.module.hospital.convert.aptitude;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.controller.app.aptitude.vo.AppAptitudeRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.aptitude.AptitudeDO;

/**
 * 资质信息 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface AptitudeConvert {

    AptitudeConvert INSTANCE = Mappers.getMapper(AptitudeConvert.class);

    AptitudeDO convert(AptitudeCreateReqVO bean);

    AptitudeDO convert(AptitudeUpdateReqVO bean);

    AptitudeRespVO convert(AptitudeDO bean);

    List<AptitudeRespVO> convertList(List<AptitudeDO> list);
    List<AppAptitudeRespVO> convertList03(List<AptitudeDO> list);

    PageResult<AptitudeRespVO> convertPage(PageResult<AptitudeDO> page);

    List<AptitudeExcelVO> convertList02(List<AptitudeDO> list);

}
