package cn.iocoder.yudao.module.radar.convert.healthdata;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.radar.bean.entity.HealthData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.radar.controller.admin.healthdata.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthdata.HealthDataDO;

/**
 * 体征数据 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface HealthDataConvert {

    HealthDataConvert INSTANCE = Mappers.getMapper(HealthDataConvert.class);

    HealthDataDO convert(HealthDataCreateReqVO bean);

    HealthDataDO convert(HealthDataUpdateReqVO bean);

    HealthDataRespVO convert(HealthDataDO bean);

    List<HealthDataRespVO> convertList(List<HealthDataDO> list);

    PageResult<HealthDataRespVO> convertPage(PageResult<HealthDataDO> page);

    List<HealthDataExcelVO> convertList02(List<HealthDataDO> list);

    HealthDataCreateReqVO convert(HealthData healthData);
}
