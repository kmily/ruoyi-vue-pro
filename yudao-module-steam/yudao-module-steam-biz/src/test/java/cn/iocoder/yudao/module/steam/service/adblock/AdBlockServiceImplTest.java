package cn.iocoder.yudao.module.steam.service.adblock;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.adblock.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.adblock.AdBlockDO;
import cn.iocoder.yudao.module.steam.dal.mysql.adblock.AdBlockMapper;
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
 * {@link AdBlockServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(AdBlockServiceImpl.class)
public class AdBlockServiceImplTest extends BaseDbUnitTest {

    @Resource
    private AdBlockServiceImpl adBlockService;

    @Resource
    private AdBlockMapper adBlockMapper;

    @Test
    public void testCreateAdBlock_success() {
        // 准备参数
        AdBlockSaveReqVO createReqVO = randomPojo(AdBlockSaveReqVO.class).setId(null);

        // 调用
        Long adBlockId = adBlockService.createAdBlock(createReqVO);
        // 断言
        assertNotNull(adBlockId);
        // 校验记录的属性是否正确
        AdBlockDO adBlock = adBlockMapper.selectById(adBlockId);
        assertPojoEquals(createReqVO, adBlock, "id");
    }

    @Test
    public void testUpdateAdBlock_success() {
        // mock 数据
        AdBlockDO dbAdBlock = randomPojo(AdBlockDO.class);
        adBlockMapper.insert(dbAdBlock);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AdBlockSaveReqVO updateReqVO = randomPojo(AdBlockSaveReqVO.class, o -> {
            o.setId(dbAdBlock.getId()); // 设置更新的 ID
        });

        // 调用
        adBlockService.updateAdBlock(updateReqVO);
        // 校验是否更新正确
        AdBlockDO adBlock = adBlockMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, adBlock);
    }

    @Test
    public void testUpdateAdBlock_notExists() {
        // 准备参数
        AdBlockSaveReqVO updateReqVO = randomPojo(AdBlockSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> adBlockService.updateAdBlock(updateReqVO), AD_BLOCK_NOT_EXISTS);
    }

    @Test
    public void testDeleteAdBlock_success() {
        // mock 数据
        AdBlockDO dbAdBlock = randomPojo(AdBlockDO.class);
        adBlockMapper.insert(dbAdBlock);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbAdBlock.getId();

        // 调用
        adBlockService.deleteAdBlock(id);
       // 校验数据不存在了
       assertNull(adBlockMapper.selectById(id));
    }

    @Test
    public void testDeleteAdBlock_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> adBlockService.deleteAdBlock(id), AD_BLOCK_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetAdBlockPage() {
       // mock 数据
       AdBlockDO dbAdBlock = randomPojo(AdBlockDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setAdName(null);
       });
       adBlockMapper.insert(dbAdBlock);
       // 测试 createTime 不匹配
       adBlockMapper.insert(cloneIgnoreId(dbAdBlock, o -> o.setCreateTime(null)));
       // 测试 adName 不匹配
       adBlockMapper.insert(cloneIgnoreId(dbAdBlock, o -> o.setAdName(null)));
       // 准备参数
       AdBlockPageReqVO reqVO = new AdBlockPageReqVO();
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setAdName(null);

       // 调用
       PageResult<AdBlockDO> pageResult = adBlockService.getAdBlockPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbAdBlock, pageResult.getList().get(0));
    }

}