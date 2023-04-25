package cn.iocoder.yudao.module.oa.service.contract;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.oa.controller.admin.contract.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.contract.ContractDO;
import cn.iocoder.yudao.module.oa.dal.mysql.contract.ContractMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link ContractServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(ContractServiceImpl.class)
public class ContractServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ContractServiceImpl contractService;

    @Resource
    private ContractMapper contractMapper;

    @Test
    public void testCreateContract_success() {
        // 准备参数
        ContractCreateReqVO reqVO = randomPojo(ContractCreateReqVO.class);

        // 调用
        Long contractId = contractService.createContract(reqVO);
        // 断言
        assertNotNull(contractId);
        // 校验记录的属性是否正确
        ContractDO contract = contractMapper.selectById(contractId);
        assertPojoEquals(reqVO, contract);
    }

    @Test
    public void testUpdateContract_success() {
        // mock 数据
        ContractDO dbContract = randomPojo(ContractDO.class);
        contractMapper.insert(dbContract);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ContractUpdateReqVO reqVO = randomPojo(ContractUpdateReqVO.class, o -> {
            o.setId(dbContract.getId()); // 设置更新的 ID
        });

        // 调用
        contractService.updateContract(reqVO);
        // 校验是否更新正确
        ContractDO contract = contractMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, contract);
    }

    @Test
    public void testUpdateContract_notExists() {
        // 准备参数
        ContractUpdateReqVO reqVO = randomPojo(ContractUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> contractService.updateContract(reqVO), CONTRACT_NOT_EXISTS);
    }

    @Test
    public void testDeleteContract_success() {
        // mock 数据
        ContractDO dbContract = randomPojo(ContractDO.class);
        contractMapper.insert(dbContract);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbContract.getId();

        // 调用
        contractService.deleteContract(id);
       // 校验数据不存在了
       assertNull(contractMapper.selectById(id));
    }

    @Test
    public void testDeleteContract_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> contractService.deleteContract(id), CONTRACT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetContractPage() {
       // mock 数据
       ContractDO dbContract = randomPojo(ContractDO.class, o -> { // 等会查询到
           o.setContractNo(null);
           o.setStatus(null);
           o.setApprovalStatus(null);
           o.setCreateBy(null);
       });
       contractMapper.insert(dbContract);
       // 测试 contractNo 不匹配
       contractMapper.insert(cloneIgnoreId(dbContract, o -> o.setContractNo(null)));
       // 测试 status 不匹配
       contractMapper.insert(cloneIgnoreId(dbContract, o -> o.setStatus(null)));
       // 测试 approvalStatus 不匹配
       contractMapper.insert(cloneIgnoreId(dbContract, o -> o.setApprovalStatus(null)));
       // 测试 createBy 不匹配
       contractMapper.insert(cloneIgnoreId(dbContract, o -> o.setCreateBy(null)));
       // 准备参数
       ContractPageReqVO reqVO = new ContractPageReqVO();
       reqVO.setContractNo(null);
       reqVO.setStatus(null);
       reqVO.setApprovalStatus(null);
       reqVO.setCreateBy(null);

       // 调用
       PageResult<ContractDO> pageResult = contractService.getContractPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbContract, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetContractList() {
       // mock 数据
       ContractDO dbContract = randomPojo(ContractDO.class, o -> { // 等会查询到
           o.setContractNo(null);
           o.setStatus(null);
           o.setApprovalStatus(null);
           o.setCreateBy(null);
       });
       contractMapper.insert(dbContract);
       // 测试 contractNo 不匹配
       contractMapper.insert(cloneIgnoreId(dbContract, o -> o.setContractNo(null)));
       // 测试 status 不匹配
       contractMapper.insert(cloneIgnoreId(dbContract, o -> o.setStatus(null)));
       // 测试 approvalStatus 不匹配
       contractMapper.insert(cloneIgnoreId(dbContract, o -> o.setApprovalStatus(null)));
       // 测试 createBy 不匹配
       contractMapper.insert(cloneIgnoreId(dbContract, o -> o.setCreateBy(null)));
       // 准备参数
       ContractExportReqVO reqVO = new ContractExportReqVO();
       reqVO.setContractNo(null);
       reqVO.setStatus(null);
       reqVO.setApprovalStatus(null);
       reqVO.setCreateBy(null);

       // 调用
       List<ContractDO> list = contractService.getContractList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbContract, list.get(0));
    }

}
