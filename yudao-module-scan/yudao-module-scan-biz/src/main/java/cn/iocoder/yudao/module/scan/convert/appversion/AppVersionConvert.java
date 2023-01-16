package cn.iocoder.yudao.module.scan.convert.appversion;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.scan.controller.admin.appversion.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.appversion.AppVersionDO;

/**
 * 应用版本记录 Convert
 *
 * @author lyz
 */
@Mapper
public interface AppVersionConvert {

    AppVersionConvert INSTANCE = Mappers.getMapper(AppVersionConvert.class);

    AppVersionDO convert(AppVersionCreateReqVO bean);

    AppVersionDO convert(AppVersionUpdateReqVO bean);

    AppVersionRespVO convert(AppVersionDO bean);

    List<AppVersionRespVO> convertList(List<AppVersionDO> list);

    PageResult<AppVersionRespVO> convertPage(PageResult<AppVersionDO> page);

    List<AppVersionExcelVO> convertList02(List<AppVersionDO> list);

}
