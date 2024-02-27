package cn.iocoder.yudao.module.steam.service.invpreview;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
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
 * {@link InvPreviewServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(InvPreviewServiceImpl.class)
public class InvPreviewServiceImplTest extends BaseDbUnitTest {

    @Resource
    private InvPreviewServiceImpl invPreviewService;

    @Resource
    private InvPreviewMapper invPreviewMapper;

    @Test
    public void testCreateInvPreview_success() {
        // 准备参数
        InvPreviewSaveReqVO createReqVO = randomPojo(InvPreviewSaveReqVO.class).setId(null);

        // 调用
        Long invPreviewId = invPreviewService.createInvPreview(createReqVO);
        // 断言
        assertNotNull(invPreviewId);
        // 校验记录的属性是否正确
        InvPreviewDO invPreview = invPreviewMapper.selectById(invPreviewId);
        assertPojoEquals(createReqVO, invPreview, "id");
    }

    @Test
    public void testUpdateInvPreview_success() {
        // mock 数据
        InvPreviewDO dbInvPreview = randomPojo(InvPreviewDO.class);
        invPreviewMapper.insert(dbInvPreview);// @Sql: 先插入出一条存在的数据
        // 准备参数
        InvPreviewSaveReqVO updateReqVO = randomPojo(InvPreviewSaveReqVO.class, o -> {
            o.setId(dbInvPreview.getId()); // 设置更新的 ID
        });

        // 调用
        invPreviewService.updateInvPreview(updateReqVO);
        // 校验是否更新正确
        InvPreviewDO invPreview = invPreviewMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, invPreview);
    }

    @Test
    public void testUpdateInvPreview_notExists() {
        // 准备参数
        InvPreviewSaveReqVO updateReqVO = randomPojo(InvPreviewSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> invPreviewService.updateInvPreview(updateReqVO), INV_PREVIEW_NOT_EXISTS);
    }

    @Test
    public void testDeleteInvPreview_success() {
        // mock 数据
        InvPreviewDO dbInvPreview = randomPojo(InvPreviewDO.class);
        invPreviewMapper.insert(dbInvPreview);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbInvPreview.getId();

        // 调用
        invPreviewService.deleteInvPreview(id);
       // 校验数据不存在了
       assertNull(invPreviewMapper.selectById(id));
    }

    @Test
    public void testDeleteInvPreview_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> invPreviewService.deleteInvPreview(id), INV_PREVIEW_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetInvPreviewPage() {
       // mock 数据
       InvPreviewDO dbInvPreview = randomPojo(InvPreviewDO.class, o -> { // 等会查询到
           o.setPrice(null);
           o.setQuantity(null);
           o.setDeals(null);
           o.setItemId(null);
           o.setAppId(null);
           o.setItemName(null);
           o.setShortName(null);
           o.setMarketHashName(null);
           o.setImageUrl(null);
           o.setItemInfo(null);
           o.setSellType(null);
           o.setCurrencyId(null);
           o.setCnyPrice(null);
           o.setSalePrice(null);
           o.setSubsidyPrice(null);
           o.setActivityTag(null);
           o.setTagList(null);
           o.setSubsidyTag(null);
           o.setAutoPrice(null);
           o.setAutoQuantity(null);
           o.setReferencePrice(null);
       });
       invPreviewMapper.insert(dbInvPreview);
       // 测试 price 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setPrice(null)));
       // 测试 quantity 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setQuantity(null)));
       // 测试 deals 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setDeals(null)));
       // 测试 itemId 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setItemId(null)));
       // 测试 appId 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setAppId(null)));
       // 测试 itemName 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setItemName(null)));
       // 测试 shortName 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setShortName(null)));
       // 测试 marketHashName 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setMarketHashName(null)));
       // 测试 imageUrl 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setImageUrl(null)));
       // 测试 itemInfo 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setItemInfo(null)));
       // 测试 sellType 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setSellType(null)));
       // 测试 currencyId 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setCurrencyId(null)));
       // 测试 cnyPrice 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setCnyPrice(null)));
       // 测试 salePrice 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setSalePrice(null)));
       // 测试 subsidyPrice 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setSubsidyPrice(null)));
       // 测试 activityTag 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setActivityTag(null)));
       // 测试 tagList 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setTagList(null)));
       // 测试 subsidyTag 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setSubsidyTag(null)));
       // 测试 autoPrice 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setAutoPrice(null)));
       // 测试 autoQuantity 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setAutoQuantity(null)));
       // 测试 referencePrice 不匹配
       invPreviewMapper.insert(cloneIgnoreId(dbInvPreview, o -> o.setReferencePrice(null)));
       // 准备参数
       InvPreviewPageReqVO reqVO = new InvPreviewPageReqVO();
       reqVO.setPrice(null);
       reqVO.setQuantity(null);
       reqVO.setDeals(null);
       reqVO.setItemId(null);
       reqVO.setAppId(null);
       reqVO.setItemName(null);
       reqVO.setShortName(null);
       reqVO.setMarketHashName(null);
       reqVO.setImageUrl(null);
       reqVO.setItemInfo(null);
       reqVO.setSellType(null);
       reqVO.setCurrencyId(null);
       reqVO.setCnyPrice(null);
       reqVO.setSalePrice(null);
       reqVO.setSubsidyPrice(null);
       reqVO.setActivityTag(null);
       reqVO.setTagList(null);
       reqVO.setSubsidyTag(null);
       reqVO.setAutoPrice(null);
       reqVO.setAutoQuantity(null);
       reqVO.setReferencePrice(null);

       // 调用
       PageResult<InvPreviewDO> pageResult = invPreviewService.getInvPreviewPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbInvPreview, pageResult.getList().get(0));
    }

}