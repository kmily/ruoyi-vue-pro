package cn.iocoder.yudao.module.member.dal.mysql.user;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.MemberUser.MemberUserDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.*;

/**
 * 用户 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MemberUserMapper extends BaseMapperX<MemberUserDO> {

    default PageResult<MemberUserDO> selectPage(MemberUserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberUserDO>()
                .likeIfPresent(MemberUserDO::getNickname, reqVO.getNickname())
                .likeIfPresent(MemberUserDO::getRealName, reqVO.getRealName())
                .eqIfPresent(MemberUserDO::getAvatar, reqVO.getAvatar())
                .eqIfPresent(MemberUserDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MemberUserDO::getMobile, reqVO.getMobile())
                .eqIfPresent(MemberUserDO::getPassword, reqVO.getPassword())
                .eqIfPresent(MemberUserDO::getRegisterIp, reqVO.getRegisterIp())
                .eqIfPresent(MemberUserDO::getLoginIp, reqVO.getLoginIp())
                .eqIfPresent(MemberUserDO::getPayPassword, reqVO.getPayPassword())
                .betweenIfPresent(MemberUserDO::getLoginDate, reqVO.getLoginDate())
                .eqIfPresent(MemberUserDO::getAreaId, reqVO.getAreaId())
                .eqIfPresent(MemberUserDO::getUserGroupId, reqVO.getUserGroupId())
                .eqIfPresent(MemberUserDO::getPoint, reqVO.getPoint())
                .eqIfPresent(MemberUserDO::getReferrer, reqVO.getReferrer())
                .eqIfPresent(MemberUserDO::getGender, reqVO.getGender())
                .eqIfPresent(MemberUserDO::getLabelId, reqVO.getLabelId())
                .betweenIfPresent(MemberUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberUserDO::getId));
    }

    default List<MemberUserDO> selectList(MemberUserExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MemberUserDO>()
                .likeIfPresent(MemberUserDO::getNickname, reqVO.getNickname())
                .likeIfPresent(MemberUserDO::getRealName, reqVO.getRealName())
                .eqIfPresent(MemberUserDO::getAvatar, reqVO.getAvatar())
                .eqIfPresent(MemberUserDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MemberUserDO::getMobile, reqVO.getMobile())
                .eqIfPresent(MemberUserDO::getPassword, reqVO.getPassword())
                .eqIfPresent(MemberUserDO::getRegisterIp, reqVO.getRegisterIp())
                .eqIfPresent(MemberUserDO::getLoginIp, reqVO.getLoginIp())
                .eqIfPresent(MemberUserDO::getPayPassword, reqVO.getPayPassword())
                .betweenIfPresent(MemberUserDO::getLoginDate, reqVO.getLoginDate())
                .eqIfPresent(MemberUserDO::getAreaId, reqVO.getAreaId())
                .eqIfPresent(MemberUserDO::getUserGroupId, reqVO.getUserGroupId())
                .eqIfPresent(MemberUserDO::getPoint, reqVO.getPoint())
                .eqIfPresent(MemberUserDO::getReferrer, reqVO.getReferrer())
                .eqIfPresent(MemberUserDO::getGender, reqVO.getGender())
                .eqIfPresent(MemberUserDO::getLabelId, reqVO.getLabelId())
                .betweenIfPresent(MemberUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberUserDO::getId));
    }

    default List<MemberUserDO> selectListByNicknameLike(String nickname) {
        return selectList(new LambdaQueryWrapperX<MemberUserDO>()
                .likeIfPresent(MemberUserDO::getNickname, nickname));
    }


    default MemberUserDO selectByMobile(String mobile) {
        return selectOne(MemberUserDO::getMobile, mobile);
    }


}
