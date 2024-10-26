package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationUpdateLastReadTimeReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationUpdatePinnedReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;

import java.util.List;

/**
 * IM 会话 Service 接口
 *
 * @author 芋道源码
 */
public interface ImConversationService {

    /**
     * 获得用户的会话列表
     *
     * @return 会话列表
     */
    List<ImConversationDO> getConversationList(Long loginUserId);

    /**
     * 置顶会话
     *
     * @param loginUserId 登录用户编号
     * @param updateReqVO 更新信息
     */
    void updatePinned(Long loginUserId, ImConversationUpdatePinnedReqVO updateReqVO);

    /**
     * 更新最后已读时间
     *
     * @param loginUserId 登录用户编号
     * @param updateReqVO 更新信息
     */
    void updateLastReadTime(Long loginUserId, ImConversationUpdateLastReadTimeReqVO updateReqVO);

}