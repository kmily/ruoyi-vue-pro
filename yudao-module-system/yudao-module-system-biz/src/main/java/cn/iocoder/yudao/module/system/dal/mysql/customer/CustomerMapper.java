package cn.iocoder.yudao.module.system.dal.mysql.customer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.customer.CustomerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.customer.vo.*;

/**
 * 客户管理 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface CustomerMapper extends BaseMapperX<CustomerDO> {

    default PageResult<CustomerDO> selectPage(CustomerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomerDO>()
                .likeIfPresent(CustomerDO::getContactName, reqVO.getContactName())
                .eqIfPresent(CustomerDO::getContactPhone, reqVO.getContactPhone())
                .eqIfPresent(CustomerDO::getCreateBy, reqVO.getCreateBy())
                .likeIfPresent(CustomerDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(CustomerDO::getCustomerType, reqVO.getCustomerType())
                .eqIfPresent(CustomerDO::getCity, reqVO.getCity())
                .orderByDesc(CustomerDO::getId));
    }

    default List<CustomerDO> selectList(CustomerExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CustomerDO>()
                .likeIfPresent(CustomerDO::getContactName, reqVO.getContactName())
                .eqIfPresent(CustomerDO::getContactPhone, reqVO.getContactPhone())
                .eqIfPresent(CustomerDO::getCreateBy, reqVO.getCreateBy())
                .likeIfPresent(CustomerDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(CustomerDO::getCustomerType, reqVO.getCustomerType())
                .eqIfPresent(CustomerDO::getCity, reqVO.getCity())
                .orderByDesc(CustomerDO::getId));
    }

}
