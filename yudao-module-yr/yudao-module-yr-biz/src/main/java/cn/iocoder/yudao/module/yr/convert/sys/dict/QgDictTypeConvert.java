package cn.iocoder.yudao.module.yr.convert.sys.dict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.type.*;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictTypeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QgDictTypeConvert {

    QgDictTypeConvert INSTANCE = Mappers.getMapper(QgDictTypeConvert.class);

    PageResult<QgDictTypeRespVO> convertPage(PageResult<QgDictTypeDO> bean);

    QgDictTypeRespVO convert(QgDictTypeDO bean);

    QgDictTypeDO convert(QgDictTypeCreateReqVO bean);

    QgDictTypeDO convert(QgDictTypeUpdateReqVO bean);

    List<QgDictTypeSimpleRespVO> convertList(List<QgDictTypeDO> list);

    List<QgDictTypeExcelVO> convertList02(List<QgDictTypeDO> list);

}
