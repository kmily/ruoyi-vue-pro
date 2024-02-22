package cn.iocoder.yudao.module.steam.service.inv;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
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
 * {@link InvServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(InvServiceImpl.class)
public class InvServiceImplTest extends BaseDbUnitTest {

    @Resource
    private InvServiceImpl invService;

    @Resource
    private InvMapper invMapper;

    @Test
    public void testCreateInv_success() {
        // 准备参数
        InvSaveReqVO createReqVO = randomPojo(InvSaveReqVO.class).setId(null);

        // 调用
        Long invId = invService.createInv(createReqVO);
        // 断言
        assertNotNull(invId);
        // 校验记录的属性是否正确
        InvDO inv = invMapper.selectById(invId);
        assertPojoEquals(createReqVO, inv, "id");
    }

    @Test
    public void testUpdateInv_success() {
        // mock 数据
        InvDO dbInv = randomPojo(InvDO.class);
        invMapper.insert(dbInv);// @Sql: 先插入出一条存在的数据
        // 准备参数
        InvSaveReqVO updateReqVO = randomPojo(InvSaveReqVO.class, o -> {
            o.setId(dbInv.getId()); // 设置更新的 ID
        });

        // 调用
        invService.updateInv(updateReqVO);
        // 校验是否更新正确
        InvDO inv = invMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, inv);
    }

    @Test
    public void testUpdateInv_notExists() {
        // 准备参数
        InvSaveReqVO updateReqVO = randomPojo(InvSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> invService.updateInv(updateReqVO), INV_NOT_EXISTS);
    }

    @Test
    public void testDeleteInv_success() {
        // mock 数据
        InvDO dbInv = randomPojo(InvDO.class);
        invMapper.insert(dbInv);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbInv.getId();

        // 调用
        invService.deleteInv(id);
       // 校验数据不存在了
       assertNull(invMapper.selectById(id));
    }

    @Test
    public void testDeleteInv_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> invService.deleteInv(id), INV_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetInvPage() {
       // mock 数据
       InvDO dbInv = randomPojo(InvDO.class, o -> { // 等会查询到
           o.setAssetid(null);
           o.setClassid(null);
           o.setInstanceid(null);
           o.setAmount(null);
           o.setCreateTime(null);
           o.setSteamId(null);
           o.setPrice(null);
           o.setAppid(null);
           o.setBindUserId(null);
           o.setStatus(null);
           o.setTransferStatus(null);
           o.setUserId(null);
           o.setUserType(null);
       });
       invMapper.insert(dbInv);
       // 测试 assetid 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setAssetid(null)));
       // 测试 classid 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setClassid(null)));
       // 测试 instanceid 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setInstanceid(null)));
       // 测试 amount 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setAmount(null)));
       // 测试 createTime 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setCreateTime(null)));
       // 测试 steamId 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setSteamId(null)));
       // 测试 price 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setPrice(null)));
       // 测试 appid 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setAppid(null)));
       // 测试 bindUserId 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setBindUserId(null)));
       // 测试 status 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setStatus(null)));
       // 测试 transferStatus 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setTransferStatus(null)));
       // 测试 userId 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setUserId(null)));
       // 测试 userType 不匹配
       invMapper.insert(cloneIgnoreId(dbInv, o -> o.setUserType(null)));
       // 准备参数
       InvPageReqVO reqVO = new InvPageReqVO();
       reqVO.setAssetid(null);
       reqVO.setClassid(null);
       reqVO.setInstanceid(null);
       reqVO.setAmount(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setSteamId(null);
       reqVO.setPrice(null);
       reqVO.setAppid(null);
       reqVO.setBindUserId(null);
       reqVO.setStatus(null);
       reqVO.setTransferStatus(null);
       reqVO.setUserId(null);
       reqVO.setUserType(null);

       // 调用
       PageResult<InvDO> pageResult = invService.getInvPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbInv, pageResult.getList().get(0));
    }

}