package cn.iocoder.yudao.module.haoka.service.superiorapi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDevConfigDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi.SuperiorApiDevConfigMapper;
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
 * {@link SuperiorApiDevConfigServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(SuperiorApiDevConfigServiceImpl.class)
public class SuperiorApiDevConfigServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SuperiorApiDevConfigServiceImpl superiorApiDevConfigService;

    @Resource
    private SuperiorApiDevConfigMapper superiorApiDevConfigMapper;

    @Test
    public void testCreateSuperiorApiDevConfig_success() {
        // 准备参数
        SuperiorApiDevConfigSaveReqVO createReqVO = randomPojo(SuperiorApiDevConfigSaveReqVO.class).setId(null);

        // 调用
        Long superiorApiDevConfigId = superiorApiDevConfigService.createSuperiorApiDevConfig(createReqVO);
        // 断言
        assertNotNull(superiorApiDevConfigId);
        // 校验记录的属性是否正确
        SuperiorApiDevConfigDO superiorApiDevConfig = superiorApiDevConfigMapper.selectById(superiorApiDevConfigId);
        assertPojoEquals(createReqVO, superiorApiDevConfig, "id");
    }

    @Test
    public void testUpdateSuperiorApiDevConfig_success() {
        // mock 数据
        SuperiorApiDevConfigDO dbSuperiorApiDevConfig = randomPojo(SuperiorApiDevConfigDO.class);
        superiorApiDevConfigMapper.insert(dbSuperiorApiDevConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SuperiorApiDevConfigSaveReqVO updateReqVO = randomPojo(SuperiorApiDevConfigSaveReqVO.class, o -> {
            o.setId(dbSuperiorApiDevConfig.getId()); // 设置更新的 ID
        });

        // 调用
        superiorApiDevConfigService.updateSuperiorApiDevConfig(updateReqVO);
        // 校验是否更新正确
        SuperiorApiDevConfigDO superiorApiDevConfig = superiorApiDevConfigMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, superiorApiDevConfig);
    }

    @Test
    public void testUpdateSuperiorApiDevConfig_notExists() {
        // 准备参数
        SuperiorApiDevConfigSaveReqVO updateReqVO = randomPojo(SuperiorApiDevConfigSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> superiorApiDevConfigService.updateSuperiorApiDevConfig(updateReqVO), SUPERIOR_API_DEV_CONFIG_NOT_EXISTS);
    }

    @Test
    public void testDeleteSuperiorApiDevConfig_success() {
        // mock 数据
        SuperiorApiDevConfigDO dbSuperiorApiDevConfig = randomPojo(SuperiorApiDevConfigDO.class);
        superiorApiDevConfigMapper.insert(dbSuperiorApiDevConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSuperiorApiDevConfig.getId();

        // 调用
        superiorApiDevConfigService.deleteSuperiorApiDevConfig(id);
       // 校验数据不存在了
       assertNull(superiorApiDevConfigMapper.selectById(id));
    }

    @Test
    public void testDeleteSuperiorApiDevConfig_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> superiorApiDevConfigService.deleteSuperiorApiDevConfig(id), SUPERIOR_API_DEV_CONFIG_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSuperiorApiDevConfigPage() {
       // mock 数据
       SuperiorApiDevConfigDO dbSuperiorApiDevConfig = randomPojo(SuperiorApiDevConfigDO.class, o -> { // 等会查询到
           o.setHaokaSuperiorApiId(null);
           o.setCode(null);
           o.setName(null);
           o.setValue(null);
           o.setRemarks(null);
           o.setInputType(null);
           o.setInputSelectValues(null);
           o.setCreateTime(null);
       });
       superiorApiDevConfigMapper.insert(dbSuperiorApiDevConfig);
       // 测试 haokaSuperiorApiId 不匹配
       superiorApiDevConfigMapper.insert(cloneIgnoreId(dbSuperiorApiDevConfig, o -> o.setHaokaSuperiorApiId(null)));
       // 测试 code 不匹配
       superiorApiDevConfigMapper.insert(cloneIgnoreId(dbSuperiorApiDevConfig, o -> o.setCode(null)));
       // 测试 name 不匹配
       superiorApiDevConfigMapper.insert(cloneIgnoreId(dbSuperiorApiDevConfig, o -> o.setName(null)));
       // 测试 value 不匹配
       superiorApiDevConfigMapper.insert(cloneIgnoreId(dbSuperiorApiDevConfig, o -> o.setValue(null)));
       // 测试 remarks 不匹配
       superiorApiDevConfigMapper.insert(cloneIgnoreId(dbSuperiorApiDevConfig, o -> o.setRemarks(null)));
       // 测试 inputType 不匹配
       superiorApiDevConfigMapper.insert(cloneIgnoreId(dbSuperiorApiDevConfig, o -> o.setInputType(null)));
       // 测试 inputSelectValues 不匹配
       superiorApiDevConfigMapper.insert(cloneIgnoreId(dbSuperiorApiDevConfig, o -> o.setInputSelectValues(null)));
       // 测试 createTime 不匹配
       superiorApiDevConfigMapper.insert(cloneIgnoreId(dbSuperiorApiDevConfig, o -> o.setCreateTime(null)));
       // 准备参数
       SuperiorApiDevConfigPageReqVO reqVO = new SuperiorApiDevConfigPageReqVO();
       reqVO.setHaokaSuperiorApiId(null);
       reqVO.setCode(null);
       reqVO.setName(null);
       reqVO.setValue(null);
       reqVO.setRemarks(null);
       reqVO.setInputType(null);
       reqVO.setInputSelectValues(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<SuperiorApiDevConfigDO> pageResult = superiorApiDevConfigService.getSuperiorApiDevConfigPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSuperiorApiDevConfig, pageResult.getList().get(0));
    }

}