package cn.iocoder.yudao.module.steam.utils;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 有品专用jackson类
 */
public class JacksonUtils {
    private static ObjectMapper objectMapper = null;

    public JacksonUtils() {
    }

    private static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, Boolean.FALSE);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            }
        });
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return (new Date(Long.parseLong(p.getValueAsString()))).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
            }
        });
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T readValue(JsonParser p, Class<T> valueType) {
        try {
            return objectMapper.readValue(p, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(JsonParser p, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(p, valueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(JsonParser p, ResolvedType valueType) {
        try {
            return objectMapper.readValue(p, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(JsonParser p, JavaType valueType) {
        try {
            return objectMapper.readValue(p, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T extends TreeNode> T readTree(JsonParser p) {
        try {
            return objectMapper.readTree(p);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType) {
        try {
            return objectMapper.readValues(p, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType) {
        try {
            return objectMapper.readValues(p, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType) {
        try {
            return objectMapper.readValues(p, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> MappingIterator<T> readValues(JsonParser p, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValues(p, valueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static JsonNode readTree(InputStream in) {
        try {
            return objectMapper.readTree(in);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static JsonNode readTree(Reader r) {
        try {
            return objectMapper.readTree(r);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static JsonNode readTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static JsonNode readTree(byte[] content) {
        try {
            return objectMapper.readTree(content);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static JsonNode readTree(File file) {
        try {
            return objectMapper.readTree(file);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static JsonNode readTree(URL source) {
        try {
            return objectMapper.readTree(source);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static void writeValue(JsonGenerator g, Object value) {
        try {
            objectMapper.writeValue(g, value);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static void writeTree(JsonGenerator g, TreeNode rootNode) {
        try {
            objectMapper.writeTree(g, rootNode);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static void writeTree(JsonGenerator g, JsonNode rootNode) {
        try {
            objectMapper.writeTree(g, rootNode);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T treeToValue(TreeNode n, Class<T> valueType) {
        try {
            return objectMapper.treeToValue(n, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T extends JsonNode> T valueToTree(Object fromValue) {
        try {
            return objectMapper.valueToTree(fromValue);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(File src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(File src, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(src, valueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(File src, JavaType valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(URL src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(URL src, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(src, valueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(URL src, JavaType valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(String content, JavaType valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(Reader src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(Reader src, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(src, valueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(Reader src, JavaType valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(InputStream src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(InputStream src, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(src, valueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(InputStream src, JavaType valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(byte[] src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(byte[] src, int offset, int len, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, offset, len, valueType);
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(byte[] src, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(src, valueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(byte[] src, int offset, int len, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(src, offset, len, valueTypeRef);
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(byte[] src, JavaType valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(byte[] src, int offset, int len, JavaType valueType) {
        try {
            return objectMapper.readValue(src, offset, len, valueType);
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(DataInput src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T readValue(DataInput src, JavaType valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static void writeValue(File resultFile, Object value) {
        try {
            objectMapper.writeValue(resultFile, value);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static void writeValue(OutputStream out, Object value) {
        try {
            objectMapper.writeValue(out, value);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static void writeValue(DataOutput out, Object value) {
        try {
            objectMapper.writeValue(out, value);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static void writeValue(Writer w, Object value) {
        try {
            objectMapper.writeValue(w, value);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static String writeValueAsString(Object value) {
        if (null == value) {
            return null;
        } else {
            try {
                return objectMapper.writeValueAsString(value);
            } catch (Exception var2) {
                var2.printStackTrace();
                throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
            }
        }
    }

    public static byte[] writeValueAsBytes(Object value) {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (Exception var2) {
            var2.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        try {
            return objectMapper.convertValue(fromValue, toValueTypeRef);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception var3) {
            var3.printStackTrace();
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
    }

    static {
        objectMapper = objectMapper();
    }
}
