package cn.iocoder.yudao.module.steam.dal.mysql.ad;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.ad.AdDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 广告 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface AdMapper extends BaseMapperX<AdDO> {

    default List<AdDO> selectListByBlockId(Long blockId) {
        return selectList(AdDO::getBlockId, blockId);
    }

    default int deleteByBlockId(Long blockId) {
        return delete(AdDO::getBlockId, blockId);
    }

}