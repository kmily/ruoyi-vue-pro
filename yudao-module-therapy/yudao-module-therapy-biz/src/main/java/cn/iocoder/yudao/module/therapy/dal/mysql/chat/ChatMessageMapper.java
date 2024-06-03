package cn.iocoder.yudao.module.therapy.dal.mysql.chat;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.therapy.dal.dataobject.chat.ChatMessageDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author:lidongw_1
 * @Date 2024/5/30
 * @Description: 聊天消息 Mapper
 **/


@Mapper
public interface ChatMessageMapper extends BaseMapperX<ChatMessageDO> {
    
}
