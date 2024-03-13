package cn.iocoder.yudao.module.im.dal.mysql.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import org.apache.ibatis.annotations.Mapper;

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

}