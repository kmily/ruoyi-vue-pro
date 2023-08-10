package cn.iocoder.yudao.module.radar.convert.healthstatistics;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.healthstatistics.HealthStatisticsDO;

/**
 * 睡眠统计记录 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface HealthStatisticsConvert {

    HealthStatisticsConvert INSTANCE = Mappers.getMapper(HealthStatisticsConvert.class);

    HealthStatisticsDO convert(HealthStatisticsCreateReqVO bean);

    HealthStatisticsDO convert(HealthStatisticsUpdateReqVO bean);

    HealthStatisticsRespVO convert(HealthStatisticsDO bean);

    List<HealthStatisticsRespVO> convertList(List<HealthStatisticsDO> list);

    PageResult<HealthStatisticsRespVO> convertPage(PageResult<HealthStatisticsDO> page);

    List<HealthStatisticsExcelVO> convertList02(List<HealthStatisticsDO> list);
}
