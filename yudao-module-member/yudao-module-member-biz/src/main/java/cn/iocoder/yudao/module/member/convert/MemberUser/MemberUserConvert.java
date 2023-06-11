package cn.iocoder.yudao.module.member.convert.MemberUser;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.MemberUser.MemberUserDO;

/**
 * 用户 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface MemberUserConvert {

    MemberUserConvert INSTANCE = Mappers.getMapper(MemberUserConvert.class);

    MemberUserDO convert(MemberUserCreateReqVO bean);

    MemberUserDO convert(MemberUserUpdateReqVO bean);

    MemberUserRespVO convert(MemberUserDO bean);

    List<MemberUserRespVO> convertList(List<MemberUserDO> list);

    PageResult<MemberUserRespVO> convertPage(PageResult<MemberUserDO> page);

    List<MemberUserExcelVO> convertList02(List<MemberUserDO> list);



}
