package cn.iocoder.yudao.module.system.service.customer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.system.controller.admin.customer.vo.CustomerCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.customer.vo.CustomerExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.customer.vo.CustomerPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.customer.vo.CustomerUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.customer.CustomerDO;
import cn.iocoder.yudao.module.system.dal.mysql.customer.CustomerMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
* {@link CustomerServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(CustomerServiceImpl.class)
public class CustomerServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CustomerServiceImpl customerService;

    @Resource
    private CustomerMapper customerMapper;

    @Test
    public void testCreateCustomer_success() {
        // 准备参数
        CustomerCreateReqVO reqVO = randomPojo(CustomerCreateReqVO.class);

        // 调用
        Long customerId = customerService.createCustomer(reqVO);
        // 断言
        assertNotNull(customerId);
        // 校验记录的属性是否正确
        CustomerDO customer = customerMapper.selectById(customerId);
        assertPojoEquals(reqVO, customer);
    }

    @Test
    public void testUpdateCustomer_success() {
        // mock 数据
        CustomerDO dbCustomer = randomPojo(CustomerDO.class);
        customerMapper.insert(dbCustomer);// @Sql: 先插入出一条存在的数据
        // 准备参数
        CustomerUpdateReqVO reqVO = randomPojo(CustomerUpdateReqVO.class, o -> {
            o.setId(dbCustomer.getId()); // 设置更新的 ID
        });

        // 调用
        customerService.updateCustomer(reqVO);
        // 校验是否更新正确
        CustomerDO customer = customerMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, customer);
    }

    @Test
    public void testUpdateCustomer_notExists() {
        // 准备参数
        CustomerUpdateReqVO reqVO = randomPojo(CustomerUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> customerService.updateCustomer(reqVO), CUSTOMER_NOT_EXISTS);
    }

    @Test
    public void testDeleteCustomer_success() {
        // mock 数据
        CustomerDO dbCustomer = randomPojo(CustomerDO.class);
        customerMapper.insert(dbCustomer);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCustomer.getId();

        // 调用
        customerService.deleteCustomer(id);
       // 校验数据不存在了
       assertNull(customerMapper.selectById(id));
    }

    @Test
    public void testDeleteCustomer_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        //assertServiceException(() -> customerService.deleteCustomer(id), CUSTOMER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCustomerPage() {
       // mock 数据
       CustomerDO dbCustomer = randomPojo(CustomerDO.class, o -> { // 等会查询到
           o.setContactName(null);
           o.setContactPhone(null);
           o.setCreator(null);
           o.setCustomerName(null);
           o.setCustomerType(null);
           o.setCity(null);
       });
       customerMapper.insert(dbCustomer);
       // 测试 contactName 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setContactName(null)));
       // 测试 contactPhone 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setContactPhone(null)));
       // 测试 createBy 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCreator(null)));
       // 测试 customerName 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCustomerName(null)));
       // 测试 customerType 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCustomerType(null)));
       // 测试 city 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCity(null)));
       // 准备参数
       CustomerPageReqVO reqVO = new CustomerPageReqVO();
       reqVO.setContactName(null);
       reqVO.setContactPhone(null);
       reqVO.setCreator(null);
       reqVO.setCustomerName(null);
       reqVO.setCustomerType(null);
       reqVO.setCity(null);

       // 调用
       PageResult<CustomerDO> pageResult = customerService.getCustomerPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCustomer, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCustomerList() {
       // mock 数据
       CustomerDO dbCustomer = randomPojo(CustomerDO.class, o -> { // 等会查询到
           o.setContactName(null);
           o.setContactPhone(null);
           o.setCreator(null);
           o.setCustomerName(null);
           o.setCustomerType(null);
           o.setCity(null);
       });
       customerMapper.insert(dbCustomer);
       // 测试 contactName 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setContactName(null)));
       // 测试 contactPhone 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setContactPhone(null)));
       // 测试 createBy 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCreator(null)));
       // 测试 customerName 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCustomerName(null)));
       // 测试 customerType 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCustomerType(null)));
       // 测试 city 不匹配
       customerMapper.insert(cloneIgnoreId(dbCustomer, o -> o.setCity(null)));
       // 准备参数
       CustomerExportReqVO reqVO = new CustomerExportReqVO();
       reqVO.setContactName(null);
       reqVO.setContactPhone(null);
       reqVO.setCreator(null);
       reqVO.setCustomerName(null);
       reqVO.setCustomerType(null);
       reqVO.setCity(null);

       // 调用
       List<CustomerDO> list = customerService.getCustomerList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbCustomer, list.get(0));
    }

}
