package cn.iocoder.yudao.framework.easytrans.core;

import com.fhs.core.trans.vo.TransPojo;
import com.fhs.trans.service.AutoTransable;

import java.util.Collections;
import java.util.List;

/**
 * VO 数据翻译扩展
 *
 * @author HUIHUI
 */
public interface VOAutoTransable<T extends TransPojo> extends AutoTransable<T> {

    /**
     * 根据 ids 查询
     *
     * @param ids 编号列表
     * @return 数据列表
     */
    @Override
    default List<T> selectByIds(List<? extends Object> ids) {
        return Collections.emptyList();
    }

    /**
     * 获得所有数据
     *
     * @return 所有数据
     */
    @Override
    default List<T> select() {
        return Collections.emptyList();
    }

    /**
     * 根据编号查询
     *
     * @param o 编号
     * @return T
     */
    @Override
    default T selectById(Object o) {
        return null;
    }

}
