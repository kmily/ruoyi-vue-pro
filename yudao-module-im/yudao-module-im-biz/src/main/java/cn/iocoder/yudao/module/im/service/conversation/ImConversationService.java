package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
import jakarta.validation.Valid;

/**
 * 会话 Service 接口
 *
 * @author 芋道源码
 */
public interface ImConversationService {

    /**
     * 创建会话
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createConversation(@Valid ImConversationSaveReqVO createReqVO);

    /**
     * 更新会话
     *
     * @param updateReqVO 更新信息
     */
    void updateConversation(@Valid ImConversationSaveReqVO updateReqVO);

    /**
     * 删除会话
     *
     * @param id 编号
     */
    void deleteConversation(Long id);

    /**
     * 获得会话
     *
     * @param id 编号
     * @return 会话
     */
    ImConversationDO getConversation(Long id);

    /**
     * 获得会话分页
     *
     * @param pageReqVO 分页查询
     * @return 会话分页
     */
    PageResult<ImConversationDO> getConversationPage(ImConversationPageReqVO pageReqVO);


    /**
     * 保存私聊会话
     *
     * @param fromUserId 发送者
     * @param receiverId 接收者
     */
    void savePrivateConversation(Long fromUserId, Long receiverId);
}