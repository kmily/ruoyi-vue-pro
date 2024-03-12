package cn.iocoder.yudao.module.im.jackson;

import cn.iocoder.yudao.module.im.dal.dataobject.message.body.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ImMessageBodyDeserializer extends JsonDeserializer<ImMessageBody> {

    @Override
    public ImMessageBody deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        // 根据 node 中的内容来判断应该实例化哪个子类
        if (node.has("content")) {
            return new ImTextMessageBody(node.get("content").asText());
        }
        if (node.has("url")) {
            String url = node.get("url").asText();
            if (node.has("format")) {
                return new ImAudioMessageBody(url, node.get("format").asText());
            }
            return new ImImageMessageBody(url);
        }
        if (node.has("name")) {
            return new ImFileMessageBody(node.get("name").asText(), node.get("url").asText());
        }
        if (node.has("address")) {
            return new ImLocationMessageBody(node.get("address").asText(), node.get("longitude").asDouble(), node.get("latitude").asDouble());
        }
        // 如果没有匹配的属性，抛出异常
        throw ctxt.mappingException("Cannot deserialize to an instance of ImMessageBody");
    }
}