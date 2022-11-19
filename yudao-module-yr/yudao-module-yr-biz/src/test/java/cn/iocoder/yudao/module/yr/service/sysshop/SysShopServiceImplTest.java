package cn.iocoder.yudao.module.system.service.sysshop;

import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopPageReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopUpdateReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.sysshop.SysShopDO;
import cn.iocoder.yudao.module.yr.dal.mysql.sys.sysshop.SysShopMapper;
import cn.iocoder.yudao.module.yr.service.sys.sysshop.SysShopServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;


import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.yr.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link SysShopServiceImpl} 的单元测试类
*
* @author 芋道源码
*/
@Import(SysShopServiceImpl.class)
public class SysShopServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SysShopServiceImpl sysShopService;

    @Resource
    private SysShopMapper sysShopMapper;

    @Test
    public void testCreateSysShop_success() {
        // 准备参数
        SysShopCreateReqVO reqVO = randomPojo(SysShopCreateReqVO.class);

        // 调用
        Long sysShopId = sysShopService.createSysShop(reqVO);
        // 断言
        assertNotNull(sysShopId);
        // 校验记录的属性是否正确
        SysShopDO sysShop = sysShopMapper.selectById(sysShopId);
        assertPojoEquals(reqVO, sysShop);
    }

    @Test
    public void testUpdateSysShop_success() {
        // mock 数据
        SysShopDO dbSysShop = randomPojo(SysShopDO.class);
        sysShopMapper.insert(dbSysShop);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SysShopUpdateReqVO reqVO = randomPojo(SysShopUpdateReqVO.class, o -> {
            o.setId(dbSysShop.getId()); // 设置更新的 ID
        });

        // 调用
        sysShopService.updateSysShop(reqVO);
        // 校验是否更新正确
        SysShopDO sysShop = sysShopMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, sysShop);
    }

    @Test
    public void testUpdateSysShop_notExists() {
        // 准备参数
        SysShopUpdateReqVO reqVO = randomPojo(SysShopUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> sysShopService.updateSysShop(reqVO), SYS_SHOP_NOT_EXISTS);
    }

    @Test
    public void testDeleteSysShop_success() {
        // mock 数据
        SysShopDO dbSysShop = randomPojo(SysShopDO.class);
        sysShopMapper.insert(dbSysShop);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSysShop.getId();

        // 调用
        sysShopService.deleteSysShop(id);
       // 校验数据不存在了
       assertNull(sysShopMapper.selectById(id));
    }

    @Test
    public void testDeleteSysShop_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> sysShopService.deleteSysShop(id), SYS_SHOP_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSysShopPage() {
       // mock 数据
       SysShopDO dbSysShop = randomPojo(SysShopDO.class, o -> { // 等会查询到
           o.setShopName(null);
           o.setShopCity(null);
           o.setShopAddress(null);
           o.setShopAddressNum(null);
           o.setShopGroup(null);
           o.setStatus(null);
           o.setCreateTime(null);
       });
       sysShopMapper.insert(dbSysShop);
       // 测试 shopName 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopName(null)));
       // 测试 shopCity 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopCity(null)));
       // 测试 shopAddress 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopAddress(null)));
       // 测试 shopAddressNum 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopAddressNum(null)));
       // 测试 shopGroup 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopGroup(null)));
       // 测试 status 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setStatus(null)));
       // 测试 createTime 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setCreateTime(null)));
       // 准备参数
       SysShopPageReqVO reqVO = new SysShopPageReqVO();
       reqVO.setShopName(null);
       reqVO.setShopCity(null);
       reqVO.setShopAddress(null);
       reqVO.setShopAddressNum(null);
       reqVO.setShopGroup(null);
       reqVO.setStatus(null);
       reqVO.setCreateTime((new Date[]{}));

       // 调用
       PageResult<SysShopDO> pageResult = sysShopService.getSysShopPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSysShop, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSysShopList() {
       // mock 数据
       SysShopDO dbSysShop = randomPojo(SysShopDO.class, o -> { // 等会查询到
           o.setShopName(null);
           o.setShopCity(null);
           o.setShopAddress(null);
           o.setShopAddressNum(null);
           o.setShopGroup(null);
           o.setStatus(null);
           o.setCreateTime(null);
       });
       sysShopMapper.insert(dbSysShop);
       // 测试 shopName 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopName(null)));
       // 测试 shopCity 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopCity(null)));
       // 测试 shopAddress 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopAddress(null)));
       // 测试 shopAddressNum 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopAddressNum(null)));
       // 测试 shopGroup 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setShopGroup(null)));
       // 测试 status 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setStatus(null)));
       // 测试 createTime 不匹配
       sysShopMapper.insert(cloneIgnoreId(dbSysShop, o -> o.setCreateTime(null)));
       // 准备参数
       SysShopExportReqVO reqVO = new SysShopExportReqVO();
       reqVO.setShopName(null);
       reqVO.setShopCity(null);
       reqVO.setShopAddress(null);
       reqVO.setShopAddressNum(null);
       reqVO.setShopGroup(null);
       reqVO.setStatus(null);
       reqVO.setCreateTime((new Date[]{}));

       // 调用
       List<SysShopDO> list = sysShopService.getSysShopList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbSysShop, list.get(0));
    }

}
