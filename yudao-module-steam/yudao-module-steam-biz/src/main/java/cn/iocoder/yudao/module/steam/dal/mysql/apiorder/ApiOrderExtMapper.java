package cn.iocoder.yudao.module.steam.dal.mysql.apiorder;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.apiorder.ApiOrderExtDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 新版订单 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ApiOrderExtMapper extends BaseMapperX<ApiOrderExtDO> {
}