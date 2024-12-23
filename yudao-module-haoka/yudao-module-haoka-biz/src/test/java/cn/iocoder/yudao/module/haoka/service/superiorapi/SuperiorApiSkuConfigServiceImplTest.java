package cn.iocoder.yudao.module.haoka.service.superiorapi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiSkuConfigDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi.SuperiorApiSkuConfigMapper;
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
 * {@link SuperiorApiSkuConfigServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(SuperiorApiSkuConfigServiceImpl.class)
public class SuperiorApiSkuConfigServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SuperiorApiSkuConfigServiceImpl superiorApiSkuConfigService;

    @Resource
    private SuperiorApiSkuConfigMapper superiorApiSkuConfigMapper;

    @Test
    public void testCreateSuperiorApiSkuConfig_success() {
        // 准备参数
        SuperiorApiSkuConfigSaveReqVO createReqVO = randomPojo(SuperiorApiSkuConfigSaveReqVO.class).setId(null);

        // 调用
        Long superiorApiSkuConfigId = superiorApiSkuConfigService.createSuperiorApiSkuConfig(createReqVO);
        // 断言
        assertNotNull(superiorApiSkuConfigId);
        // 校验记录的属性是否正确
        SuperiorApiSkuConfigDO superiorApiSkuConfig = superiorApiSkuConfigMapper.selectById(superiorApiSkuConfigId);
        assertPojoEquals(createReqVO, superiorApiSkuConfig, "id");
    }

    @Test
    public void testUpdateSuperiorApiSkuConfig_success() {
        // mock 数据
        SuperiorApiSkuConfigDO dbSuperiorApiSkuConfig = randomPojo(SuperiorApiSkuConfigDO.class);
        superiorApiSkuConfigMapper.insert(dbSuperiorApiSkuConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SuperiorApiSkuConfigSaveReqVO updateReqVO = randomPojo(SuperiorApiSkuConfigSaveReqVO.class, o -> {
            o.setId(dbSuperiorApiSkuConfig.getId()); // 设置更新的 ID
        });

        // 调用
        superiorApiSkuConfigService.updateSuperiorApiSkuConfig(updateReqVO);
        // 校验是否更新正确
        SuperiorApiSkuConfigDO superiorApiSkuConfig = superiorApiSkuConfigMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, superiorApiSkuConfig);
    }

    @Test
    public void testUpdateSuperiorApiSkuConfig_notExists() {
        // 准备参数
        SuperiorApiSkuConfigSaveReqVO updateReqVO = randomPojo(SuperiorApiSkuConfigSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> superiorApiSkuConfigService.updateSuperiorApiSkuConfig(updateReqVO), SUPERIOR_API_SKU_CONFIG_NOT_EXISTS);
    }

    @Test
    public void testDeleteSuperiorApiSkuConfig_success() {
        // mock 数据
        SuperiorApiSkuConfigDO dbSuperiorApiSkuConfig = randomPojo(SuperiorApiSkuConfigDO.class);
        superiorApiSkuConfigMapper.insert(dbSuperiorApiSkuConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSuperiorApiSkuConfig.getId();

        // 调用
        superiorApiSkuConfigService.deleteSuperiorApiSkuConfig(id);
       // 校验数据不存在了
       assertNull(superiorApiSkuConfigMapper.selectById(id));
    }

    @Test
    public void testDeleteSuperiorApiSkuConfig_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> superiorApiSkuConfigService.deleteSuperiorApiSkuConfig(id), SUPERIOR_API_SKU_CONFIG_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSuperiorApiSkuConfigPage() {
       // mock 数据
       SuperiorApiSkuConfigDO dbSuperiorApiSkuConfig = randomPojo(SuperiorApiSkuConfigDO.class, o -> { // 等会查询到
           o.setHaokaSuperiorApiId(null);
           o.setCode(null);
           o.setName(null);
           o.setRequired(null);
           o.setCreateTime(null);
       });
       superiorApiSkuConfigMapper.insert(dbSuperiorApiSkuConfig);
       // 测试 haokaSuperiorApiId 不匹配
       superiorApiSkuConfigMapper.insert(cloneIgnoreId(dbSuperiorApiSkuConfig, o -> o.setHaokaSuperiorApiId(null)));
       // 测试 code 不匹配
       superiorApiSkuConfigMapper.insert(cloneIgnoreId(dbSuperiorApiSkuConfig, o -> o.setCode(null)));
       // 测试 name 不匹配
       superiorApiSkuConfigMapper.insert(cloneIgnoreId(dbSuperiorApiSkuConfig, o -> o.setName(null)));
       // 测试 required 不匹配
       superiorApiSkuConfigMapper.insert(cloneIgnoreId(dbSuperiorApiSkuConfig, o -> o.setRequired(null)));
       // 测试 createTime 不匹配
       superiorApiSkuConfigMapper.insert(cloneIgnoreId(dbSuperiorApiSkuConfig, o -> o.setCreateTime(null)));
       // 准备参数
       SuperiorApiSkuConfigPageReqVO reqVO = new SuperiorApiSkuConfigPageReqVO();
       reqVO.setHaokaSuperiorApiId(null);
       reqVO.setCode(null);
       reqVO.setName(null);
       reqVO.setRequired(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<SuperiorApiSkuConfigDO> pageResult = superiorApiSkuConfigService.getSuperiorApiSkuConfigPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSuperiorApiSkuConfig, pageResult.getList().get(0));
    }

}