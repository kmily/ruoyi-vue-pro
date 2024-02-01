package cn.iocoder.yudao.module.steam.service.selquality;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.selquality.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selquality.SelQualityDO;
import cn.iocoder.yudao.module.steam.dal.mysql.selquality.SelQualityMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import jakarta.annotation.Resource;
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
 * {@link SelQualityServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(SelQualityServiceImpl.class)
public class SelQualityServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SelQualityServiceImpl selQualityService;

    @Resource
    private SelQualityMapper selQualityMapper;

    @Test
    public void testCreateSelQuality_success() {
        // 准备参数
        SelQualitySaveReqVO createReqVO = randomPojo(SelQualitySaveReqVO.class).setId(null);

        // 调用
        Long selQualityId = selQualityService.createSelQuality(createReqVO);
        // 断言
        assertNotNull(selQualityId);
        // 校验记录的属性是否正确
        SelQualityDO selQuality = selQualityMapper.selectById(selQualityId);
        assertPojoEquals(createReqVO, selQuality, "id");
    }

    @Test
    public void testUpdateSelQuality_success() {
        // mock 数据
        SelQualityDO dbSelQuality = randomPojo(SelQualityDO.class);
        selQualityMapper.insert(dbSelQuality);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SelQualitySaveReqVO updateReqVO = randomPojo(SelQualitySaveReqVO.class, o -> {
            o.setId(dbSelQuality.getId()); // 设置更新的 ID
        });

        // 调用
        selQualityService.updateSelQuality(updateReqVO);
        // 校验是否更新正确
        SelQualityDO selQuality = selQualityMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, selQuality);
    }

    @Test
    public void testUpdateSelQuality_notExists() {
        // 准备参数
        SelQualitySaveReqVO updateReqVO = randomPojo(SelQualitySaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> selQualityService.updateSelQuality(updateReqVO), SEL_QUALITY_NOT_EXISTS);
    }

    @Test
    public void testDeleteSelQuality_success() {
        // mock 数据
        SelQualityDO dbSelQuality = randomPojo(SelQualityDO.class);
        selQualityMapper.insert(dbSelQuality);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSelQuality.getId();

        // 调用
        selQualityService.deleteSelQuality(id);
       // 校验数据不存在了
       assertNull(selQualityMapper.selectById(id));
    }

    @Test
    public void testDeleteSelQuality_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> selQualityService.deleteSelQuality(id), SEL_QUALITY_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSelQualityPage() {
       // mock 数据
       SelQualityDO dbSelQuality = randomPojo(SelQualityDO.class, o -> { // 等会查询到
           o.setInternalName(null);
           o.setLocalizedTagName(null);
           o.setColor(null);
           o.setCreateTime(null);
       });
       selQualityMapper.insert(dbSelQuality);
       // 测试 internalName 不匹配
       selQualityMapper.insert(cloneIgnoreId(dbSelQuality, o -> o.setInternalName(null)));
       // 测试 localizedTagName 不匹配
       selQualityMapper.insert(cloneIgnoreId(dbSelQuality, o -> o.setLocalizedTagName(null)));
       // 测试 color 不匹配
       selQualityMapper.insert(cloneIgnoreId(dbSelQuality, o -> o.setColor(null)));
       // 测试 createTime 不匹配
       selQualityMapper.insert(cloneIgnoreId(dbSelQuality, o -> o.setCreateTime(null)));
       // 准备参数
       SelQualityPageReqVO reqVO = new SelQualityPageReqVO();
       reqVO.setInternalName(null);
       reqVO.setLocalizedTagName(null);
       reqVO.setColor(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<SelQualityDO> pageResult = selQualityService.getSelQualityPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSelQuality, pageResult.getList().get(0));
    }

}