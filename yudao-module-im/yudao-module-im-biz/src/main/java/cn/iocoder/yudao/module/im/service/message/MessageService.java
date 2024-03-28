package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.SendMessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.SendMessageRespVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;

import java.util.List;

/**
 * 消息 Service 接口
 *
 * @author 芋道源码
 */
public interface MessageService {

    /**
     * 获得历史消息
     *
     * @param pageReqVO 分页查询
     * @return 消息分页
     */
    List<MessageDO> getHistoryMessage(MessagePageReqVO pageReqVO);

    /**
     * 拉取消息-大于 seq 的消息
     *
     * @param userId   用户id
     * @param sequence 序列号
     * @param size 数量
     * @return 消息列表
     */
    List<MessageDO> getMessageListBySequence(Long userId, Long sequence, Integer size);

    /**
     * 发送消息
     * @param loginUserId 登录用户编号
     * @param message 消息
     * @return 消息编号
     */
    SendMessageRespVO sendMessage(Long loginUserId, SendMessageReqVO message);
}