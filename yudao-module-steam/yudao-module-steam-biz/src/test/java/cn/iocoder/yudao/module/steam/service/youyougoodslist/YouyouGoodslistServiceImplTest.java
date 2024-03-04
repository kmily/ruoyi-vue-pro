package cn.iocoder.yudao.module.steam.service.youyougoodslist;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.youyougoodslist.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyougoodslist.YouyouGoodslistDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyougoodslist.YouyouGoodslistMapper;
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
 * {@link YouyouGoodslistServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(YouyouGoodslistServiceImpl.class)
public class YouyouGoodslistServiceImplTest extends BaseDbUnitTest {

    @Resource
    private YouyouGoodslistServiceImpl youyouGoodslistService;

    @Resource
    private YouyouGoodslistMapper youyouGoodslistMapper;

    @Test
    public void testCreateYouyouGoodslist_success() {
        // 准备参数
        YouyouGoodslistSaveReqVO createReqVO = randomPojo(YouyouGoodslistSaveReqVO.class).setId(null);

        // 调用
        Integer youyouGoodslistId = youyouGoodslistService.createYouyouGoodslist(createReqVO);
        // 断言
        assertNotNull(youyouGoodslistId);
        // 校验记录的属性是否正确
        YouyouGoodslistDO youyouGoodslist = youyouGoodslistMapper.selectById(youyouGoodslistId);
        assertPojoEquals(createReqVO, youyouGoodslist, "id");
    }

    @Test
    public void testUpdateYouyouGoodslist_success() {
        // mock 数据
        YouyouGoodslistDO dbYouyouGoodslist = randomPojo(YouyouGoodslistDO.class);
        youyouGoodslistMapper.insert(dbYouyouGoodslist);// @Sql: 先插入出一条存在的数据
        // 准备参数
        YouyouGoodslistSaveReqVO updateReqVO = randomPojo(YouyouGoodslistSaveReqVO.class, o -> {
            o.setId(dbYouyouGoodslist.getId()); // 设置更新的 ID
        });

        // 调用
        youyouGoodslistService.updateYouyouGoodslist(updateReqVO);
        // 校验是否更新正确
        YouyouGoodslistDO youyouGoodslist = youyouGoodslistMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, youyouGoodslist);
    }

    @Test
    public void testUpdateYouyouGoodslist_notExists() {
        // 准备参数
        YouyouGoodslistSaveReqVO updateReqVO = randomPojo(YouyouGoodslistSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> youyouGoodslistService.updateYouyouGoodslist(updateReqVO), YOUYOU_GOODSLIST_NOT_EXISTS);
    }

    @Test
    public void testDeleteYouyouGoodslist_success() {
        // mock 数据
        YouyouGoodslistDO dbYouyouGoodslist = randomPojo(YouyouGoodslistDO.class);
        youyouGoodslistMapper.insert(dbYouyouGoodslist);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbYouyouGoodslist.getId();

        // 调用
        youyouGoodslistService.deleteYouyouGoodslist(id);
       // 校验数据不存在了
       assertNull(youyouGoodslistMapper.selectById(id));
    }

    @Test
    public void testDeleteYouyouGoodslist_notExists() {
        // 准备参数
        Integer id = randomIntegerId();

        // 调用, 并断言异常
        assertServiceException(() -> youyouGoodslistService.deleteYouyouGoodslist(id), YOUYOU_GOODSLIST_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetYouyouGoodslistPage() {
       // mock 数据
       YouyouGoodslistDO dbYouyouGoodslist = randomPojo(YouyouGoodslistDO.class, o -> { // 等会查询到
           o.setTemplateId(null);
           o.setCommodityName(null);
           o.setCommodityPrice(null);
           o.setCommodityAbrade(null);
           o.setCommodityPaintSeed(null);
           o.setCommodityPaintIndex(null);
           o.setCommodityHaveNameTag(null);
           o.setCommodityHaveBuzhang(null);
           o.setCommodityHaveSticker(null);
           o.setShippingMode(null);
           o.setTemplateisFade(null);
           o.setTemplateisHardened(null);
           o.setTemplateisDoppler(null);
           o.setCommodityStickersStickerId(null);
           o.setCommodityStickersRawIndex(null);
           o.setCommodityStickersName(null);
           o.setCommodityStickersHashName(null);
           o.setCommodityStickersMaterial(null);
           o.setCommodityStickersImgUrl(null);
           o.setCommodityStickersPrice(null);
           o.setCommodityStickersAbrade(null);
           o.setCommodityDopplerTitle(null);
           o.setCommodityDopplerAbbrTitle(null);
           o.setCommodityDopplerColor(null);
           o.setCommodityFadeTitle(null);
           o.setCommodityFadeNumerialValue(null);
           o.setCommodityFadeColor(null);
           o.setCommodityHardenedTitle(null);
           o.setCommodityHardenedAbbrTitle(null);
           o.setCommodityHardenedColor(null);
           o.setCreateTime(null);
       });
       youyouGoodslistMapper.insert(dbYouyouGoodslist);
       // 测试 templateId 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setTemplateId(null)));
       // 测试 commodityName 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityName(null)));
       // 测试 commodityPrice 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityPrice(null)));
       // 测试 commodityAbrade 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityAbrade(null)));
       // 测试 commodityPaintSeed 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityPaintSeed(null)));
       // 测试 commodityPaintIndex 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityPaintIndex(null)));
       // 测试 commodityHaveNameTag 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityHaveNameTag(null)));
       // 测试 commodityHaveBuzhang 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityHaveBuzhang(null)));
       // 测试 commodityHaveSticker 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityHaveSticker(null)));
       // 测试 shippingMode 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setShippingMode(null)));
       // 测试 templateisFade 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setTemplateisFade(null)));
       // 测试 templateisHardened 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setTemplateisHardened(null)));
       // 测试 templateisDoppler 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setTemplateisDoppler(null)));
       // 测试 commodityStickersStickerId 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityStickersStickerId(null)));
       // 测试 commodityStickersRawIndex 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityStickersRawIndex(null)));
       // 测试 commodityStickersName 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityStickersName(null)));
       // 测试 commodityStickersHashName 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityStickersHashName(null)));
       // 测试 commodityStickersMaterial 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityStickersMaterial(null)));
       // 测试 commodityStickersImgUrl 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityStickersImgUrl(null)));
       // 测试 commodityStickersPrice 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityStickersPrice(null)));
       // 测试 commodityStickersAbrade 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityStickersAbrade(null)));
       // 测试 commodityDopplerTitle 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityDopplerTitle(null)));
       // 测试 commodityDopplerAbbrTitle 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityDopplerAbbrTitle(null)));
       // 测试 commodityDopplerColor 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityDopplerColor(null)));
       // 测试 commodityFadeTitle 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityFadeTitle(null)));
       // 测试 commodityFadeNumerialValue 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityFadeNumerialValue(null)));
       // 测试 commodityFadeColor 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityFadeColor(null)));
       // 测试 commodityHardenedTitle 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityHardenedTitle(null)));
       // 测试 commodityHardenedAbbrTitle 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityHardenedAbbrTitle(null)));
       // 测试 commodityHardenedColor 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCommodityHardenedColor(null)));
       // 测试 createTime 不匹配
       youyouGoodslistMapper.insert(cloneIgnoreId(dbYouyouGoodslist, o -> o.setCreateTime(null)));
       // 准备参数
       YouyouGoodslistPageReqVO reqVO = new YouyouGoodslistPageReqVO();
       reqVO.setTemplateId(null);
       reqVO.setCommodityName(null);
       reqVO.setCommodityPrice(null);
       reqVO.setCommodityAbrade(null);
       reqVO.setCommodityPaintSeed(null);
       reqVO.setCommodityPaintIndex(null);
       reqVO.setCommodityHaveNameTag(null);
       reqVO.setCommodityHaveBuzhang(null);
       reqVO.setCommodityHaveSticker(null);
       reqVO.setShippingMode(null);
       reqVO.setTemplateisFade(null);
       reqVO.setTemplateisHardened(null);
       reqVO.setTemplateisDoppler(null);
       reqVO.setCommodityStickersStickerId(null);
       reqVO.setCommodityStickersRawIndex(null);
       reqVO.setCommodityStickersName(null);
       reqVO.setCommodityStickersHashName(null);
       reqVO.setCommodityStickersMaterial(null);
       reqVO.setCommodityStickersImgUrl(null);
       reqVO.setCommodityStickersPrice(null);
       reqVO.setCommodityStickersAbrade(null);
       reqVO.setCommodityDopplerTitle(null);
       reqVO.setCommodityDopplerAbbrTitle(null);
       reqVO.setCommodityDopplerColor(null);
       reqVO.setCommodityFadeTitle(null);
       reqVO.setCommodityFadeNumerialValue(null);
       reqVO.setCommodityFadeColor(null);
       reqVO.setCommodityHardenedTitle(null);
       reqVO.setCommodityHardenedAbbrTitle(null);
       reqVO.setCommodityHardenedColor(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<YouyouGoodslistDO> pageResult = youyouGoodslistService.getYouyouGoodslistPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbYouyouGoodslist, pageResult.getList().get(0));
    }

}