package cn.iocoder.yudao.module.system.dal.mysql.social;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserBindDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SocialUserBindMapper extends BaseMapperX<SocialUserBindDO> {

    default void deleteByUserTypeAndUserIdAndPlatformAndUnionId(Integer userType, Long userId,
                                                                Integer platform, String unionId) {
        delete(new LambdaQueryWrapperX<SocialUserBindDO>()
                .eq(SocialUserBindDO::getUserType, userType)
                .eq(SocialUserBindDO::getUserId, userId)
                .eq(SocialUserBindDO::getPlatform, platform)
                .eq(SocialUserBindDO::getUnionId, unionId));
    }

    default SocialUserBindDO selectByUserTypeAndPlatformAndUnionId(Integer userType,
                                                                   Integer platform, String unionId) {
        return selectOne(new LambdaQueryWrapperX<SocialUserBindDO>()
                .eq(SocialUserBindDO::getUserType, userType)
                .eq(SocialUserBindDO::getPlatform, platform)
                .eq(SocialUserBindDO::getUnionId, unionId));
    }

}
