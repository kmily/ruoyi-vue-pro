package cn.iocoder.yudao.module.im.dal.mysql.message;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// TODO @hao：IM 前缀
/**
 * 消息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface MessageMapper extends BaseMapperX<ImMessageDO> {

    default List<ImMessageDO> selectMessageList(ImMessageDO message) {
        return selectList(new LambdaQueryWrapperX<ImMessageDO>()
                .eqIfPresent(ImMessageDO::getConversationNo, message.getConversationNo())
                .eqIfPresent(ImMessageDO::getSendTime, message.getSendTime())
                .orderByAsc(ImMessageDO::getSendTime));
    }
}