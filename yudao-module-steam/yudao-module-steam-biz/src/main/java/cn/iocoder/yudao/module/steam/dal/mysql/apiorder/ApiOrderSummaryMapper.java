package cn.iocoder.yudao.module.steam.dal.mysql.apiorder;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderSummaryDo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 新版订单 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ApiOrderSummaryMapper extends BaseMapperX<ApiOrderSummaryDo> {
}