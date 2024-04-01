package cn.iocoder.yudao.module.steam.service.hotwords;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.hotwords.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.hotwords.HotWordsDO;
import cn.iocoder.yudao.module.steam.dal.mysql.hotwords.HotWordsMapper;
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
 * {@link HotWordsServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(HotWordsServiceImpl.class)
public class HotWordsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private HotWordsServiceImpl hotWordsService;

    @Resource
    private HotWordsMapper hotWordsMapper;

    @Test
    public void testCreateHotWords_success() {
        // 准备参数
        HotWordsSaveReqVO createReqVO = randomPojo(HotWordsSaveReqVO.class).setId(null);

        // 调用
        Integer hotWordsId = hotWordsService.createHotWords(createReqVO);
        // 断言
        assertNotNull(hotWordsId);
        // 校验记录的属性是否正确
        HotWordsDO hotWords = hotWordsMapper.selectById(hotWordsId);
        assertPojoEquals(createReqVO, hotWords, "id");
    }

    @Test
    public void testUpdateHotWords_success() {
        // mock 数据
        HotWordsDO dbHotWords = randomPojo(HotWordsDO.class);
        hotWordsMapper.insert(dbHotWords);// @Sql: 先插入出一条存在的数据
        // 准备参数
        HotWordsSaveReqVO updateReqVO = randomPojo(HotWordsSaveReqVO.class, o -> {
            o.setId(dbHotWords.getId()); // 设置更新的 ID
        });

        // 调用
        hotWordsService.updateHotWords(updateReqVO);
        // 校验是否更新正确
        HotWordsDO hotWords = hotWordsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, hotWords);
    }

    @Test
    public void testUpdateHotWords_notExists() {
        // 准备参数
        HotWordsSaveReqVO updateReqVO = randomPojo(HotWordsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> hotWordsService.updateHotWords(updateReqVO), HOT_WORDS_NOT_EXISTS);
    }

    @Test
    public void testDeleteHotWords_success() {
        // mock 数据
        HotWordsDO dbHotWords = randomPojo(HotWordsDO.class);
        hotWordsMapper.insert(dbHotWords);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbHotWords.getId();

        // 调用
        hotWordsService.deleteHotWords(id);
       // 校验数据不存在了
       assertNull(hotWordsMapper.selectById(id));
    }

    @Test
    public void testDeleteHotWords_notExists() {
        // 准备参数
        /*Integer id = randomIntegerId();*/

        // 调用, 并断言异常
        assertServiceException(() -> hotWordsService.deleteHotWords(1), HOT_WORDS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetHotWordsPage() {
       // mock 数据
       HotWordsDO dbHotWords = randomPojo(HotWordsDO.class, o -> { // 等会查询到
           o.setHotWords(null);
           o.setMarketName(null);
           o.setCreateTime(null);
       });
       hotWordsMapper.insert(dbHotWords);
       // 测试 hotWords 不匹配
       hotWordsMapper.insert(cloneIgnoreId(dbHotWords, o -> o.setHotWords(null)));
       // 测试 marketName 不匹配
       hotWordsMapper.insert(cloneIgnoreId(dbHotWords, o -> o.setMarketName(null)));
       // 测试 createTime 不匹配
       hotWordsMapper.insert(cloneIgnoreId(dbHotWords, o -> o.setCreateTime(null)));
       // 准备参数
       HotWordsPageReqVO reqVO = new HotWordsPageReqVO();
       reqVO.setHotWords(null);
       reqVO.setMarketName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<HotWordsDO> pageResult = hotWordsService.getHotWordsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbHotWords, pageResult.getList().get(0));
    }

}