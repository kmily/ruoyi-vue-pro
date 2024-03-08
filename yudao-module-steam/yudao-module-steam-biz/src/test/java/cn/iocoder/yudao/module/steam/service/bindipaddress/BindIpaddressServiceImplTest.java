package cn.iocoder.yudao.module.steam.service.bindipaddress;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.bindipaddress.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.bindipaddress.BindIpaddressDO;
import cn.iocoder.yudao.module.steam.dal.mysql.bindipaddress.BindIpaddressMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link BindIpaddressServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(BindIpaddressServiceImpl.class)
public class BindIpaddressServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BindIpaddressServiceImpl bindIpaddressService;

    @Resource
    private BindIpaddressMapper bindIpaddressMapper;

    @Test
    public void testCreateBindIpaddress_success() {
        // 准备参数
        BindIpaddressSaveReqVO createReqVO = randomPojo(BindIpaddressSaveReqVO.class).setId(null);

        // 调用
        Long bindIpaddressId = bindIpaddressService.createBindIpaddress(createReqVO);
        // 断言
        assertNotNull(bindIpaddressId);
        // 校验记录的属性是否正确
        BindIpaddressDO bindIpaddress = bindIpaddressMapper.selectById(bindIpaddressId);
        assertPojoEquals(createReqVO, bindIpaddress, "id");
    }

    @Test
    public void testUpdateBindIpaddress_success() {
        // mock 数据
        BindIpaddressDO dbBindIpaddress = randomPojo(BindIpaddressDO.class);
        bindIpaddressMapper.insert(dbBindIpaddress);// @Sql: 先插入出一条存在的数据
        // 准备参数
        BindIpaddressSaveReqVO updateReqVO = randomPojo(BindIpaddressSaveReqVO.class, o -> {
            o.setId(dbBindIpaddress.getId()); // 设置更新的 ID
        });

        // 调用
        bindIpaddressService.updateBindIpaddress(updateReqVO);
        // 校验是否更新正确
        BindIpaddressDO bindIpaddress = bindIpaddressMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, bindIpaddress);
    }

    @Test
    public void testUpdateBindIpaddress_notExists() {
        // 准备参数
        BindIpaddressSaveReqVO updateReqVO = randomPojo(BindIpaddressSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> bindIpaddressService.updateBindIpaddress(updateReqVO), BIND_IPADDRESS_NOT_EXISTS);
    }

    @Test
    public void testDeleteBindIpaddress_success() {
        // mock 数据
        BindIpaddressDO dbBindIpaddress = randomPojo(BindIpaddressDO.class);
        bindIpaddressMapper.insert(dbBindIpaddress);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbBindIpaddress.getId();

        // 调用
        bindIpaddressService.deleteBindIpaddress(id);
       // 校验数据不存在了
       assertNull(bindIpaddressMapper.selectById(id));
    }

    @Test
    public void testDeleteBindIpaddress_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> bindIpaddressService.deleteBindIpaddress(id), BIND_IPADDRESS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBindIpaddressPage() {
       // mock 数据
       BindIpaddressDO dbBindIpaddress = randomPojo(BindIpaddressDO.class, o -> { // 等会查询到
           o.setPort(null);
           o.setCreateTime(null);
           o.setAddressId(null);
           o.setIpAddress(null);
       });
       bindIpaddressMapper.insert(dbBindIpaddress);
       // 测试 port 不匹配
       bindIpaddressMapper.insert(cloneIgnoreId(dbBindIpaddress, o -> o.setPort(null)));
       // 测试 createTime 不匹配
       bindIpaddressMapper.insert(cloneIgnoreId(dbBindIpaddress, o -> o.setCreateTime(null)));
       // 测试 addressId 不匹配
       bindIpaddressMapper.insert(cloneIgnoreId(dbBindIpaddress, o -> o.setAddressId(null)));
       // 测试 ipAddress 不匹配
       bindIpaddressMapper.insert(cloneIgnoreId(dbBindIpaddress, o -> o.setIpAddress(null)));
       // 准备参数
       BindIpaddressPageReqVO reqVO = new BindIpaddressPageReqVO();
       reqVO.setPort(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setAddressId(null);
       reqVO.setIpAddress(null);

       // 调用
       PageResult<BindIpaddressDO> pageResult = bindIpaddressService.getBindIpaddressPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbBindIpaddress, pageResult.getList().get(0));
    }

}