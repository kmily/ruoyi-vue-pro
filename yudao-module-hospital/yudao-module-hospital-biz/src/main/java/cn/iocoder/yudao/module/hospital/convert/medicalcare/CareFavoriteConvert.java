package cn.iocoder.yudao.module.hospital.convert.medicalcare;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.CareFavoriteDO;

/**
 * 医护收藏 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface CareFavoriteConvert {

    CareFavoriteConvert INSTANCE = Mappers.getMapper(CareFavoriteConvert.class);

    CareFavoriteDO convert(AppCareFavoriteCreateReqVO bean);


    AppCareFavoriteRespVO convert(CareFavoriteDO bean);

    List<AppCareFavoriteRespVO> convertList(List<CareFavoriteDO> list);

    PageResult<AppCareFavoriteRespVO> convertPage(PageResult<CareFavoriteDO> page);


}
