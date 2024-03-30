package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSaveMessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.SendMessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.SendMessageRespVO;
import cn.iocoder.yudao.module.im.dal.dataobject.group.GroupMemberDO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.message.MessageMapper;
import cn.iocoder.yudao.module.im.enums.conversation.ConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.MessageSourceEnum;
import cn.iocoder.yudao.module.im.enums.message.MessageStatusEnum;
import cn.iocoder.yudao.module.im.service.groupmember.GroupMemberService;
import cn.iocoder.yudao.module.im.service.inbox.InboxService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.MESSAGE_RECEIVER_NOT_EXISTS;

/**
 * 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private InboxService inboxService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private GroupMemberService groupMemberService;

    @Override
    public List<MessageDO> getHistoryMessage(MessagePageReqVO pageReqVO) {
        return messageMapper.getHistoryMessage(pageReqVO);
    }

    @Override
    public List<MessageDO> getMessageListBySequence(Long userId, Long sequence, Integer size) {
        List<Long> messageIds = inboxService.selectMessageIdsByUserIdAndSequence(userId, sequence, size);
        return messageMapper.selectBatchIds(messageIds);
    }

    @Override
    public SendMessageRespVO sendMessage(Long fromUserId, SendMessageReqVO message) {
        // 保存消息
        // TODO @anhaohao：InboxSaveMessageReqVO 不用 new 出来传递到 saveMessage 方法里；
        InboxSaveMessageReqVO inboxSaveMessageReqVO = new InboxSaveMessageReqVO();
        SendMessageRespVO sendMessageRespVO = saveMessage(fromUserId, message, inboxSaveMessageReqVO);

        // 保存收件箱 + 发送消息给用户
        // TODO @anhaohao：考虑到少定义一些 VO，直接传递 MessageDO 就完事了；反正这两者也是强耦合的；
        inboxService.saveInboxAndSendMessage(inboxSaveMessageReqVO);
        return sendMessageRespVO;
    }

    // TODO @anhaohao：这个方法，是不是定义成 private 哈；然后返回是 MessageDO 对象，设置最上面的 sendMessage 也是这个。最终 controller 转成 SendMessageRespVO
    public SendMessageRespVO saveMessage(Long fromUserId, SendMessageReqVO message, InboxSaveMessageReqVO inboxSaveMessageReqVO) {
        // 需要校验 receiverId 存在
        validateReceiverIdExists(message);
        // 查询发送人昵称和发送人头像
        AdminUserRespDTO fromUser = adminUserApi.getUser(fromUserId);
        // 使用链式调用创建 MessageDO 对象
        // TODO @anhaohao：一部分字段，可以 beanutils tobean 搞定；
        // TODO @anhaohao：链式 set 的时候，要把相同的放在一行；例如说，setSenderNickname、setSenderAvatar；本质上，就是为了“同类”在一行，阅读起来简单；
        MessageDO messageDO = new MessageDO()
                .setClientMessageId(message.getClientMessageId())
                .setSenderId(fromUserId)
                .setReceiverId(message.getReceiverId())
                .setSenderNickname(fromUser.getNickname())
                .setSenderAvatar(fromUser.getAvatar())
                .setConversationType(message.getConversationType())
                .setContentType(message.getContentType())
                .setConversationNo(ConversationTypeEnum.generateConversationNo(fromUserId, message.getReceiverId(), message.getConversationType()))
                .setContent(message.getContent())
                .setSendFrom(MessageSourceEnum.USER_SEND.getStatus())
                .setSendTime(TimeUtil.now())
                .setMessageStatus(MessageStatusEnum.SENDING.getStatus());
        messageMapper.insert(messageDO);

        // 设置 InboxSaveMessageReqVO 对象
        inboxSaveMessageReqVO.setConversationType(message.getConversationType())
                .setFromId(fromUserId)
                .setReceiverId(message.getReceiverId())
                .setMessageId(messageDO.getId())
                .setContentType(message.getContentType())
                .setContent(message.getContent())
                .setSendTime(messageDO.getSendTime())
                .setSenderNickname(fromUser.getNickname())
                .setSenderAvatar(fromUser.getAvatar());

        // 返回 SendMessageRespVO 对象
        return new SendMessageRespVO(messageDO.getId(), messageDO.getSendTime());
    }

    // TODO @anhaohao：validateReceiver，更简单一点哈；不仅仅校验存在，未来还可以校验，自己是不是有好友关系、群聊是否在群聊里面等等
    private void validateReceiverIdExists(SendMessageReqVO message) {
        // TODO @anhaohao：这个不要这里校验，交给 validator 校验掉
        if (message.getReceiverId() == null) {
            throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
        }
        if (message.getConversationType().equals(ConversationTypeEnum.SINGLE.getType())) {
            // TODO @anhaohao：// 之后要空一个空格，中英文协作习惯，中文和英文之间，不能连着；最后也不用 ；哈
            //校验用户是否存在；
            AdminUserRespDTO receiverUser = adminUserApi.getUser(message.getReceiverId());
            if (receiverUser == null) {
                throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
            }
        } else if (message.getConversationType().equals(ConversationTypeEnum.GROUP.getType())) {
            //校验群聊是否存在；
            List<GroupMemberDO> groupMemberDOS = groupMemberService.selectByGroupId(message.getReceiverId());
            if (groupMemberDOS.isEmpty()) {
                throw exception(MESSAGE_RECEIVER_NOT_EXISTS);
            }
        }
    }

}