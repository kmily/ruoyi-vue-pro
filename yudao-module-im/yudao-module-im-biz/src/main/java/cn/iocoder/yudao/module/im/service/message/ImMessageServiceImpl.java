package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.message.ImMessageMapper;
import cn.iocoder.yudao.module.im.enums.message.ImMessageStatusEnum;
import cn.iocoder.yudao.module.im.websocket.message.ImSendMessage;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import org.dromara.hutool.core.date.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
    public Long savePrivateMessage(ImSendMessage message, Long senderId) {
        ImMessageSaveReqVO imMessageSaveReqVO = new ImMessageSaveReqVO();
        imMessageSaveReqVO.setClientMessageId(message.getClientMessageId());
        imMessageSaveReqVO.setSenderId(senderId);
        imMessageSaveReqVO.setReceiverId(message.getReceiverId());
        //查询发送人昵称和发送人头像
        AdminUserRespDTO user = adminUserApi.getUser(senderId);
        imMessageSaveReqVO.setSenderNickname(user.getNickname());
        imMessageSaveReqVO.setSenderAvatar(user.getAvatar());
        imMessageSaveReqVO.setConversationType(message.getConversationType());
        imMessageSaveReqVO.setContentType(message.getContentType());
        imMessageSaveReqVO.setConversationNo(senderId + "_" + message.getReceiverId());
        imMessageSaveReqVO.setContent(message.getContent());
        //消息来源 100-用户发送；200-系统发送（一般是通知）；不能为空
        imMessageSaveReqVO.setSendFrom(100);
        imMessageSaveReqVO.setSendTime(TimeUtil.now());
        imMessageSaveReqVO.setMessageStatus(ImMessageStatusEnum.SENDING.getStatus());
        return createMessage(imMessageSaveReqVO);
    }

    @Override
    public void updateMessageStatus(Long messageId, Integer messageStatus) {
        //校验 id 是否存在
        validateMessageExists(messageId);
        //更新消息状态
        ImMessageDO imMessageDO = new ImMessageDO();
        imMessageDO.setId(messageId);
        imMessageDO.setMessageStatus(messageStatus);
        imMessageMapper.updateById(imMessageDO);
    }

}