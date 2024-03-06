package cn.iocoder.yudao.module.steam.service.youyoucommodity;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.UUCommodityMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

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
    private UUCommodityMapper UUCommodityMapper;

    @Test
    public void testCreateYouyouCommodity_success() {
        // 准备参数
        YouyouCommoditySaveReqVO createReqVO = randomPojo(YouyouCommoditySaveReqVO.class).setId(null);

        // 调用
        Integer youyouCommodityId = youyouCommodityService.createYouyouCommodity(createReqVO);
        // 断言
        assertNotNull(youyouCommodityId);
        // 校验记录的属性是否正确
        YouyouCommodityDO youyouCommodity = UUCommodityMapper.selectById(youyouCommodityId);
        assertPojoEquals(createReqVO, youyouCommodity, "id");
    }

    @Test
    public void testUpdateYouyouCommodity_success() {
        // mock 数据
        YouyouCommodityDO dbYouyouCommodity = randomPojo(YouyouCommodityDO.class);
        UUCommodityMapper.insert(dbYouyouCommodity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        YouyouCommoditySaveReqVO updateReqVO = randomPojo(YouyouCommoditySaveReqVO.class, o -> {
            o.setId(dbYouyouCommodity.getId()); // 设置更新的 ID
        });

        // 调用
        youyouCommodityService.updateYouyouCommodity(updateReqVO);
        // 校验是否更新正确
        YouyouCommodityDO youyouCommodity = UUCommodityMapper.selectById(updateReqVO.getId()); // 获取最新的
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
        UUCommodityMapper.insert(dbYouyouCommodity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Integer id = dbYouyouCommodity.getId();

        // 调用
        youyouCommodityService.deleteYouyouCommodity(id);
       // 校验数据不存在了
       assertNull(UUCommodityMapper.selectById(id));
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
           o.setCreateTime(null);
           o.setCommodityStickers(null);
           o.setCommodityDoppler(null);
           o.setCommodityFade(null);
           o.setCommodityHardened(null);
           o.setTransferStatus(null);
           o.setStatus(null);
       });
       UUCommodityMapper.insert(dbYouyouCommodity);
       // 测试 id 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setId(null)));
       // 测试 templateId 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setTemplateId(null)));
       // 测试 commodityName 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityName(null)));
       // 测试 commodityPrice 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityPrice(null)));
       // 测试 createTime 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCreateTime(null)));
       // 测试 commodityStickers 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityStickers(null)));
       // 测试 commodityDoppler 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityDoppler(null)));
       // 测试 commodityFade 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityFade(null)));
       // 测试 commodityHardened 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setCommodityHardened(null)));
       // 测试 transferStatus 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setTransferStatus(null)));
       // 测试 status 不匹配
       UUCommodityMapper.insert(cloneIgnoreId(dbYouyouCommodity, o -> o.setStatus(null)));
       // 准备参数
       YouyouCommodityPageReqVO reqVO = new YouyouCommodityPageReqVO();
       reqVO.setId(null);
       reqVO.setTemplateId(null);
       reqVO.setCommodityName(null);
       reqVO.setCommodityPrice(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setCommodityStickers(null);
       reqVO.setCommodityDoppler(null);
       reqVO.setCommodityFade(null);
       reqVO.setCommodityHardened(null);
       reqVO.setTransferStatus(null);
       reqVO.setStatus(null);

       // 调用
       PageResult<YouyouCommodityDO> pageResult = youyouCommodityService.getYouyouCommodityPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbYouyouCommodity, pageResult.getList().get(0));
    }

}