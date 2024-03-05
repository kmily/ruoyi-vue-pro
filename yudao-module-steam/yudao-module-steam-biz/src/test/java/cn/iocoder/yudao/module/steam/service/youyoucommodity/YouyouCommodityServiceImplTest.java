package cn.iocoder.yudao.module.steam.service.youyoucommodity;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.YouyouCommodityMapper;
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
 * {@link YouyouCommodityServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(YouyouCommodityServiceImpl.class)
public class YouyouCommodityServiceImplTest extends BaseDbUnitTest {

    @Resource
    private YouyouCommodityServiceImpl youyouCommodityService;

    @Resource
    private YouyouCommodityMapper youyouCommodityMapper;

    @Test
    public void testCreateYouyouCommodity_success() {
        // 准备参数
        YouyouCommoditySaveReqVO createReqVO = randomPojo(YouyouCommoditySaveReqVO.class).setId(null);

        // 调用
        Integer youyouCommodityId = youyouCommodityService.createYouyouCommodity(createReqVO);
        // 断言
        assertNotNull(youyouCommodityId);
        // 校验记录的属性是否正确
        YouyouCommodityDO youyouCommodity = youyouCommodityMapper.selectById(youyouCommodityId);
        assertPojoEquals(createReqVO, youyouCommodity, "id");
    }

    @Test
    public void testUpdateYouyouCommodity_success() {
        // mock 数据
        YouyouCommodityDO dbYouyouCommodity = randomPojo(YouyouCommodityDO.class);
        youyouCommodityMapper.insert(dbYouyouCommodity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        YouyouCommoditySaveReqVO updateReqVO = randomPojo(YouyouCommoditySaveReqVO.class, o -> {
            o.setId(dbYouyouCommodity.getId()); // 设置更新的 ID
        });

        // 调用
        youyouCommodityService.updateYouyouCommodity(updateReqVO);
        // 校验是否更新正确
        YouyouCommodityDO youyouCommodity = youyouCommodityMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, youyouCommodity);
    }

    @Test
    public void testUpdateYouyouCommodity_notExists() {
        // 准备参数
        YouyouCommoditySaveReqVO updateReqVO = randomPojo(YouyouCommoditySaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> youyouCommodityService.updateYouyouCommodity(updateReqVO), YOUYOU_COMMODITY_NOT_EXISTS);
    }

    @Test
    public void testDeleteYouyouCommodity_success() {
        // mock 数据
        YouyouCommodityDO dbYouyouCommodity = randomPojo(YouyouCommodityDO.class);
        youyouCommodityMapper.insert(dbYouyouCommodity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbYouyouCommodity.getId();

        // 调用
        youyouCommodityService.deleteYouyouCommodity(id);
       // 校验数据不存在了
       assertNull(youyouCommodityMapper.selectById(id));
    }

    @Test
    public void testDeleteYouyouCommodity_notExists() {
        // 准备参数
        Integer id = 1;

        // 调用, 并断言异常
        assertServiceException(() -> youyouCommodityService.deleteYouyouCommodity(id), YOUYOU_COMMODITY_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetYouyouCommodityPage() {
       // mock 数据
       YouyouCommodityDO dbYouyouCommodity = randomPojo(YouyouCommodityDO.class, o -> { // 等会查询到
           o.setId(null);
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
           o.setTransferStatus(null);
           o.setCreateTime(null);
       });
       youyouCommodityMapper.insert(dbYouyouCommodity);
       // 测试 id 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setId(null)));
       // 测试 templateId 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setTemplateId(null)));
       // 测试 commodityName 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityName(null)));
       // 测试 commodityPrice 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityPrice(null)));
       // 测试 commodityAbrade 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityAbrade(null)));
       // 测试 commodityPaintSeed 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityPaintSeed(null)));
       // 测试 commodityPaintIndex 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityPaintIndex(null)));
       // 测试 commodityHaveNameTag 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityHaveNameTag(null)));
       // 测试 commodityHaveBuzhang 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityHaveBuzhang(null)));
       // 测试 commodityHaveSticker 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityHaveSticker(null)));
       // 测试 shippingMode 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setShippingMode(null)));
       // 测试 templateisFade 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setTemplateisFade(null)));
       // 测试 templateisHardened 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setTemplateisHardened(null)));
       // 测试 templateisDoppler 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setTemplateisDoppler(null)));
       // 测试 commodityStickersStickerId 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickersStickerId(null)));
       // 测试 commodityStickersRawIndex 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickersRawIndex(null)));
       // 测试 commodityStickersName 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickersName(null)));
       // 测试 commodityStickersHashName 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickersHashName(null)));
       // 测试 commodityStickersMaterial 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickersMaterial(null)));
       // 测试 commodityStickersImgUrl 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickersImgUrl(null)));
       // 测试 commodityStickersPrice 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickersPrice(null)));
       // 测试 commodityStickersAbrade 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickersAbrade(null)));
       // 测试 commodityDopplerTitle 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityDopplerTitle(null)));
       // 测试 commodityDopplerAbbrTitle 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityDopplerAbbrTitle(null)));
       // 测试 commodityDopplerColor 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityDopplerColor(null)));
       // 测试 commodityFadeTitle 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityFadeTitle(null)));
       // 测试 commodityFadeNumerialValue 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityFadeNumerialValue(null)));
       // 测试 commodityFadeColor 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityFadeColor(null)));
       // 测试 commodityHardenedTitle 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityHardenedTitle(null)));
       // 测试 commodityHardenedAbbrTitle 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityHardenedAbbrTitle(null)));
       // 测试 commodityHardenedColor 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityHardenedColor(null)));
       // 测试 transferStatus 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setTransferStatus(null)));
       // 测试 createTime 不匹配
       youyouCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCreateTime(null)));
       // 准备参数
       YouyouCommodityPageReqVO reqVO = new YouyouCommodityPageReqVO();
       reqVO.setId(null);
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
       reqVO.setTransferStatus(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<YouyouCommodityDO> pageResult = youyouCommodityService.getYouyouCommodityPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbYouyouCommodity, pageResult.getList().get(0));
    }

}