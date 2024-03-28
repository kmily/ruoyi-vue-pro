package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationLastTimeReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationPinnedReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import cn.iocoder.yudao.module.im.dal.mysql.conversation.ConversationMapper;
import cn.iocoder.yudao.module.im.service.inbox.InboxService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * IM 会话 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ConversationServiceImpl implements ConversationService {

    @Resource
    private ConversationMapper conversationMapper;
    @Resource
    private InboxService inboxService;

    @Override
    public List<ConversationDO> getConversationList() {
        return conversationMapper.selectList();
    }

    @Override
    public void updatePinned(ConversationPinnedReqVO updateReqVO) {
        // TODO @hao：updateTop 和 updateLastReadTime 使用独立的逻辑实现，不使用统一的 ImConversationSaveReqVO；
        // TODO 大体步骤建议：
        // 1. 先 getOrderCreateConversation，查询会话，不存在则插入；
        // 2. 更新对应的字段
        // 3. 做对应更新的 notify 推送
        ConversationDO conversation = conversationMapper.selectByNo(updateReqVO.getNo());
        if (conversation == null) {
            ConversationDO conversationDO = new ConversationDO();
            conversationDO.setNo(updateReqVO.getNo());
            conversationDO.setPinned(updateReqVO.getPinned());
            conversationDO.setUserId(updateReqVO.getUserId());
            conversationDO.setTargetId(updateReqVO.getTargetId());
            conversationDO.setType(updateReqVO.getType());
            conversationMapper.insert(conversationDO);
        } else {
            // 更新
            ConversationDO updateObj = BeanUtils.toBean(updateReqVO, ConversationDO.class);
            conversationMapper.updateById(updateObj);
        }

    }

    @Override
    public void updateLastReadTime(ConversationLastTimeReqVO updateReqVO) {
        ConversationDO conversation = conversationMapper.selectByNo(updateReqVO.getNo());
        if (conversation == null) {
            ConversationDO conversationDO = new ConversationDO();
            conversationDO.setNo(updateReqVO.getNo());
            conversationDO.setLastReadTime(updateReqVO.getLastReadTime());
            conversationDO.setUserId(updateReqVO.getUserId());
            conversationDO.setTargetId(updateReqVO.getTargetId());
            conversationDO.setType(updateReqVO.getType());
            conversationMapper.insert(conversationDO);
        } else {
            // 更新
            ConversationDO updateObj = BeanUtils.toBean(updateReqVO, ConversationDO.class);
            conversationMapper.updateById(updateObj);
        }
    }

}