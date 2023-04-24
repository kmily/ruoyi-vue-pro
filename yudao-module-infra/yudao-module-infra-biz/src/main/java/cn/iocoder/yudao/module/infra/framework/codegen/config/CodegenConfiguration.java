package cn.iocoder.yudao.module.infra.framework.codegen.config;

import cn.iocoder.yudao.module.infra.enums.codegen.MockTypeEnum;
import cn.iocoder.yudao.module.infra.service.codegen.inner.DataGeneratorFactory;
import cn.iocoder.yudao.module.infra.service.codegen.inner.generator.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CodegenProperties.class)
public class CodegenConfiguration {
    /**
     * 初始化一个数据生成器工厂
     *
     * @param dataGenerators 其他的数据生成器
     * @return 数据生成器工厂
     */
    @Bean
    public DataGeneratorFactory dataGeneratorFactory(ObjectProvider<? extends DataGenerator> dataGenerators) {
        final Map<Integer, DataGenerator> mockTypeDataGeneratorMap = new HashMap<>(6);
        mockTypeDataGeneratorMap.put(MockTypeEnum.NONE.getType(), new DefaultDataGenerator());
        mockTypeDataGeneratorMap.put(MockTypeEnum.FIXED.getType(), new FixedDataGenerator());
        mockTypeDataGeneratorMap.put(MockTypeEnum.RANDOM.getType(), new RandomDataGenerator());
        mockTypeDataGeneratorMap.put(MockTypeEnum.RULE.getType(), new RuleDataGenerator());
        mockTypeDataGeneratorMap.put(MockTypeEnum.DICT.getType(), new DictDataGenerator());
        mockTypeDataGeneratorMap.put(MockTypeEnum.INCREASE.getType(), new IncreaseDataGenerator());
        //不允许覆盖默认处理器
        dataGenerators.orderedStream().forEach(
                (Consumer<DataGenerator>) dataGenerator -> mockTypeDataGeneratorMap.putIfAbsent(dataGenerator.getOrder(), dataGenerator)
        );
        return new DataGeneratorFactory(mockTypeDataGeneratorMap);
    }
}
