package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.websocket.message.ImSendMessage;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 消息 Service 接口
 *
 * @author 芋道源码
 */
public interface ImMessageService {

    /**
     * 创建消息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMessage(@Valid ImMessageSaveReqVO createReqVO);

    /**
     * 更新消息
     *
     * @param updateReqVO 更新信息
     */
    void updateMessage(@Valid ImMessageSaveReqVO updateReqVO);

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
    ImMessageDO getMessage(Long id);

    /**
     * 获得消息分页
     *
     * @param pageReqVO 分页查询
     * @return 消息分页
     */
    PageResult<ImMessageDO> getMessagePage(ImMessagePageReqVO pageReqVO);


    /**
     * 保存私聊消息
     *
     * @param imSendMessage 消息信息
     * @param fromUserId    发送人编号
     * @return id
     */
    ImMessageDO savePrivateMessage(ImSendMessage imSendMessage, Long fromUserId);

    /**
     * 更新消息状态
     *
     * @param messageId     消息id
     * @param messageStatus 消息状态
     */
    void updateMessageStatus(Long messageId, Integer messageStatus);

    /**
     * 保存群聊消息
     *
     * @param message    消息
     * @param fromUserId 发送者用户ID
     * @return id
     */
    ImMessageDO saveGroupMessage(ImSendMessage message, Long fromUserId);

    /**
     * 拉取消息-大于 seq 的消息
     *
     * @param userId   用户id
     * @param sequence 序列号
     * @param size 数量
     * @return 消息列表
     */
    List<ImMessageDO> loadMessage(Long userId, Long sequence, Integer size);

    /**
     * 拉取全部消息
     *
     * @param userId   登录用户编号
     * @param size 数量
     * @return 消息列表
     */
    List<ImMessageDO> loadAllMessage(Long userId, Integer size);
}