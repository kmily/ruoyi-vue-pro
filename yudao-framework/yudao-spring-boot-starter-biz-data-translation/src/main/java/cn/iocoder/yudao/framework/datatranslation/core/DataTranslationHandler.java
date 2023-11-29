package cn.iocoder.yudao.framework.datatranslation.core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 数据翻译数据源获取基础接口
 *
 * @author HUIHUI
 */
public interface DataTranslationHandler {

    /**
     * 根据 ids 查询
     *
     * @param ids 编号列表
     * @return 数据列表
     */
    default List<Map<String, Object>> selectByIds(Collection<?> ids) {
        return Collections.emptyList();
    }

    /**
     * 根据编号查询
     *
     * @param o 编号
     * @return 数据
     */
    default Map<String, Object> selectById(Object o) {
        return Collections.emptyMap();
    }

}
