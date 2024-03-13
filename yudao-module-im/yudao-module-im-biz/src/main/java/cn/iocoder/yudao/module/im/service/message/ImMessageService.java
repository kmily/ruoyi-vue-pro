package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import jakarta.validation.Valid;

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
     * 发送私聊消息
     * @param imMessageSaveReqVO 消息信息
     * @return 消息编号
     */
    Long sendPrivateMessage(ImMessageSaveReqVO imMessageSaveReqVO);
}