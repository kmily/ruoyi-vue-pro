package cn.iocoder.yudao.module.im.dal.mysql.conversation;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * IM 会话 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ConversationMapper extends BaseMapperX<ConversationDO> {

    default ConversationDO selectByNo(String no){
        return selectOne(ConversationDO::getNo, no);
    }
}