package cn.iocoder.yudao.module.im.dal.mysql.conversation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationPageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * IM 会话 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ConversationMapper extends BaseMapperX<ConversationDO> {

    default PageResult<ConversationDO> selectPage(ImConversationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConversationDO>()
                .eqIfPresent(ConversationDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ConversationDO::getType, reqVO.getType())
                .eqIfPresent(ConversationDO::getTargetId, reqVO.getTargetId())
                .eqIfPresent(ConversationDO::getNo, reqVO.getNo())
                .eqIfPresent(ConversationDO::getPinned, reqVO.getPinned())
                .betweenIfPresent(ConversationDO::getLastReadTime, reqVO.getLastReadTime())
                .betweenIfPresent(ConversationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ConversationDO::getId));
    }

    // TODO @hao：1）no) {，要有空格哈；2）可以直接 selectOne(ImConversationDO::getNo, no) 父类做了封装
    default ConversationDO selectByNo(String no){
        return selectOne(new LambdaQueryWrapperX<ConversationDO>().eq(ConversationDO::getNo, no));
    }
}