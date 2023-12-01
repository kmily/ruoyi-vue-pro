package cn.iocoder.yudao.module.system.convert.helpcenter;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterExcelVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterRespVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.system.dal.dataobject.helpcenter.HelpCenterDO;

/**
 * 常见问题 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface HelpCenterConvert {

    HelpCenterConvert INSTANCE = Mappers.getMapper(HelpCenterConvert.class);

    HelpCenterDO convert(HelpCenterCreateReqVO bean);

    HelpCenterDO convert(HelpCenterUpdateReqVO bean);

    HelpCenterRespVO convert(HelpCenterDO bean);

    List<HelpCenterRespVO> convertList(List<HelpCenterDO> list);

    PageResult<HelpCenterRespVO> convertPage(PageResult<HelpCenterDO> page);

    List<HelpCenterExcelVO> convertList02(List<HelpCenterDO> list);

}
