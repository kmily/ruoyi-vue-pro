package cn.iocoder.yudao.module.radar.convert.banner;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.radar.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.banner.BannerDO;

/**
 * 雷达模块banner图 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BannerConvert {

    BannerConvert INSTANCE = Mappers.getMapper(BannerConvert.class);

    BannerDO convert(BannerCreateReqVO bean);

    BannerDO convert(BannerUpdateReqVO bean);

    BannerRespVO convert(BannerDO bean);

    List<BannerRespVO> convertList(List<BannerDO> list);

    PageResult<BannerRespVO> convertPage(PageResult<BannerDO> page);

    List<BannerExcelVO> convertList02(List<BannerDO> list);

}
