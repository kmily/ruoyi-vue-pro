package cn.iocoder.yudao.module.haoka.service.superiorproductconfig;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.superiorproductconfig.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorproductconfig.SuperiorProductConfigDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.superiorproductconfig.SuperiorProductConfigMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link SuperiorProductConfigServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(SuperiorProductConfigServiceImpl.class)
public class SuperiorProductConfigServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SuperiorProductConfigServiceImpl superiorProductConfigService;

    @Resource
    private SuperiorProductConfigMapper superiorProductConfigMapper;

    @Test
    public void testCreateSuperiorProductConfig_success() {
        // 准备参数
        SuperiorProductConfigSaveReqVO createReqVO = randomPojo(SuperiorProductConfigSaveReqVO.class).setId(null);

        // 调用
        Long superiorProductConfigId = superiorProductConfigService.createSuperiorProductConfig(createReqVO);
        // 断言
        assertNotNull(superiorProductConfigId);
        // 校验记录的属性是否正确
        SuperiorProductConfigDO superiorProductConfig = superiorProductConfigMapper.selectById(superiorProductConfigId);
        assertPojoEquals(createReqVO, superiorProductConfig, "id");
    }

    @Test
    public void testUpdateSuperiorProductConfig_success() {
        // mock 数据
        SuperiorProductConfigDO dbSuperiorProductConfig = randomPojo(SuperiorProductConfigDO.class);
        superiorProductConfigMapper.insert(dbSuperiorProductConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SuperiorProductConfigSaveReqVO updateReqVO = randomPojo(SuperiorProductConfigSaveReqVO.class, o -> {
            o.setId(dbSuperiorProductConfig.getId()); // 设置更新的 ID
        });

        // 调用
        superiorProductConfigService.updateSuperiorProductConfig(updateReqVO);
        // 校验是否更新正确
        SuperiorProductConfigDO superiorProductConfig = superiorProductConfigMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, superiorProductConfig);
    }

    @Test
    public void testUpdateSuperiorProductConfig_notExists() {
        // 准备参数
        SuperiorProductConfigSaveReqVO updateReqVO = randomPojo(SuperiorProductConfigSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> superiorProductConfigService.updateSuperiorProductConfig(updateReqVO), SUPERIOR_PRODUCT_CONFIG_NOT_EXISTS);
    }

    @Test
    public void testDeleteSuperiorProductConfig_success() {
        // mock 数据
        SuperiorProductConfigDO dbSuperiorProductConfig = randomPojo(SuperiorProductConfigDO.class);
        superiorProductConfigMapper.insert(dbSuperiorProductConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSuperiorProductConfig.getId();

        // 调用
        superiorProductConfigService.deleteSuperiorProductConfig(id);
       // 校验数据不存在了
       assertNull(superiorProductConfigMapper.selectById(id));
    }

    @Test
    public void testDeleteSuperiorProductConfig_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> superiorProductConfigService.deleteSuperiorProductConfig(id), SUPERIOR_PRODUCT_CONFIG_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSuperiorProductConfigPage() {
       // mock 数据
       SuperiorProductConfigDO dbSuperiorProductConfig = randomPojo(SuperiorProductConfigDO.class, o -> { // 等会查询到
           o.setHaokaSuperiorApiId(null);
           o.setHaokaProductId(null);
           o.setIsConfined(null);
           o.setConfig(null);
           o.setRemarks(null);
           o.setCreateTime(null);
       });
       superiorProductConfigMapper.insert(dbSuperiorProductConfig);
       // 测试 haokaSuperiorApiId 不匹配
       superiorProductConfigMapper.insert(cloneIgnoreId(dbSuperiorProductConfig, o -> o.setHaokaSuperiorApiId(null)));
       // 测试 haokaProductId 不匹配
       superiorProductConfigMapper.insert(cloneIgnoreId(dbSuperiorProductConfig, o -> o.setHaokaProductId(null)));
       // 测试 isConfined 不匹配
       superiorProductConfigMapper.insert(cloneIgnoreId(dbSuperiorProductConfig, o -> o.setIsConfined(null)));
       // 测试 config 不匹配
       superiorProductConfigMapper.insert(cloneIgnoreId(dbSuperiorProductConfig, o -> o.setConfig(null)));
       // 测试 remarks 不匹配
       superiorProductConfigMapper.insert(cloneIgnoreId(dbSuperiorProductConfig, o -> o.setRemarks(null)));
       // 测试 createTime 不匹配
       superiorProductConfigMapper.insert(cloneIgnoreId(dbSuperiorProductConfig, o -> o.setCreateTime(null)));
       // 准备参数
       SuperiorProductConfigPageReqVO reqVO = new SuperiorProductConfigPageReqVO();
       reqVO.setHaokaSuperiorApiId(null);
       reqVO.setHaokaProductId(null);
       reqVO.setIsConfined(null);
       reqVO.setConfig(null);
       reqVO.setRemarks(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<SuperiorProductConfigDO> pageResult = superiorProductConfigService.getSuperiorProductConfigPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSuperiorProductConfig, pageResult.getList().get(0));
    }

}