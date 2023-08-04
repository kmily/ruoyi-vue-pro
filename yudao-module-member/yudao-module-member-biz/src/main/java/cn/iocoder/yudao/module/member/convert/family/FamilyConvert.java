package cn.iocoder.yudao.module.member.convert.family;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.family.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;

/**
 * 用户家庭 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface FamilyConvert {

    FamilyConvert INSTANCE = Mappers.getMapper(FamilyConvert.class);

    FamilyDO convert(FamilyCreateReqVO bean);

    FamilyDO convert(FamilyUpdateReqVO bean);

    FamilyRespVO convert(FamilyDO bean);

    List<FamilyRespVO> convertList(List<FamilyDO> list);

    PageResult<FamilyRespVO> convertPage(PageResult<FamilyDO> page);

    List<FamilyExcelVO> convertList02(List<FamilyDO> list);

}
