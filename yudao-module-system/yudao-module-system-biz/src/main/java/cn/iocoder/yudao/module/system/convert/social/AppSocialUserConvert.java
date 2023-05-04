package cn.iocoder.yudao.module.system.convert.social;

import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserUnbindReqDTO;
import cn.iocoder.yudao.module.system.controller.app.social.vo.AppSocialUserBindReqVO;
import cn.iocoder.yudao.module.system.controller.app.social.vo.AppSocialUserUnbindReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppSocialUserConvert {

    AppSocialUserConvert INSTANCE = Mappers.getMapper(AppSocialUserConvert.class);

    SocialUserBindReqDTO convert(Long userId, Integer userType, AppSocialUserBindReqVO reqVO);

    SocialUserUnbindReqDTO convert(Long userId, Integer userType, AppSocialUserUnbindReqVO reqVO);

}
