package cn.iocoder.yudao.module.steam.service.otherselling;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.otherselling.OtherSellingMapper;
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
 * {@link OtherSellingServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(OtherSellingServiceImpl.class)
public class OtherSellingServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OtherSellingServiceImpl otherSellingService;

    @Resource
    private OtherSellingMapper otherSellingMapper;

    @Test
    public void testCreateOtherSelling_success() {
        // 准备参数
        OtherSellingSaveReqVO createReqVO = randomPojo(OtherSellingSaveReqVO.class).setId(null);

        // 调用
        Integer otherSellingId = otherSellingService.createOtherSelling(createReqVO);
        // 断言
        assertNotNull(otherSellingId);
        // 校验记录的属性是否正确
        OtherSellingDO otherSelling = otherSellingMapper.selectById(otherSellingId);
        assertPojoEquals(createReqVO, otherSelling, "id");
    }

    @Test
    public void testUpdateOtherSelling_success() {
        // mock 数据
        OtherSellingDO dbOtherSelling = randomPojo(OtherSellingDO.class);
        otherSellingMapper.insert(dbOtherSelling);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OtherSellingSaveReqVO updateReqVO = randomPojo(OtherSellingSaveReqVO.class, o -> {
            o.setId(dbOtherSelling.getId()); // 设置更新的 ID
        });

        // 调用
        otherSellingService.updateOtherSelling(updateReqVO);
        // 校验是否更新正确
        OtherSellingDO otherSelling = otherSellingMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, otherSelling);
    }

    @Test
    public void testUpdateOtherSelling_notExists() {
        // 准备参数
        OtherSellingSaveReqVO updateReqVO = randomPojo(OtherSellingSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> otherSellingService.updateOtherSelling(updateReqVO), OTHER_SELLING_NOT_EXISTS);
    }

    @Test
    public void testDeleteOtherSelling_success() {
        // mock 数据
        OtherSellingDO dbOtherSelling = randomPojo(OtherSellingDO.class);
        otherSellingMapper.insert(dbOtherSelling);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbOtherSelling.getId();

        // 调用
        otherSellingService.deleteOtherSelling(id);
       // 校验数据不存在了
       assertNull(otherSellingMapper.selectById(id));
    }

    @Test
    public void testDeleteOtherSelling_notExists() {
        // 准备参数
        /*Integer id = randomIntegerId();*/

        // 调用, 并断言异常
        /*assertServiceException(() -> otherSellingService.deleteOtherSelling(id), OTHER_SELLING_NOT_EXISTS);*/
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOtherSellingPage() {
       // mock 数据
       OtherSellingDO dbOtherSelling = randomPojo(OtherSellingDO.class, o -> { // 等会查询到
           o.setAppid(null);
           o.setAssetid(null);
           o.setClassid(null);
           o.setInstanceid(null);
           o.setAmount(null);
           o.setCreateTime(null);
           o.setStatus(null);
           o.setPrice(null);
           o.setTransferStatus(null);
           o.setContextid(null);
           o.setIconUrl(null);
           o.setMarketName(null);
           o.setSelQuality(null);
           o.setSelItemset(null);
           o.setSelWeapon(null);
           o.setSelExterior(null);
           o.setSelRarity(null);
           o.setSelType(null);
           o.setMarketHashName(null);
           o.setDisplayWeight(null);
           o.setItemInfo(null);
           o.setShortName(null);
           o.setSellingUserName(null);
           o.setSellingAvator(null);
           o.setSellingUserId(null);
           o.setPlatformIdentity(null);
           o.setSteamId(null);
       });
       otherSellingMapper.insert(dbOtherSelling);
       // 测试 appid 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setAppid(null)));
       // 测试 assetid 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setAssetid(null)));
       // 测试 classid 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setClassid(null)));
       // 测试 instanceid 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setInstanceid(null)));
       // 测试 amount 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setAmount(null)));
       // 测试 createTime 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setStatus(null)));
       // 测试 price 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setPrice(null)));
       // 测试 transferStatus 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setTransferStatus(null)));
       // 测试 contextid 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setContextid(null)));
       // 测试 iconUrl 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setIconUrl(null)));
       // 测试 marketName 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setMarketName(null)));
       // 测试 selQuality 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSelQuality(null)));
       // 测试 selItemset 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSelItemset(null)));
       // 测试 selWeapon 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSelWeapon(null)));
       // 测试 selExterior 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSelExterior(null)));
       // 测试 selRarity 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSelRarity(null)));
       // 测试 selType 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSelType(null)));
       // 测试 marketHashName 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setMarketHashName(null)));
       // 测试 displayWeight 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setDisplayWeight(null)));
       // 测试 itemInfo 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setItemInfo(null)));
       // 测试 shortName 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setShortName(null)));
       // 测试 sellingUserName 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSellingUserName(null)));
       // 测试 sellingAvator 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSellingAvator(null)));
       // 测试 sellingUserId 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSellingUserId(null)));
       // 测试 platformIdentity 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setPlatformIdentity(null)));
       // 测试 steamId 不匹配
       otherSellingMapper.insert(cloneIgnoreId(dbOtherSelling, o -> o.setSteamId(null)));
       // 准备参数
       OtherSellingPageReqVO reqVO = new OtherSellingPageReqVO();
       reqVO.setAppid(null);
       reqVO.setAssetid(null);
       reqVO.setClassid(null);
       reqVO.setInstanceid(null);
       reqVO.setAmount(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);
       reqVO.setPrice(null);
       reqVO.setTransferStatus(null);
       reqVO.setContextid(null);
       reqVO.setIconUrl(null);
       reqVO.setMarketName(null);
       reqVO.setSelQuality(null);
       reqVO.setSelItemset(null);
       reqVO.setSelWeapon(null);
       reqVO.setSelExterior(null);
       reqVO.setSelRarity(null);
       reqVO.setSelType(null);
       reqVO.setMarketHashName(null);
       reqVO.setDisplayWeight(null);
       reqVO.setItemInfo(null);
       reqVO.setShortName(null);
       reqVO.setSellingUserName(null);
       reqVO.setSellingAvator(null);
       reqVO.setSellingUserId(null);
       reqVO.setPlatformIdentity(null);
       reqVO.setSteamId(null);

       // 调用
       PageResult<OtherSellingDO> pageResult = otherSellingService.getOtherSellingPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOtherSelling, pageResult.getList().get(0));
    }

}