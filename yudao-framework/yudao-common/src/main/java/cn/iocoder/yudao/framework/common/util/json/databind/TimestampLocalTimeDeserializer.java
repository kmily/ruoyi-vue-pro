package cn.iocoder.yudao.framework.common.util.json.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 基于时间戳的 LocalTime 反序列化器
 *
 * @author shixiaohe
 */
public class TimestampLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    public static final TimestampLocalTimeDeserializer INSTANCE = new TimestampLocalTimeDeserializer();

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 从时间戳转换为 Instant
        Instant instant = Instant.ofEpochMilli(p.getValueAsLong());

        // 转换为 ZonedDateTime 或 LocalDateTime
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

        // 提取时间部分
        return zonedDateTime.toLocalTime();
    }

}
