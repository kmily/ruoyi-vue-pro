package cn.iocoder.yudao.module.steam.service.othertemplate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.othertemplate.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.othertemplate.OtherTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.othertemplate.OtherTemplateMapper;
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
 * {@link OtherTemplateServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(OtherTemplateServiceImpl.class)
public class OtherTemplateServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OtherTemplateServiceImpl otherTemplateService;

    @Resource
    private OtherTemplateMapper otherTemplateMapper;

    @Test
    public void testCreateOtherTemplate_success() {
        // 准备参数
        OtherTemplateSaveReqVO createReqVO = randomPojo(OtherTemplateSaveReqVO.class).setId(null);

        // 调用
        Integer otherTemplateId = otherTemplateService.createOtherTemplate(createReqVO);
        // 断言
        assertNotNull(otherTemplateId);
        // 校验记录的属性是否正确
        OtherTemplateDO otherTemplate = otherTemplateMapper.selectById(otherTemplateId);
        assertPojoEquals(createReqVO, otherTemplate, "id");
    }

    @Test
    public void testUpdateOtherTemplate_success() {
        // mock 数据
        OtherTemplateDO dbOtherTemplate = randomPojo(OtherTemplateDO.class);
        otherTemplateMapper.insert(dbOtherTemplate);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OtherTemplateSaveReqVO updateReqVO = randomPojo(OtherTemplateSaveReqVO.class, o -> {
            o.setId(dbOtherTemplate.getId()); // 设置更新的 ID
        });

        // 调用
        otherTemplateService.updateOtherTemplate(updateReqVO);
        // 校验是否更新正确
        OtherTemplateDO otherTemplate = otherTemplateMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, otherTemplate);
    }

    @Test
    public void testUpdateOtherTemplate_notExists() {
        // 准备参数
        OtherTemplateSaveReqVO updateReqVO = randomPojo(OtherTemplateSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> otherTemplateService.updateOtherTemplate(updateReqVO), OTHER_TEMPLATE_NOT_EXISTS);
    }

    @Test
    public void testDeleteOtherTemplate_success() {
        // mock 数据
        OtherTemplateDO dbOtherTemplate = randomPojo(OtherTemplateDO.class);
        otherTemplateMapper.insert(dbOtherTemplate);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbOtherTemplate.getId();

        // 调用
        otherTemplateService.deleteOtherTemplate(id);
       // 校验数据不存在了
       assertNull(otherTemplateMapper.selectById(id));
    }

    @Test
    public void testDeleteOtherTemplate_notExists() {
        // 准备参数
/*        Integer id = randomIntegerId();*/

        // 调用, 并断言异常
/*
        assertServiceException(() -> otherTemplateService.deleteOtherTemplate(id), OTHER_TEMPLATE_NOT_EXISTS);
*/
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOtherTemplatePage() {
       // mock 数据
       OtherTemplateDO dbOtherTemplate = randomPojo(OtherTemplateDO.class, o -> { // 等会查询到
           o.setPlatformIdentity(null);
           o.setExterior(null);
           o.setExteriorName(null);
           o.setItemId(null);
           o.setItemName(null);
           o.setMarketHashName(null);
           o.setAutoDeliverPrice(null);
           o.setQuality(null);
           o.setRarity(null);
           o.setType(null);
           o.setImageUrl(null);
           o.setAutoDeliverQuantity(null);
           o.setQualityColor(null);
           o.setQualityName(null);
           o.setRarityColor(null);
           o.setRarityName(null);
           o.setShortName(null);
           o.setTypeName(null);
       });
       otherTemplateMapper.insert(dbOtherTemplate);
       // 测试 platformIdentity 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setPlatformIdentity(null)));
       // 测试 exterior 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setExterior(null)));
       // 测试 exteriorName 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setExteriorName(null)));
       // 测试 itemId 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setItemId(null)));
       // 测试 itemName 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setItemName(null)));
       // 测试 marketHashName 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setMarketHashName(null)));
       // 测试 autoDeliverPrice 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setAutoDeliverPrice(null)));
       // 测试 quality 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setQuality(null)));
       // 测试 rarity 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setRarity(null)));
       // 测试 type 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setType(null)));
       // 测试 imageUrl 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setImageUrl(null)));
       // 测试 autoDeliverQuantity 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setAutoDeliverQuantity(null)));
       // 测试 qualityColor 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setQualityColor(null)));
       // 测试 qualityName 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setQualityName(null)));
       // 测试 rarityColor 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setRarityColor(null)));
       // 测试 rarityName 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setRarityName(null)));
       // 测试 shortName 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setShortName(null)));
       // 测试 typeName 不匹配
       otherTemplateMapper.insert(cloneIgnoreId(dbOtherTemplate, o -> o.setTypeName(null)));
       // 准备参数
       OtherTemplatePageReqVO reqVO = new OtherTemplatePageReqVO();
       reqVO.setPlatformIdentity(null);
       reqVO.setExterior(null);
       reqVO.setExteriorName(null);
       reqVO.setItemId(null);
       reqVO.setItemName(null);
       reqVO.setMarketHashName(null);
       reqVO.setAutoDeliverPrice(null);
       reqVO.setQuality(null);
       reqVO.setRarity(null);
       reqVO.setType(null);
       reqVO.setImageUrl(null);
       reqVO.setAutoDeliverQuantity(null);
       reqVO.setQualityColor(null);
       reqVO.setQualityName(null);
       reqVO.setRarityColor(null);
       reqVO.setRarityName(null);
       reqVO.setShortName(null);
       reqVO.setTypeName(null);

       // 调用
       PageResult<OtherTemplateDO> pageResult = otherTemplateService.getOtherTemplatePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOtherTemplate, pageResult.getList().get(0));
    }

}