package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import jakarta.validation.Valid;

/**
 * 消息 Service 接口
 *
 * @author 芋道源码
 */
public interface MessageService {

    /**
     * 创建消息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMessage(@Valid MessageSaveReqVO createReqVO);

    /**
     * 更新消息
     *
     * @param updateReqVO 更新信息
     */
    void updateMessage(@Valid MessageSaveReqVO updateReqVO);

    /**
     * 删除消息
     *
     * @param id 编号
     */
    void deleteMessage(Long id);

    /**
     * 获得消息
     *
     * @param id 编号
     * @return 消息
     */
    MessageDO getMessage(Long id);

    /**
     * 获得消息分页
     *
     * @param pageReqVO 分页查询
     * @return 消息分页
     */
    PageResult<MessageDO> getMessagePage(MessagePageReqVO pageReqVO);

    /**
     * 发送私聊消息
     * @param messageSaveReqVO 消息信息
     * @return 消息编号
     */
    Long sendPrivateMessage(MessageSaveReqVO messageSaveReqVO);
}