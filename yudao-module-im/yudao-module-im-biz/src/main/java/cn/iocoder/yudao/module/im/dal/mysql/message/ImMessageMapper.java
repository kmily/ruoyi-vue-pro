package cn.iocoder.yudao.module.im.dal.mysql.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 消息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ImMessageMapper extends BaseMapperX<ImMessageDO> {

    default PageResult<ImMessageDO> selectPage(ImMessagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ImMessageDO>()
                .eqIfPresent(ImMessageDO::getClientMessageId, reqVO.getClientMessageId())
                .eqIfPresent(ImMessageDO::getSenderId, reqVO.getSenderId())
                .eqIfPresent(ImMessageDO::getReceiverId, reqVO.getReceiverId())
                .likeIfPresent(ImMessageDO::getSenderNickname, reqVO.getSenderNickname())
                .eqIfPresent(ImMessageDO::getSenderAvatar, reqVO.getSenderAvatar())
                .eqIfPresent(ImMessageDO::getConversationType, reqVO.getConversationType())
                .eqIfPresent(ImMessageDO::getConversationNo, reqVO.getConversationNo())
                .eqIfPresent(ImMessageDO::getContentType, reqVO.getContentType())
                .eqIfPresent(ImMessageDO::getContent, reqVO.getContent())
                .betweenIfPresent(ImMessageDO::getSendTime, reqVO.getSendTime())
                .eqIfPresent(ImMessageDO::getSendFrom, reqVO.getSendFrom())
                .betweenIfPresent(ImMessageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ImMessageDO::getId));
    }

    // TODO @hao：不链表哈；先从 ImInboxDO 查询出 messageId，然后再到 ImMessageDO 里 IN
    default List<ImMessageDO> getGreaterThanSequenceMessage(Long userId, Long sequence, Integer size) {
        //查询 inbox 表中，大于 sequence 的消息,关联 message 表，按照 inbox 表 sequence 升序
        return selectJoinList(ImMessageDO.class, new MPJLambdaWrapper<ImMessageDO>()
                .selectAll(ImMessageDO.class)
                .innerJoin(ImInboxDO.class, ImInboxDO::getMessageId, ImMessageDO::getId)
                .eq(ImInboxDO::getUserId, userId)
                .gt(ImInboxDO::getSequence, sequence)
                .orderByAsc(ImInboxDO::getSequence)
                .last("limit 0," + size));
    }

    // TODO @hao：在 dao 里，使用 selectListByUserId，查询用 select，条件用 by，这个算是 spring data 的 method dsl
    default List<ImMessageDO> getAllMessage(Long userId, Integer size) {
        //查询 inbox 表中，100条消息,关联 message 表，按照 inbox 表 sequence 降序
        return selectJoinList(ImMessageDO.class, new MPJLambdaWrapper<ImMessageDO>()
                .selectAll(ImMessageDO.class)
                .innerJoin(ImInboxDO.class, ImInboxDO::getMessageId, ImMessageDO::getId)
                .eq(ImInboxDO::getUserId, userId)
                .orderByDesc(ImInboxDO::getSequence)
                .last("limit 0," + size));
    }

}