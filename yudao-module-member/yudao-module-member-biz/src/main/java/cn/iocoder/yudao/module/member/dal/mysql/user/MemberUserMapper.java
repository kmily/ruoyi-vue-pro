package cn.iocoder.yudao.module.member.dal.mysql.user;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.UserPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.UserStatisticsDetailVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 会员 User Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MemberUserMapper extends BaseMapperX<MemberUserDO> {

    default MemberUserDO selectByMobile(String mobile) {
        return selectOne(MemberUserDO::getMobile, mobile);
    }

    default List<MemberUserDO> selectListByNicknameLike(String nickname) {
        return selectList(new LambdaQueryWrapperX<MemberUserDO>()
                .likeIfPresent(MemberUserDO::getNickname, nickname));
    }

    default PageResult<MemberUserDO> selectPage(UserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberUserDO>()
                .likeIfPresent(MemberUserDO::getNickname, reqVO.getNickname())
                .eqIfPresent(MemberUserDO::getAvatar, reqVO.getAvatar())
                .eqIfPresent(MemberUserDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MemberUserDO::getMobile, reqVO.getMobile())
                .eqIfPresent(MemberUserDO::getPassword, reqVO.getPassword())
                .eqIfPresent(MemberUserDO::getSex, reqVO.getSex())
                .eqIfPresent(MemberUserDO::getBirthday, reqVO.getBirthday())
                .eqIfPresent(MemberUserDO::getRegisterIp, reqVO.getRegisterIp())
                .eqIfPresent(MemberUserDO::getLoginIp, reqVO.getLoginIp())
                .betweenIfPresent(MemberUserDO::getLoginDate, reqVO.getLoginDate())
                .betweenIfPresent(MemberUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberUserDO::getId));
    }

    List<UserStatisticsDetailVO> selectCountEveryMonth();
}
