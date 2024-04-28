package cn.iocoder.yudao.module.im.service.group;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.im.controller.admin.group.vo.*;
import cn.iocoder.yudao.module.im.dal.dataobject.group.ImGroupDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.im.dal.mysql.group.GroupMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.*;

// TODO @hao：前缀 IM
/**
 * 群 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupMapper groupMapper;

    @Override
    public Long createGroup(ImGroupSaveReqVO createReqVO) {
        // 插入
        ImGroupDO group = BeanUtils.toBean(createReqVO, ImGroupDO.class);
        groupMapper.insert(group);
        // 返回
        return group.getId();
    }

    @Override
    public void updateGroup(ImGroupSaveReqVO updateReqVO) {
        // 校验存在
        validateGroupExists(updateReqVO.getId());
        // 更新
        ImGroupDO updateObj = BeanUtils.toBean(updateReqVO, ImGroupDO.class);
        groupMapper.updateById(updateObj);
    }

    @Override
    public void deleteGroup(Long id) {
        // 校验存在
        validateGroupExists(id);
        // 删除
        groupMapper.deleteById(id);
    }

    private void validateGroupExists(Long id) {
        if (groupMapper.selectById(id) == null) {
            throw exception(GROUP_NOT_EXISTS);
        }
    }

    @Override
    public ImGroupDO getGroup(Long id) {
        return groupMapper.selectById(id);
    }

    @Override
    public PageResult<ImGroupDO> getGroupPage(ImGroupPageReqVO pageReqVO) {
        return groupMapper.selectPage(pageReqVO);
    }

}