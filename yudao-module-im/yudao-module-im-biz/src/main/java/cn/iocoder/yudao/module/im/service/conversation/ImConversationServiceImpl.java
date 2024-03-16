package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
import cn.iocoder.yudao.module.im.dal.mysql.conversation.ImConversationMapper;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.CONVERSATION_NOT_EXISTS;

/**
 * 会话 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ImConversationServiceImpl implements ImConversationService {

    @Resource
    private ImConversationMapper imConversationMapper;

    @Override
    public Long createConversation(ImConversationSaveReqVO createReqVO) {
        // 插入
        ImConversationDO conversation = BeanUtils.toBean(createReqVO, ImConversationDO.class);
        imConversationMapper.insert(conversation);
        // 返回
        return conversation.getId();
    }

    @Override
    public void updateConversation(ImConversationSaveReqVO updateReqVO) {
        // 校验存在
        validateConversationExists(updateReqVO.getId());
        // 更新
        ImConversationDO updateObj = BeanUtils.toBean(updateReqVO, ImConversationDO.class);
        imConversationMapper.updateById(updateObj);
    }

    @Override
    public void deleteConversation(Long id) {
        // 校验存在
        validateConversationExists(id);
        // 删除
        imConversationMapper.deleteById(id);
    }

    private void validateConversationExists(Long id) {
        if (imConversationMapper.selectById(id) == null) {
            throw exception(CONVERSATION_NOT_EXISTS);
        }
    }

    @Override
    public ImConversationDO getConversation(Long id) {
        return imConversationMapper.selectById(id);
    }

    @Override
    public PageResult<ImConversationDO> getConversationPage(ImConversationPageReqVO pageReqVO) {
        return imConversationMapper.selectPage(pageReqVO);
    }

    @Override
    public void savePrivateConversation(Long fromUserId, Long receiverId) {
        // 创建并保存会话
        createAndSaveConversation(fromUserId, receiverId);
        createAndSaveConversation(receiverId, fromUserId);
    }

    private void createAndSaveConversation(Long userId, Long targetId) {
        // 创建会话
        ImConversationDO conversation = new ImConversationDO();
        conversation.setUserId(userId);
        conversation.setConversationType(ImConversationTypeEnum.PRIVATE.getType());
        conversation.setTargetId(targetId);
        conversation.setNo("s_" + userId + "_" + targetId);
        conversation.setPinned(false);

        // 根据 no 查询是否存在,不存在则新增
        ImConversationDO imConversationDO = imConversationMapper.selectByNo(conversation.getNo());
        if (imConversationDO == null) {
            imConversationMapper.insert(conversation);
        }
    }

}