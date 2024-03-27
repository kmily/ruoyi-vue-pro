package cn.iocoder.yudao.module.im.dal.mysql.message;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.InboxDO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 消息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MessageMapper extends BaseMapperX<MessageDO> {

    // TODO @hao：不链表哈；先从 ImInboxDO 查询出 messageId，然后再到 ImMessageDO 里 IN
    default List<MessageDO> getGreaterThanSequenceMessage(Long userId, Long sequence, Integer size) {
        //查询 inbox 表中，大于 sequence 的消息,关联 message 表，按照 inbox 表 sequence 升序
        return selectJoinList(MessageDO.class, new MPJLambdaWrapper<MessageDO>()
                .selectAll(MessageDO.class)
                .innerJoin(InboxDO.class, InboxDO::getMessageId, MessageDO::getId)
                .eq(InboxDO::getUserId, userId)
                .gt(InboxDO::getSequence, sequence)
                .orderByAsc(InboxDO::getSequence)
                .last("limit 0," + size));
    }

    // TODO @hao：在 dao 里，使用 selectListByUserId，查询用 select，条件用 by，这个算是 spring data 的 method dsl
    default List<MessageDO> getAllMessage(Long userId, Integer size) {
        //查询 inbox 表中，100条消息,关联 message 表，按照 inbox 表 sequence 降序
        return selectJoinList(MessageDO.class, new MPJLambdaWrapper<MessageDO>()
                .selectAll(MessageDO.class)
                .innerJoin(InboxDO.class, InboxDO::getMessageId, MessageDO::getId)
                .eq(InboxDO::getUserId, userId)
                .orderByDesc(InboxDO::getSequence)
                .last("limit 0," + size));
    }

    default List<MessageDO> getMessagePage(MessagePageReqVO pageReqVO) {
        return selectList(new LambdaQueryWrapperX<MessageDO>()
                .eqIfPresent(MessageDO::getConversationNo, pageReqVO.getConversationNo())
                .betweenIfPresent(MessageDO::getSendTime, pageReqVO.getSendTime())
                .orderByAsc(MessageDO::getSendTime));
    }

}