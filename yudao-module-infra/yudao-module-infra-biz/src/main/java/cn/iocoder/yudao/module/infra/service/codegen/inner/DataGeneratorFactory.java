package cn.iocoder.yudao.module.infra.service.codegen.inner;

import cn.iocoder.yudao.module.infra.enums.codegen.MockTypeEnum;
import cn.iocoder.yudao.module.infra.service.codegen.inner.generator.DataGenerator;

import java.util.Map;
import java.util.Optional;

/**
 * 数据生成器工厂
 * 工厂 + 单例模式，降低开销
 *
 * @author https://github.com/liyupi
 */
public class DataGeneratorFactory {

    /**
     * 模拟类型 => 生成器映射
     */
    private final Map<Integer, DataGenerator> MOCK_TYPE_DATA_GENERATOR_MAP;

    public DataGeneratorFactory(Map<Integer, DataGenerator> mockTypeDataGeneratorMap) {
        MOCK_TYPE_DATA_GENERATOR_MAP = mockTypeDataGeneratorMap;
    }

    /**
     * 获取实例，一定保证有默认处理器
     *
     * @param type 处理类型
     * @return 相应数据生成器
     */
    public DataGenerator getGenerator(Integer type) {
        Integer mockType = Optional.ofNullable(type).orElse(MockTypeEnum.NONE.getType());
        return Optional.ofNullable(MOCK_TYPE_DATA_GENERATOR_MAP.get(mockType)).orElse(MOCK_TYPE_DATA_GENERATOR_MAP.get(MockTypeEnum.NONE.getType()));
    }

}
