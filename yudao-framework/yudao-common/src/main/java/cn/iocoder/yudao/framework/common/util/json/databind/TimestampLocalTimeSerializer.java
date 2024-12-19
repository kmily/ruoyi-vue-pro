package cn.iocoder.yudao.framework.common.util.json.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * 基于时间戳的 LocalTime 序列化器
 *
 * @author shixiaohe
 */
public class TimestampLocalTimeSerializer extends JsonSerializer<LocalTime> {

    public static final TimestampLocalTimeSerializer INSTANCE = new TimestampLocalTimeSerializer();

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 获取当前日期
        LocalDate today = LocalDate.now();

        // 将 LocalTime 和日期合并为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(today, value);

        // 转换为时间戳（毫秒）
        long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // 写入时间戳
        gen.writeNumber(timestamp);
    }

}
