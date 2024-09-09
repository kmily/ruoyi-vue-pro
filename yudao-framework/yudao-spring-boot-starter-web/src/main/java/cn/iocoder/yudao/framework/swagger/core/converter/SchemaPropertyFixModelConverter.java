package cn.iocoder.yudao.framework.swagger.core.converter;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.RefUtils;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * swagger3的规范?
 * 如果属性中的字段是引用另一个类时, 那么底层会被包装成只有$ref的Schema, 导致属性上的@Schema注解信息直接丢失了
 * 那么就会导致前端不显示属性上的注解信息了, 这里手动将信息和$ref字段放在一起, 保证前端可以正常显现引用属性上@Schema注解的信息
 * 且 springdoc.api-docs.version 需配置 3.1.0 版本, 3.0.1会在序列化时判断$ref有值则直接过滤除了$ref字段以外的信息
 * </p>
 *
 * @author jerryskyr
 */
public class SchemaPropertyFixModelConverter implements ModelConverter {



    /**
     * Instantiates a new Polymorphic model converter.
     *
     */
    public SchemaPropertyFixModelConverter() {
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        Schema<?> resolvedSchema = (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
        if (resolvedSchema == null || resolvedSchema.get$ref() == null) {
            return resolvedSchema;
        }
        return supplementarySchema(type, resolvedSchema, context.getDefinedModels().values());
    }

    /**
     * 补充 schema 信息.
     *
     * @param type the type
     * @param schema the schema
     * @param schemas the schemas
     * @return the schema
     */
    @SuppressWarnings("rawtypes")
    private Schema supplementarySchema(AnnotatedType type, Schema schema, Collection<Schema> schemas) {
        // 拿到真实的类型定义
        Optional<Schema> schemaDefine = schemas.stream()
                .filter(s -> schema.get$ref().equals(RefUtils.constructRef(s.getName())))
                .findAny();
        if (!schemaDefine.isPresent()) {
            return schema;
        }

        // 拿到真实定义下的类型下的属性定义
        Map properties = schemaDefine.get().getProperties();
        if (properties == null) {
            return schema;
        }

        for (Object propertie : properties.values()) {
            if (propertie instanceof Schema) {
                // 拿到属性的真实定义
                Schema propertieSchema = (Schema) propertie;

                String propertieRef = propertieSchema.get$ref();
                // 如果是引用类型, 则将真实定义的属性, 赋值到引用定义中
                if (propertieRef != null) {
                    schemas.stream()
                            .filter(s -> propertieRef.equals(RefUtils.constructRef(s.getName())))
                            .findAny()
                            .ifPresent(propertieSchemaDefine -> {
                                if (StrUtil.isEmpty(propertieSchema.getTitle())) {
                                    propertieSchema.setTitle(propertieSchemaDefine.getTitle());
                                }
                                if (StrUtil.isEmpty(propertieSchema.getDescription())) {
                                    propertieSchema.setDescription(propertieSchemaDefine.getDescription());
                                }
                            });
                }

            }
        }

        return schema;
    }
}
