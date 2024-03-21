package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.message.ImMessageMapper;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import cn.iocoder.yudao.module.im.enums.message.ImMessageStatusEnum;
import cn.iocoder.yudao.module.im.websocket.message.ImSendMessage;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import org.dromara.hutool.core.date.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.MESSAGE_NOT_EXISTS;

/**
 * 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ImMessageServiceImpl implements ImMessageService {

    @Resource
    private ImMessageMapper imMessageMapper;
    @Resource
    private AdminUserApi adminUserApi;

    @Override
    public Long createMessage(ImMessageSaveReqVO createReqVO) {
        // 插入
        ImMessageDO message = BeanUtils.toBean(createReqVO, ImMessageDO.class);
        imMessageMapper.insert(message);
        // 返回
        return message.getId();
    }

    @Override
    public void updateMessage(ImMessageSaveReqVO updateReqVO) {
        // 校验存在
        validateMessageExists(updateReqVO.getId());
        // 更新
        ImMessageDO updateObj = BeanUtils.toBean(updateReqVO, ImMessageDO.class);
        imMessageMapper.updateById(updateObj);
    }

    @Override
    public void deleteMessage(Long id) {
        // 校验存在
        validateMessageExists(id);
        // 删除
        imMessageMapper.deleteById(id);
    }

    private void validateMessageExists(Long id) {
        if (imMessageMapper.selectById(id) == null) {
            throw exception(MESSAGE_NOT_EXISTS);
        }
    }

    @Override
    public ImMessageDO getMessage(Long id) {
        return imMessageMapper.selectById(id);
    }

    @Override
    public PageResult<ImMessageDO> getMessagePage(ImMessagePageReqVO pageReqVO) {
        return imMessageMapper.selectPage(pageReqVO);
    }


    @Override
    public ImMessageDO savePrivateMessage(ImSendMessage message, Long fromUserId) {
        return saveImMessageDO(message, fromUserId);
    }

    private ImMessageDO saveImMessageDO(ImSendMessage message, Long fromUserId) {
        // TODO @hao：可以搞成 messageDO
        // TODO @hao：链式调用
        ImMessageDO imMessageDO = new ImMessageDO();
        imMessageDO.setClientMessageId(message.getClientMessageId());
        imMessageDO.setSenderId(fromUserId);
        imMessageDO.setReceiverId(message.getReceiverId());
        // 查询发送人昵称和发送人头像
        AdminUserRespDTO user = adminUserApi.getUser(fromUserId);
        imMessageDO.setSenderNickname(user.getNickname());
        imMessageDO.setSenderAvatar(user.getAvatar());
        imMessageDO.setConversationType(message.getConversationType());
        imMessageDO.setContentType(message.getContentType());
        // 单聊：s_{userId}_{targetId} 群聊：群聊：g_{groupId}
        // TODO @hao：这里的 conversationNo 的生成，可以写到工具类里，或者干脆放到 ImConversationTypeEnum 里；
        if (message.getConversationType().equals(ImConversationTypeEnum.PRIVATE.getType())) {
            imMessageDO.setConversationNo("s_" + fromUserId + "_" + message.getReceiverId());
        } else if (message.getConversationType().equals(ImConversationTypeEnum.GROUP.getType())) {
            imMessageDO.setConversationNo("g_" + message.getReceiverId());
        }
        imMessageDO.setContent(message.getContent());
        // TODO @hao：枚举哈；
        // 消息来源 100-用户发送；200-系统发送（一般是通知）；不能为空
        imMessageDO.setSendFrom(100);
        imMessageDO.setSendTime(TimeUtil.now());
        // TODO @hao：服务端的消息，就是默认生成哈；
        imMessageDO.setMessageStatus(ImMessageStatusEnum.SENDING.getStatus());
        imMessageMapper.insert(imMessageDO);
        return imMessageDO;
    }

    @Override
    public void updateMessageStatus(Long messageId, Integer messageStatus) {
        // 校验 id 是否存在
        validateMessageExists(messageId);
        //更新消息状态
        ImMessageDO imMessageDO = new ImMessageDO();
        imMessageDO.setId(messageId);
        imMessageDO.setMessageStatus(messageStatus);
        imMessageMapper.updateById(imMessageDO);
    }

    @Override
    public ImMessageDO saveGroupMessage(ImSendMessage message, Long fromUserId) {
        return saveImMessageDO(message, fromUserId);
    }

    @Override
    public List<ImMessageDO> loadMessage(Long userId, Long sequence, Integer size) {
        return imMessageMapper.getGreaterThanSequenceMessage(userId, sequence, size);
    }

    @Override
    public List<ImMessageDO> loadAllMessage(Long userId, Integer size) {
        return imMessageMapper.getAllMessage(userId, size);
    }

}