package cn.iocoder.yudao.module.haoka.dal.mysql.ordersource;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.ordersource.OrderSourceDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.ordersource.vo.*;

/**
 * 订单来源配置 Mapper
 *
 * @author xiongxiong
 */
@Mapper
public interface OrderSourceMapper extends BaseMapperX<OrderSourceDO> {

    default PageResult<OrderSourceDO> selectPage(OrderSourcePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderSourceDO>()
                .eqIfPresent(OrderSourceDO::getSourceRemark, reqVO.getSourceRemark())
                .eqIfPresent(OrderSourceDO::getChannel, reqVO.getChannel())
                .betweenIfPresent(OrderSourceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrderSourceDO::getId));
    }

}