package cn.iocoder.yudao.module.oa.dal.mysql.customer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.oa.dal.dataobject.customer.CustomerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.*;

/**
 * 客户 Mapper
 *
 * @author 东海
 */
@Mapper
public interface CustomerMapper extends BaseMapperX<CustomerDO> {

    default PageResult<CustomerDO> selectPage(CustomerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomerDO>()
                .likeIfPresent(CustomerDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(CustomerDO::getCustomerType, reqVO.getCustomerType())
                .likeIfPresent(CustomerDO::getContactName, reqVO.getContactName())
                .eqIfPresent(CustomerDO::getContactPhone, reqVO.getContactPhone())
                .eqIfPresent(CustomerDO::getProvince, reqVO.getProvince())
                .eqIfPresent(CustomerDO::getCity, reqVO.getCity())
                .eqIfPresent(CustomerDO::getDistrict, reqVO.getDistrict())
                .eqIfPresent(CustomerDO::getAddress, reqVO.getAddress())
                .likeIfPresent(CustomerDO::getBankName, reqVO.getBankName())
                .eqIfPresent(CustomerDO::getBankAccount, reqVO.getBankAccount())
                .eqIfPresent(CustomerDO::getTaxNumber, reqVO.getTaxNumber())
                .eqIfPresent(CustomerDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(CustomerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CustomerDO::getId));
    }

    default List<CustomerDO> selectList(CustomerExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CustomerDO>()
                .likeIfPresent(CustomerDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(CustomerDO::getCustomerType, reqVO.getCustomerType())
                .likeIfPresent(CustomerDO::getContactName, reqVO.getContactName())
                .eqIfPresent(CustomerDO::getContactPhone, reqVO.getContactPhone())
                .eqIfPresent(CustomerDO::getProvince, reqVO.getProvince())
                .eqIfPresent(CustomerDO::getCity, reqVO.getCity())
                .eqIfPresent(CustomerDO::getDistrict, reqVO.getDistrict())
                .eqIfPresent(CustomerDO::getAddress, reqVO.getAddress())
                .likeIfPresent(CustomerDO::getBankName, reqVO.getBankName())
                .eqIfPresent(CustomerDO::getBankAccount, reqVO.getBankAccount())
                .eqIfPresent(CustomerDO::getTaxNumber, reqVO.getTaxNumber())
                .eqIfPresent(CustomerDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(CustomerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CustomerDO::getId));
    }

}
