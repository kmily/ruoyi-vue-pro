package cn.iocoder.yudao.module.im.dal.mysql.conversation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationPageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ConversationMapper extends BaseMapperX<ConversationDO> {

    default PageResult<ConversationDO> selectPage(ConversationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConversationDO>()
                .eqIfPresent(ConversationDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ConversationDO::getConversationType, reqVO.getConversationType())
                .eqIfPresent(ConversationDO::getTargetId, reqVO.getTargetId())
                .eqIfPresent(ConversationDO::getNo, reqVO.getNo())
                .eqIfPresent(ConversationDO::getPinned, reqVO.getPinned())
                .betweenIfPresent(ConversationDO::getLastReadTime, reqVO.getLastReadTime())
                .betweenIfPresent(ConversationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ConversationDO::getId));
    }

}