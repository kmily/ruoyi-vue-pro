package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import jakarta.validation.Valid;

/**
 * 会话 Service 接口
 *
 * @author 芋道源码
 */
public interface ConversationService {

    /**
     * 创建会话
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createConversation(@Valid ConversationSaveReqVO createReqVO);

    /**
     * 更新会话
     *
     * @param updateReqVO 更新信息
     */
    void updateConversation(@Valid ConversationSaveReqVO updateReqVO);

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
    ConversationDO getConversation(Long id);

    /**
     * 获得会话分页
     *
     * @param pageReqVO 分页查询
     * @return 会话分页
     */
    PageResult<ConversationDO> getConversationPage(ConversationPageReqVO pageReqVO);

}