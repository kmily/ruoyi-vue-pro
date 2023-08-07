package cn.iocoder.yudao.module.member.convert.homepage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.homepage.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.homepage.HomePageDO;

/**
 * 首页配置 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface HomePageConvert {

    HomePageConvert INSTANCE = Mappers.getMapper(HomePageConvert.class);

    HomePageDO convert(AppHomePageCreateReqVO bean);

    HomePageDO convert(AppHomePageUpdateReqVO bean);

    AppHomePageRespVO convert(HomePageDO bean);

    List<AppHomePageRespVO> convertList(List<HomePageDO> list);

    PageResult<AppHomePageRespVO> convertPage(PageResult<HomePageDO> page);

    List<AppHomePageExcelVO> convertList02(List<HomePageDO> list);

}
