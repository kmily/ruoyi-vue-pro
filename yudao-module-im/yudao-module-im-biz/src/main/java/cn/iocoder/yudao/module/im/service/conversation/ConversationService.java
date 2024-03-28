package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationLastTimeReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationPinnedReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;

import java.util.List;

/**
 * IM 会话 Service 接口
 *
 * @author 芋道源码
 */
public interface ConversationService {

    /**
     * 获得用户的会话列表
     *
     * @return 会话列表
     */
    List<ConversationDO> getConversationList();

    /**
     * 置顶会话
     *
     * @param updateReqVO 更新信息
     */
    void updatePinned(ConversationPinnedReqVO updateReqVO);

    /**
     * 更新最后已读时间
     *
     * @param updateReqVO 更新信息
     */
    void updateLastReadTime(ConversationLastTimeReqVO updateReqVO);

}