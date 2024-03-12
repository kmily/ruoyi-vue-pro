package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import cn.iocoder.yudao.module.im.dal.mysql.conversation.ConversationMapper;
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
public class ConversationServiceImpl implements ConversationService {

    @Resource
    private ConversationMapper conversationMapper;

    @Override
    public Long createConversation(ConversationSaveReqVO createReqVO) {
        // 插入
        ConversationDO conversation = BeanUtils.toBean(createReqVO, ConversationDO.class);
        conversationMapper.insert(conversation);
        // 返回
        return conversation.getId();
    }

    @Override
    public void updateConversation(ConversationSaveReqVO updateReqVO) {
        // 校验存在
        validateConversationExists(updateReqVO.getId());
        // 更新
        ConversationDO updateObj = BeanUtils.toBean(updateReqVO, ConversationDO.class);
        conversationMapper.updateById(updateObj);
    }

    @Override
    public void deleteConversation(Long id) {
        // 校验存在
        validateConversationExists(id);
        // 删除
        conversationMapper.deleteById(id);
    }

    private void validateConversationExists(Long id) {
        if (conversationMapper.selectById(id) == null) {
            throw exception(CONVERSATION_NOT_EXISTS);
        }
    }

    @Override
    public ConversationDO getConversation(Long id) {
        return conversationMapper.selectById(id);
    }

    @Override
    public PageResult<ConversationDO> getConversationPage(ConversationPageReqVO pageReqVO) {
        return conversationMapper.selectPage(pageReqVO);
    }

}