package cn.iocoder.yudao.module.im.dal.dataobject.message.body;


import cn.iocoder.yudao.module.im.jackson.ImMessageBodyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * IM 消息的 body 内容
 */
@JsonDeserialize(using = ImMessageBodyDeserializer.class)
public interface ImMessageBody {
}