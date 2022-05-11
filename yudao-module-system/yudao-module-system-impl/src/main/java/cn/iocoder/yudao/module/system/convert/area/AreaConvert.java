package cn.iocoder.yudao.module.system.convert.area;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaExcelVO;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaRespVO;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.area.AreaDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AreaConvert {

    AreaConvert INSTANCE = Mappers.getMapper(AreaConvert.class);

    AreaDO convert(AreaCreateReqVO bean);

    AreaDO convert(AreaUpdateReqVO bean);

    AreaRespVO convert(AreaDO bean);

    List<AreaRespVO> convertList(List<AreaDO> list);

    PageResult<AreaRespVO> convertPage(PageResult<AreaDO> page);

    List<AreaExcelVO> convertList02(List<AreaDO> list);
}
