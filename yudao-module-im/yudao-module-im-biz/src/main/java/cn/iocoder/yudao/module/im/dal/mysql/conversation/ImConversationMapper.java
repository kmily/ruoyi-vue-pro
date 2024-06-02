package cn.iocoder.yudao.module.im.dal.mysql.conversation;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * IM 会话 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ImConversationMapper extends BaseMapperX<ImConversationDO> {

    default ImConversationDO selectByNo(String no){
        return selectOne(ImConversationDO::getNo, no);
    }
}