package cn.iocoder.yudao.module.steam.service.selrarity;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selrarity.SelRarityDO;
import cn.iocoder.yudao.module.steam.dal.mysql.selrarity.SelRarityMapper;
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
 * {@link SelRarityServiceImpl} 的单元测试类
 *
 * @author lgm
 */
@Import(SelRarityServiceImpl.class)
public class SelRarityServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SelRarityServiceImpl selRarityService;

    @Resource
    private SelRarityMapper selRarityMapper;

    @Test
    public void testCreateSelRarity_success() {
        // 准备参数
        SelRaritySaveReqVO createReqVO = randomPojo(SelRaritySaveReqVO.class).setId(null);

        // 调用
        Long selRarityId = selRarityService.createSelRarity(createReqVO);
        // 断言
        assertNotNull(selRarityId);
        // 校验记录的属性是否正确
        SelRarityDO selRarity = selRarityMapper.selectById(selRarityId);
        assertPojoEquals(createReqVO, selRarity, "id");
    }

    @Test
    public void testUpdateSelRarity_success() {
        // mock 数据
        SelRarityDO dbSelRarity = randomPojo(SelRarityDO.class);
        selRarityMapper.insert(dbSelRarity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SelRaritySaveReqVO updateReqVO = randomPojo(SelRaritySaveReqVO.class, o -> {
            o.setId(dbSelRarity.getId()); // 设置更新的 ID
        });

        // 调用
        selRarityService.updateSelRarity(updateReqVO);
        // 校验是否更新正确
        SelRarityDO selRarity = selRarityMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, selRarity);
    }

    @Test
    public void testUpdateSelRarity_notExists() {
        // 准备参数
        SelRaritySaveReqVO updateReqVO = randomPojo(SelRaritySaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> selRarityService.updateSelRarity(updateReqVO), SEL_RARITY_NOT_EXISTS);
    }

    @Test
    public void testDeleteSelRarity_success() {
        // mock 数据
        SelRarityDO dbSelRarity = randomPojo(SelRarityDO.class);
        selRarityMapper.insert(dbSelRarity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSelRarity.getId();

        // 调用
        selRarityService.deleteSelRarity(id);
       // 校验数据不存在了
       assertNull(selRarityMapper.selectById(id));
    }

    @Test
    public void testDeleteSelRarity_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> selRarityService.deleteSelRarity(id), SEL_RARITY_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSelRarityPage() {
       // mock 数据
       SelRarityDO dbSelRarity = randomPojo(SelRarityDO.class, o -> { // 等会查询到
           o.setLocalizedTagName(null);
           o.setInternalName(null);
           o.setColor(null);
           o.setCreateTime(null);
       });
       selRarityMapper.insert(dbSelRarity);
       // 测试 localizedTagName 不匹配
       selRarityMapper.insert(cloneIgnoreId(dbSelRarity, o -> o.setLocalizedTagName(null)));
       // 测试 internalName 不匹配
       selRarityMapper.insert(cloneIgnoreId(dbSelRarity, o -> o.setInternalName(null)));
       // 测试 color 不匹配
       selRarityMapper.insert(cloneIgnoreId(dbSelRarity, o -> o.setColor(null)));
       // 测试 createTime 不匹配
       selRarityMapper.insert(cloneIgnoreId(dbSelRarity, o -> o.setCreateTime(null)));
       // 准备参数
       SelRarityPageReqVO reqVO = new SelRarityPageReqVO();
       reqVO.setLocalizedTagName(null);
       reqVO.setInternalName(null);
       reqVO.setColor(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<SelRarityDO> pageResult = selRarityService.getSelRarityPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSelRarity, pageResult.getList().get(0));
    }

}