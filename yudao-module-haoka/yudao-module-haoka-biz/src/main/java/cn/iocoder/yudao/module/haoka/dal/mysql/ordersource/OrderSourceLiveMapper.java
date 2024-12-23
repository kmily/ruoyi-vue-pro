package cn.iocoder.yudao.module.haoka.dal.mysql.ordersource;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceLiveDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单来源-直播间配置 Mapper
 *
 * @author xiongxiong
 */
@Mapper
public interface OrderSourceLiveMapper extends BaseMapperX<OrderSourceLiveDO> {

    default PageResult<OrderSourceLiveDO> selectPage(PageParam reqVO, Long sourceId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderSourceLiveDO>()
            .eq(OrderSourceLiveDO::getSourceId, sourceId)
            .orderByDesc(OrderSourceLiveDO::getId));
    }

    default int deleteBySourceId(Long sourceId) {
        return delete(OrderSourceLiveDO::getSourceId, sourceId);
    }

}