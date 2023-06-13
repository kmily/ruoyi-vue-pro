package cn.iocoder.yudao.module.oa.service.customer;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.customer.CustomerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.customer.CustomerConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.customer.CustomerMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 客户 Service 实现类
 *
 * @author 东海
 */
@Service
@Validated
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Long createCustomer(CustomerCreateReqVO createReqVO) {
        // 插入
        CustomerDO customer = CustomerConvert.INSTANCE.convert(createReqVO);
        customerMapper.insert(customer);
        // 返回
        return customer.getId();
    }

    @Override
    public void updateCustomer(CustomerUpdateReqVO updateReqVO) {
        // 校验存在
        validateCustomerExists(updateReqVO.getId());
        // 更新
        CustomerDO updateObj = CustomerConvert.INSTANCE.convert(updateReqVO);
        customerMapper.updateById(updateObj);
    }

    @Override
    public void deleteCustomer(Long id) {
        // 校验存在
        validateCustomerExists(id);
        // 删除
        customerMapper.deleteById(id);
    }

    private void validateCustomerExists(Long id) {
        if (customerMapper.selectById(id) == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
    }

    @Override
    public CustomerDO getCustomer(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public List<CustomerDO> getCustomerList(Collection<Long> ids) {
        return customerMapper.selectBatchIds(ids);
    }

    @Override
    public List<CustomerDO> getCustomerByName(String name) {
        return customerMapper.selectByName(name);
    }

    @Override
    public PageResult<CustomerDO> getCustomerPage(CustomerPageReqVO pageReqVO) {
        return customerMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CustomerDO> getCustomerList(CustomerExportReqVO exportReqVO) {
        return customerMapper.selectList(exportReqVO);
    }

}
