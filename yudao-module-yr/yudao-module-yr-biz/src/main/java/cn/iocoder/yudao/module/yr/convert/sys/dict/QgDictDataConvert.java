package cn.iocoder.yudao.module.yr.convert.sys.dict;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.yr.api.dict.QgDictDataRespDTO;
import cn.iocoder.yudao.module.yr.common.vo.UpdateStatusVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dict.vo.data.*;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dict.QgDictDataDO;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface QgDictDataConvert {

    QgDictDataConvert INSTANCE = Mappers.getMapper(QgDictDataConvert.class);

    List<QgDictDataSimpleRespVO> convertList(List<QgDictDataDO> list);

    List<QgDictDataRespVO> convertListAll(List<QgDictDataDO> list);

    QgDictDataRespVO convert(QgDictDataDO bean);

    PageResult<QgDictDataRespVO> convertPage(PageResult<QgDictDataDO> page);

    QgDictDataDO convert(QgDictDataUpdateReqVO bean);

    QgDictDataDO convert(UpdateStatusVO bean);

    QgDictDataDO convert(QgDictDataCreateReqVO bean);

    List<QgDictDataExcelVO> convertList02(List<QgDictDataDO> bean);

    QgDictDataRespDTO convert02(QgDictDataDO bean);

    List<QgDictDataRespDTO> convertList03(Collection<QgDictDataDO> list);

}
