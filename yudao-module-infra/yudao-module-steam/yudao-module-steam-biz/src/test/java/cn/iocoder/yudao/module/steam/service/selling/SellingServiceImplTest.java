package cn.iocoder.yudao.module.steam.service.selling;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
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
 * {@link SellingServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(SellingServiceImpl.class)
public class SellingServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SellingServiceImpl sellingService;

    @Resource
    private SellingMapper sellingMapper;

    @Test
    public void testCreateSelling_success() {
        // 准备参数
        SellingSaveReqVO createReqVO = randomPojo(SellingSaveReqVO.class).setId(null);

        // 调用
        Long sellingId = sellingService.createSelling(createReqVO);
        // 断言
        assertNotNull(sellingId);
        // 校验记录的属性是否正确
        SellingDO selling = sellingMapper.selectById(sellingId);
        assertPojoEquals(createReqVO, selling, "id");
    }

    @Test
    public void testUpdateSelling_success() {
        // mock 数据
        SellingDO dbSelling = randomPojo(SellingDO.class);
        sellingMapper.insert(dbSelling);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SellingSaveReqVO updateReqVO = randomPojo(SellingSaveReqVO.class, o -> {
            o.setId(dbSelling.getId()); // 设置更新的 ID
        });

        // 调用
        sellingService.updateSelling(updateReqVO);
        // 校验是否更新正确
        SellingDO selling = sellingMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, selling);
    }

    @Test
    public void testUpdateSelling_notExists() {
        // 准备参数
        SellingSaveReqVO updateReqVO = randomPojo(SellingSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> sellingService.updateSelling(updateReqVO), SELLING_NOT_EXISTS);
    }

    @Test
    public void testDeleteSelling_success() {
        // mock 数据
        SellingDO dbSelling = randomPojo(SellingDO.class);
        sellingMapper.insert(dbSelling);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSelling.getId();

        // 调用
        sellingService.deleteSelling(id);
       // 校验数据不存在了
       assertNull(sellingMapper.selectById(id));
    }

    @Test
    public void testDeleteSelling_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> sellingService.deleteSelling(id), SELLING_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSellingPage() {
       // mock 数据
       SellingDO dbSelling = randomPojo(SellingDO.class, o -> { // 等会查询到
           o.setAppid(null);
           o.setAssetid(null);
           o.setClassid(null);
           o.setInstanceid(null);
           o.setAmount(null);
           o.setCreateTime(null);
           o.setSteamId(null);
           o.setStatus(null);
           o.setPrice(null);
           o.setTransferStatus(null);
           o.setUserId(null);
           o.setUserType(null);
           o.setBindUserId(null);
           o.setContextid(null);
       });
       sellingMapper.insert(dbSelling);
       // 测试 appid 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setAppid(null)));
       // 测试 assetid 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setAssetid(null)));
       // 测试 classid 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setClassid(null)));
       // 测试 instanceid 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setInstanceid(null)));
       // 测试 amount 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setAmount(null)));
       // 测试 createTime 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setCreateTime(null)));
       // 测试 steamId 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setSteamId(null)));
       // 测试 status 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setStatus(null)));
       // 测试 price 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setPrice(null)));
       // 测试 transferStatus 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setTransferStatus(null)));
       // 测试 userId 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setUserId(null)));
       // 测试 userType 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setUserType(null)));
       // 测试 bindUserId 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setBindUserId(null)));
       // 测试 contextid 不匹配
       sellingMapper.insert(cloneIgnoreId(dbSelling, o -> o.setContextid(null)));
       // 准备参数
       SellingPageReqVO reqVO = new SellingPageReqVO();
       reqVO.setAppid(null);
       reqVO.setAssetid(null);
       reqVO.setClassid(null);
       reqVO.setInstanceid(null);
       reqVO.setAmount(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setSteamId(null);
       reqVO.setStatus(null);
       reqVO.setPrice(null);
       reqVO.setTransferStatus(null);
       reqVO.setUserId(null);
       reqVO.setUserType(null);
       reqVO.setBindUserId(null);
       reqVO.setContextid(null);

       // 调用
       PageResult<SellingDO> pageResult = sellingService.getSellingPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSelling, pageResult.getList().get(0));
    }

}