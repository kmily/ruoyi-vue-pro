package cn.iocoder.yudao.framework.easytrans.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 数据翻译基础接口- 基于责任链模式实现
 *
 * @author HUIHUI
 */
public interface DataTranslationHandler {

    /**
     * 根据 ids 查询
     *
     * @param ids         编号列表
     * @param handlerType 处理器类型
     * @return 数据列表
     */
    default List<Map<String, Object>> selectByIds(String handlerType, List<Object> ids) {
        return Collections.emptyList();
    }

    /**
     * 根据编号查询
     *
     * @param o           编号
     * @param handlerType 处理器类型
     * @return 数据
     */
    default Map<String, Object> selectById(String handlerType, Object o) {
        return Collections.emptyMap();
    }

}
