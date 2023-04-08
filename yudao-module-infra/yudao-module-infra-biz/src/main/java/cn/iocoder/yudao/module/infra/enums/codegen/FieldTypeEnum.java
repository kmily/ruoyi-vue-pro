package cn.iocoder.yudao.module.infra.enums.codegen;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * 字段类型枚举
 *
 * @author https://github.com/liyupi
 */
public enum FieldTypeEnum {

    TINYINT("TINYINT", "Integer", "number"),
    SMALLINT("SMALLINT", "Integer", "number"),
    MEDIUMINT("MEDIUMINT", "Integer", "number"),
    INT("INT", "Integer", "number"),
    BIGINT("BIGINT", "Long", "number"),
    FLOAT("FLOAT", "Double", "number"),
    DOUBLE("DOUBLE", "Double", "number"),
    DECIMAL("DECIMAL", "BigDecimal", "number"),
    DATE("DATE", "Date", "Date"),
    TIME("TIME", "Time", "Date"),
    YEAR("YEAR", "Integer", "number"),
    DATETIME("DATETIME", "Date", "Date"),
    TIMESTAMP("TIMESTAMP", "Long", "number"),
    CHAR("CHAR", "String", "string"),
    VARCHAR("VARCHAR", "String", "string"),
    TINYTEXT("TINYTEXT", "String", "string"),
    TEXT("TEXT", "String", "string"),
    MEDIUMTEXT("MEDIUMTEXT", "String", "string"),
    LONGTEXT("LONGTEXT", "String", "string"),
    TINYBLOB("TINYBLOB", "byte[]", "string"),
    BLOB("BLOB", "byte[]", "string"),
    MEDIUMBLOB("MEDIUMBLOB", "byte[]", "string"),
    LONGBLOB("LONGBLOB", "byte[]", "string"),
    BINARY("BINARY", "byte[]", "string"),
    VARBINARY("VARBINARY", "byte[]", "string");

    private final String value;

    private final String javaType;

    private final String typescriptType;

    FieldTypeEnum(String value, String javaType, String typescriptType) {
        this.value = value;
        this.javaType = javaType;
        this.typescriptType = typescriptType;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(FieldTypeEnum::getValue).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static FieldTypeEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (FieldTypeEnum mockTypeEnum : FieldTypeEnum.values()) {
            if (mockTypeEnum.value.equals(value)) {
                return mockTypeEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getTypescriptType() {
        return typescriptType;
    }
}
