package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.message.MessageMapper;
import jakarta.annotation.Resource;
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
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public Long createMessage(MessageSaveReqVO createReqVO) {
        // 插入
        MessageDO message = BeanUtils.toBean(createReqVO, MessageDO.class);
        messageMapper.insert(message);
        // 返回
        return message.getId();
    }

    @Override
    public void updateMessage(MessageSaveReqVO updateReqVO) {
        // 校验存在
        validateMessageExists(updateReqVO.getId());
        // 更新
        MessageDO updateObj = BeanUtils.toBean(updateReqVO, MessageDO.class);
        messageMapper.updateById(updateObj);
    }

    @Override
    public void deleteMessage(Long id) {
        // 校验存在
        validateMessageExists(id);
        // 删除
        messageMapper.deleteById(id);
    }

    private void validateMessageExists(Long id) {
        if (messageMapper.selectById(id) == null) {
            throw exception(MESSAGE_NOT_EXISTS);
        }
    }

    @Override
    public MessageDO getMessage(Long id) {
        return messageMapper.selectById(id);
    }

    @Override
    public PageResult<MessageDO> getMessagePage(MessagePageReqVO pageReqVO) {
        return messageMapper.selectPage(pageReqVO);
    }

    @Override
    public Long sendPrivateMessage(MessageSaveReqVO messageSaveReqVO) {
        return 0L;
    }

}