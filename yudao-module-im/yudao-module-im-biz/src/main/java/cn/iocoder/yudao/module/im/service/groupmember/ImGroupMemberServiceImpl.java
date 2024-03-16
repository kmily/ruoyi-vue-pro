package cn.iocoder.yudao.module.im.service.groupmember;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.im.controller.admin.groupmember.vo.*;
import cn.iocoder.yudao.module.im.dal.dataobject.groupmember.GroupMemberDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.im.dal.mysql.groupmember.ImGroupMemberMapper;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.*;

/**
 * 群成员 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ImGroupMemberServiceImpl implements ImGroupMemberService {

    @Resource
    private ImGroupMemberMapper imGroupMemberMapper;

    @Override
    public Long createGroupMember(ImGroupMemberSaveReqVO createReqVO) {
        // 插入
        GroupMemberDO groupMember = BeanUtils.toBean(createReqVO, GroupMemberDO.class);
        imGroupMemberMapper.insert(groupMember);
        // 返回
        return groupMember.getId();
    }

    @Override
    public void updateGroupMember(ImGroupMemberSaveReqVO updateReqVO) {
        // 校验存在
        validateGroupMemberExists(updateReqVO.getId());
        // 更新
        GroupMemberDO updateObj = BeanUtils.toBean(updateReqVO, GroupMemberDO.class);
        imGroupMemberMapper.updateById(updateObj);
    }

    @Override
    public void deleteGroupMember(Long id) {
        // 校验存在
        validateGroupMemberExists(id);
        // 删除
        imGroupMemberMapper.deleteById(id);
    }

    private void validateGroupMemberExists(Long id) {
        if (imGroupMemberMapper.selectById(id) == null) {
            throw exception(GROUP_MEMBER_NOT_EXISTS);
        }
    }

    @Override
    public GroupMemberDO getGroupMember(Long id) {
        return imGroupMemberMapper.selectById(id);
    }

    @Override
    public PageResult<GroupMemberDO> getGroupMemberPage(ImGroupMemberPageReqVO pageReqVO) {
        return imGroupMemberMapper.selectPage(pageReqVO);
    }

    @Override
    public List<GroupMemberDO> selectByGroupId(Long groupId) {
        return imGroupMemberMapper.selectListByGroupId(groupId);
    }

}