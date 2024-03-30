package cn.iocoder.yudao.module.im.service.inbox;

import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSaveMessageReqVO;

import java.util.List;

/**
 * IM 收件箱 Service 接口
 *
 * @author 芋道源码
 */
public interface InboxService {

    /**
     * 保存收件箱和发送消息
     *
     * @param inboxSaveMessageReqVO 收件箱保存消息 Request VO
     */
    void saveInboxAndSendMessage(InboxSaveMessageReqVO inboxSaveMessageReqVO);

    /**
     * 获得大于 sequence 的消息ids
     *
     * @param userId   用户编号
     * @param sequence 序列号
     * @param size     数量
     * @return 消息编号列表
     */
    List<Long> selectMessageIdsByUserIdAndSequence(Long userId, Long sequence, Integer size);

}