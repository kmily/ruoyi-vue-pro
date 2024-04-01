package cn.iocoder.yudao.module.steam.convert.app;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UuOrderConvert {
    UuOrderConvert INSTANCE = Mappers.getMapper(UuOrderConvert.class);
}
