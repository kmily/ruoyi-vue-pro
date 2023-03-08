package cn.iocoder.yudao.module.bpm.dal.mysql.task;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.bpm.dal.dataobject.task.HiVarinstDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper
 *
 * @author 芋道
 */
@Mapper
public interface HiVarinstMapper extends BaseMapperX<HiVarinstDO> {

    @Override
    int updateById(@Param("entity") HiVarinstDO entity);
}
