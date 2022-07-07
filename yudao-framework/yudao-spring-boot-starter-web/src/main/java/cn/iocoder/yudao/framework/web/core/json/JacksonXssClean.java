package cn.iocoder.yudao.framework.web.core.json;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.web.core.util.XssUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * jackson 反序列化 xss 处理
 */
@Slf4j
public class JacksonXssClean extends JsonDeserializer<String> {

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        // XSS filter
        String value = p.getValueAsString();
        if (StrUtil.isNotBlank(value)) {
            value = XssUtils.filter(value);
        }
        return value;
    }

}
