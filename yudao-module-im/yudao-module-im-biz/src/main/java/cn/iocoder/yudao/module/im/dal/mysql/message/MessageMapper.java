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

    default List<MessageDO> getHistoryMessage(MessagePageReqVO pageReqVO) {
        return selectList(new LambdaQueryWrapperX<MessageDO>()
                .eqIfPresent(MessageDO::getConversationNo, pageReqVO.getConversationNo())
                .betweenIfPresent(MessageDO::getSendTime, pageReqVO.getSendTime())
                .orderByAsc(MessageDO::getSendTime));
    }

}