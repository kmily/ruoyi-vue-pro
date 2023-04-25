package cn.iocoder.yudao.module.system.service.customer;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.customer.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.customer.CustomerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 客户管理 Service 接口
 *
 * @author 管理员
 */
public interface CustomerService {

    /**
     * 创建客户管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCustomer(@Valid CustomerCreateReqVO createReqVO);

    /**
     * 更新客户管理
     *
     * @param updateReqVO 更新信息
     */
    void updateCustomer(@Valid CustomerUpdateReqVO updateReqVO);

    /**
     * 删除客户管理
     *
     * @param id 编号
     */
    void deleteCustomer(Long id);

    /**
     * 获得客户管理
     *
     * @param id 编号
     * @return 客户管理
     */
    CustomerDO getCustomer(Long id);

    /**
     * 获得客户管理列表
     *
     * @param ids 编号
     * @return 客户管理列表
     */
    List<CustomerDO> getCustomerList(Collection<Long> ids);

    /**
     * 获得客户管理分页
     *
     * @param pageReqVO 分页查询
     * @return 客户管理分页
     */
    PageResult<CustomerDO> getCustomerPage(CustomerPageReqVO pageReqVO);

    /**
     * 获得客户管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 客户管理列表
     */
    List<CustomerDO> getCustomerList(CustomerExportReqVO exportReqVO);

}
