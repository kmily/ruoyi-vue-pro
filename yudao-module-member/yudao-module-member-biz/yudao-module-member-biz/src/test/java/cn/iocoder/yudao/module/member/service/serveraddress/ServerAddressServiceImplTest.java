package cn.iocoder.yudao.module.member.service.serveraddress;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serveraddress.ServerAddressDO;
import cn.iocoder.yudao.module.member.dal.mysql.serveraddress.ServerAddressMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link ServerAddressServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(ServerAddressServiceImpl.class)
public class ServerAddressServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ServerAddressServiceImpl serverAddressService;

    @Resource
    private ServerAddressMapper serverAddressMapper;

    @Test
    public void testCreateServerAddress_success() {
        // 准备参数
        ServerAddressCreateReqVO reqVO = randomPojo(ServerAddressCreateReqVO.class);

        // 调用
        Long serverAddressId = serverAddressService.createServerAddress(reqVO);
        // 断言
        assertNotNull(serverAddressId);
        // 校验记录的属性是否正确
        ServerAddressDO serverAddress = serverAddressMapper.selectById(serverAddressId);
        assertPojoEquals(reqVO, serverAddress);
    }

    @Test
    public void testUpdateServerAddress_success() {
        // mock 数据
        ServerAddressDO dbServerAddress = randomPojo(ServerAddressDO.class);
        serverAddressMapper.insert(dbServerAddress);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ServerAddressUpdateReqVO reqVO = randomPojo(ServerAddressUpdateReqVO.class, o -> {
            o.setId(dbServerAddress.getId()); // 设置更新的 ID
        });

        // 调用
        serverAddressService.updateServerAddress(reqVO);
        // 校验是否更新正确
        ServerAddressDO serverAddress = serverAddressMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, serverAddress);
    }

    @Test
    public void testUpdateServerAddress_notExists() {
        // 准备参数
        ServerAddressUpdateReqVO reqVO = randomPojo(ServerAddressUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> serverAddressService.updateServerAddress(reqVO), SERVER_ADDRESS_NOT_EXISTS);
    }

    @Test
    public void testDeleteServerAddress_success() {
        // mock 数据
        ServerAddressDO dbServerAddress = randomPojo(ServerAddressDO.class);
        serverAddressMapper.insert(dbServerAddress);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbServerAddress.getId();

        // 调用
        serverAddressService.deleteServerAddress(id);
       // 校验数据不存在了
       assertNull(serverAddressMapper.selectById(id));
    }

    @Test
    public void testDeleteServerAddress_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> serverAddressService.deleteServerAddress(id), SERVER_ADDRESS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetServerAddressPage() {
       // mock 数据
       ServerAddressDO dbServerAddress = randomPojo(ServerAddressDO.class, o -> { // 等会查询到
           o.setUserId(null);
           o.setAreaId(null);
           o.setAddress(null);
           o.setDetailAddress(null);
           o.setDefaultStatus(null);
           o.setCoordinate(null);
           o.setCreateTime(null);
       });
       serverAddressMapper.insert(dbServerAddress);
       // 测试 userId 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setUserId(null)));
       // 测试 areaId 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setAreaId(null)));
       // 测试 address 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setAddress(null)));
       // 测试 detailAddress 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setDetailAddress(null)));
       // 测试 defaultStatus 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setDefaultStatus(null)));
       // 测试 coordinate 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setCoordinate(null)));
       // 测试 createTime 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setCreateTime(null)));
       // 准备参数
       ServerAddressPageReqVO reqVO = new ServerAddressPageReqVO();
       reqVO.setUserId(null);
       reqVO.setAreaId(null);
       reqVO.setAddress(null);
       reqVO.setDetailAddress(null);
       reqVO.setDefaultStatus(null);
       reqVO.setCoordinate(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ServerAddressDO> pageResult = serverAddressService.getServerAddressPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbServerAddress, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetServerAddressList() {
       // mock 数据
       ServerAddressDO dbServerAddress = randomPojo(ServerAddressDO.class, o -> { // 等会查询到
           o.setUserId(null);
           o.setAreaId(null);
           o.setAddress(null);
           o.setDetailAddress(null);
           o.setDefaultStatus(null);
           o.setCoordinate(null);
           o.setCreateTime(null);
       });
       serverAddressMapper.insert(dbServerAddress);
       // 测试 userId 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setUserId(null)));
       // 测试 areaId 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setAreaId(null)));
       // 测试 address 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setAddress(null)));
       // 测试 detailAddress 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setDetailAddress(null)));
       // 测试 defaultStatus 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setDefaultStatus(null)));
       // 测试 coordinate 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setCoordinate(null)));
       // 测试 createTime 不匹配
       serverAddressMapper.insert(cloneIgnoreId(dbServerAddress, o -> o.setCreateTime(null)));
       // 准备参数
       ServerAddressExportReqVO reqVO = new ServerAddressExportReqVO();
       reqVO.setUserId(null);
       reqVO.setAreaId(null);
       reqVO.setAddress(null);
       reqVO.setDetailAddress(null);
       reqVO.setDefaultStatus(null);
       reqVO.setCoordinate(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       List<ServerAddressDO> list = serverAddressService.getServerAddressList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbServerAddress, list.get(0));
    }

}
