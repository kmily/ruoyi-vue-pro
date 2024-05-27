package cn.iocoder.yudao.framework.mybatis.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BaseMapperC<T> extends BaseMapper<T> {
    /**
     * 物理删除
     * @param id
     * @return
     */
    int deleteAbsoluteById(LambdaQueryWrapper<T> queryWrapper);

}
