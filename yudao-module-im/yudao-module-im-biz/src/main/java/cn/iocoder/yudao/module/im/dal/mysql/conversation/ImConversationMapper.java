package cn.iocoder.yudao.module.im.dal.mysql.conversation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationPageReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * 会话 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ImConversationMapper extends BaseMapperX<ImConversationDO> {

    default PageResult<ImConversationDO> selectPage(ImConversationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ImConversationDO>()
                .eqIfPresent(ImConversationDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ImConversationDO::getConversationType, reqVO.getConversationType())
                .eqIfPresent(ImConversationDO::getTargetId, reqVO.getTargetId())
                .eqIfPresent(ImConversationDO::getNo, reqVO.getNo())
                .eqIfPresent(ImConversationDO::getPinned, reqVO.getPinned())
                .betweenIfPresent(ImConversationDO::getLastReadTime, reqVO.getLastReadTime())
                .betweenIfPresent(ImConversationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ImConversationDO::getId));
    }

    default ImConversationDO selectByNo(String no){
        return selectOne(new LambdaQueryWrapperX<ImConversationDO>().eq(ImConversationDO::getNo, no));
    }
}