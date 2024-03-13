package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
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
public class ImConversationServiceImpl implements ImConversationService {

    @Resource
    private ConversationMapper conversationMapper;

    @Override
    public Long createConversation(ImConversationSaveReqVO createReqVO) {
        // 插入
        ImConversationDO conversation = BeanUtils.toBean(createReqVO, ImConversationDO.class);
        conversationMapper.insert(conversation);
        // 返回
        return conversation.getId();
    }

    @Override
    public void updateConversation(ImConversationSaveReqVO updateReqVO) {
        // 校验存在
        validateConversationExists(updateReqVO.getId());
        // 更新
        ImConversationDO updateObj = BeanUtils.toBean(updateReqVO, ImConversationDO.class);
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
    public ImConversationDO getConversation(Long id) {
        return conversationMapper.selectById(id);
    }

    @Override
    public PageResult<ImConversationDO> getConversationPage(ImConversationPageReqVO pageReqVO) {
        return conversationMapper.selectPage(pageReqVO);
    }

}