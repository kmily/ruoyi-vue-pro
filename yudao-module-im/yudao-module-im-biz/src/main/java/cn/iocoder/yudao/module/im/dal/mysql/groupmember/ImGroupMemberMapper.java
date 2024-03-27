package cn.iocoder.yudao.module.im.dal.mysql.groupmember;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.groupmember.vo.ImGroupMemberPageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.group.GroupMemberDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 群成员 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ImGroupMemberMapper extends BaseMapperX<GroupMemberDO> {

    default PageResult<GroupMemberDO> selectPage(ImGroupMemberPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<GroupMemberDO>()
                .eqIfPresent(GroupMemberDO::getGroupId, reqVO.getGroupId())
                .eqIfPresent(GroupMemberDO::getUserId, reqVO.getUserId())
                .likeIfPresent(GroupMemberDO::getNickname, reqVO.getNickname())
                .eqIfPresent(GroupMemberDO::getAvatar, reqVO.getAvatar())
                .likeIfPresent(GroupMemberDO::getAliasName, reqVO.getAliasName())
                .eqIfPresent(GroupMemberDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(GroupMemberDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(GroupMemberDO::getId));
    }

    default List<GroupMemberDO> selectListByGroupId(Long groupId) {
        return selectList(new LambdaQueryWrapperX<GroupMemberDO>().eq(GroupMemberDO::getGroupId, groupId));
    }
}