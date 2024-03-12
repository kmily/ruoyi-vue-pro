package cn.iocoder.yudao.module.im.dal.mysql.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MessageMapper extends BaseMapperX<MessageDO> {

    default PageResult<MessageDO> selectPage(MessagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MessageDO>()
                .eqIfPresent(MessageDO::getClientMessageId, reqVO.getClientMessageId())
                .eqIfPresent(MessageDO::getSenderId, reqVO.getSenderId())
                .eqIfPresent(MessageDO::getReceiverId, reqVO.getReceiverId())
                .likeIfPresent(MessageDO::getSenderNickname, reqVO.getSenderNickname())
                .eqIfPresent(MessageDO::getSenderAvatar, reqVO.getSenderAvatar())
                .eqIfPresent(MessageDO::getConversationType, reqVO.getConversationType())
                .eqIfPresent(MessageDO::getConversationNo, reqVO.getConversationNo())
                .eqIfPresent(MessageDO::getContentType, reqVO.getContentType())
                .eqIfPresent(MessageDO::getContent, reqVO.getContent())
                .betweenIfPresent(MessageDO::getSendTime, reqVO.getSendTime())
                .eqIfPresent(MessageDO::getSendFrom, reqVO.getSendFrom())
                .betweenIfPresent(MessageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MessageDO::getId));
    }

}