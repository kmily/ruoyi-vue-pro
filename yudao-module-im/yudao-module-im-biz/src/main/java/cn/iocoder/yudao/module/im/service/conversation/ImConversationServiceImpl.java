package cn.iocoder.yudao.module.im.service.conversation;

import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationUpdateLastReadTimeReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationUpdatePinnedReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
import cn.iocoder.yudao.module.im.dal.mysql.conversation.ImConversationMapper;
import cn.iocoder.yudao.module.im.enums.conversation.ImConversationTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

// TODO @hao：前缀 IM
/**
 * IM 会话 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ImConversationServiceImpl implements ImConversationService {

    @Resource
    private ImConversationMapper imConversationMapper;

    @Override
    public List<ImConversationDO> getConversationList() {
        return imConversationMapper.selectList();
    }

    @Override
    public void updatePinned(Long loginUserId, ImConversationUpdatePinnedReqVO updateReqVO) {
        // 1. 获得会话编号
        String no = ImConversationTypeEnum.generateConversationNo(loginUserId, updateReqVO.getTargetId(), updateReqVO.getType());
        // 2. 查询会话
        ImConversationDO conversation = imConversationMapper.selectByNo(no);
        if (conversation == null) {
            // 2.1. 不存在，则插入
            conversation = insertConversation(no, loginUserId, updateReqVO.getTargetId(), updateReqVO.getType());
        }
        // 3. 更新会话
        conversation.setPinned(updateReqVO.getPinned());
        imConversationMapper.updateById(conversation);
        // 4. 做对应更新的 notify 推送
    }

    private ImConversationDO insertConversation(String no, Long userId, Long targetId, Integer type) {
        ImConversationDO imConversationDO = new ImConversationDO();
        imConversationDO.setNo(no);
        imConversationDO.setUserId(userId);
        imConversationDO.setTargetId(targetId);
        imConversationDO.setType(type);
        imConversationMapper.insert(imConversationDO);
        return imConversationDO;
    }

    @Override
    public void updateLastReadTime(Long loginUserId, ImConversationUpdateLastReadTimeReqVO updateReqVO) {
        // 1. 获得会话编号
        String no = ImConversationTypeEnum.generateConversationNo(loginUserId, updateReqVO.getTargetId(), updateReqVO.getType());
        // 2. 查询会话
        ImConversationDO conversation = imConversationMapper.selectByNo(no);
        if (conversation == null) {
            // 2.1. 不存在，则插入
            conversation = insertConversation(no, loginUserId, updateReqVO.getTargetId(), updateReqVO.getType());
        }
        // 3. 更新会话
        conversation.setLastReadTime(updateReqVO.getLastReadTime());
        imConversationMapper.updateById(conversation);
        // 4. 做对应更新的 notify 推送
    }

}