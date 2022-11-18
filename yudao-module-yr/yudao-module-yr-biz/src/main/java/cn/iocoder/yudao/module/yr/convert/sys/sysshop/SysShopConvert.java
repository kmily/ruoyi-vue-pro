package cn.iocoder.yudao.module.yr.convert.sys.sysshop;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopExcelVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopRespVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopUpdateReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.sysshop.SysShopDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * 店面 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface SysShopConvert {

    SysShopConvert INSTANCE = Mappers.getMapper(SysShopConvert.class);

    SysShopDO convert(SysShopCreateReqVO bean);

    SysShopDO convert(SysShopUpdateReqVO bean);

    SysShopRespVO convert(SysShopDO bean);

    List<SysShopRespVO> convertList(List<SysShopDO> list);

    PageResult<SysShopRespVO> convertPage(PageResult<SysShopDO> page);

    List<SysShopExcelVO> convertList02(List<SysShopDO> list);

}
