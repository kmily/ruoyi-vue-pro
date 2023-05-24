package cn.iocoder.yudao.module.oa.service.customer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.CustomerCreateReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.CustomerExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.customer.vo.CustomerUpdateReqVO;
import cn.iocoder.yudao.module.oa.convert.customer.CustomerConvert;
import cn.iocoder.yudao.module.oa.dal.dataobject.customer.CustomerDO;
import cn.iocoder.yudao.module.oa.dal.mysql.customer.CustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;

/**
 * 客户管理 Service 实现类
 *
 * @author 管理员
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
    public PageResult<CustomerDO> getCustomerPage(CustomerPageReqVO pageReqVO) {
        return customerMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CustomerDO> getCustomerList(CustomerExportReqVO exportReqVO) {
        return customerMapper.selectList(exportReqVO);
    }

}
