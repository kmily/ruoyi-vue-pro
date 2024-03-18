package cn.iocoder.yudao.module.im.service.conversation;

import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
import cn.iocoder.yudao.module.im.dal.mysql.conversation.ImConversationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.CONVERSATION_NOT_EXISTS;

/**
 * 会话 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ImConversationServiceImpl implements ImConversationService {

    @Resource
    private ImConversationMapper imConversationMapper;

    @Override
    public Long createConversation(ImConversationSaveReqVO createReqVO) {
        // 插入
        ImConversationDO conversation = BeanUtils.toBean(createReqVO, ImConversationDO.class);
        imConversationMapper.insert(conversation);
        // 返回
        return conversation.getId();
    }

    @Override
    public void updateConversation(ImConversationSaveReqVO updateReqVO) {
        // 校验存在
        validateConversationExists(updateReqVO.getId());
        // 更新
        ImConversationDO updateObj = BeanUtils.toBean(updateReqVO, ImConversationDO.class);
        imConversationMapper.updateById(updateObj);
    }

    @Override
    public void deleteConversation(Long id) {
        // 校验存在
        validateConversationExists(id);
        // 删除
        imConversationMapper.deleteById(id);
    }

    private void validateConversationExists(Long id) {
        if (imConversationMapper.selectById(id) == null) {
            throw exception(CONVERSATION_NOT_EXISTS);
        }
    }

    @Override
    public ImConversationDO getConversation(Long id) {
        return imConversationMapper.selectById(id);
    }

    @Override
    public PageResult<ImConversationDO> getConversationPage(ImConversationPageReqVO pageReqVO) {
        return imConversationMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ImConversationDO> getConversationList() {
        return imConversationMapper.selectList();
    }

    @Override
    public void updateTop(ImConversationSaveReqVO updateReqVO) {
        createOrUpdateConversation(updateReqVO);
    }

    @Override
    public void updateLastReadTime(ImConversationSaveReqVO updateReqVO) {
        createOrUpdateConversation(updateReqVO);
    }

    private void createOrUpdateConversation(ImConversationSaveReqVO updateReqVO) {
        // 操作会话（已读、置顶）时，才会延迟创建,要先判断是否存在，根据 no 查询是否存在,不存在则新增
        ImConversationDO conversation = imConversationMapper.selectByNo(updateReqVO.getNo());
        if (conversation == null) {
            ImConversationDO conversationDO = new ImConversationDO();
            conversationDO.setNo(updateReqVO.getNo());
            conversationDO.setUserId(updateReqVO.getUserId());
            conversationDO.setTargetId(updateReqVO.getTargetId());
            conversationDO.setConversationType(updateReqVO.getConversationType());
            conversationDO.setPinned(updateReqVO.getPinned());
            conversationDO.setLastReadTime(DateUtil.toLocalDateTime(new Date()));
            imConversationMapper.insert(conversationDO);
        } else {
            // 更新
            ImConversationDO updateObj = BeanUtils.toBean(updateReqVO, ImConversationDO.class);
            imConversationMapper.updateById(updateObj);
        }
    }

}