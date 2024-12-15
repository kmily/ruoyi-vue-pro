package cn.iocoder.yudao.module.haoka.service.superiorapi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.superiorapi.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.superiorapi.SuperiorApiDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.superiorapi.SuperiorApiMapper;
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
 * {@link SuperiorApiServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(SuperiorApiServiceImpl.class)
public class SuperiorApiServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SuperiorApiServiceImpl superiorApiService;

    @Resource
    private SuperiorApiMapper superiorApiMapper;

    @Test
    public void testCreateSuperiorApi_success() {
        // 准备参数
        SuperiorApiSaveReqVO createReqVO = randomPojo(SuperiorApiSaveReqVO.class).setId(null);

        // 调用
        Long superiorApiId = superiorApiService.createSuperiorApi(createReqVO);
        // 断言
        assertNotNull(superiorApiId);
        // 校验记录的属性是否正确
        SuperiorApiDO superiorApi = superiorApiMapper.selectById(superiorApiId);
        assertPojoEquals(createReqVO, superiorApi, "id");
    }

    @Test
    public void testUpdateSuperiorApi_success() {
        // mock 数据
        SuperiorApiDO dbSuperiorApi = randomPojo(SuperiorApiDO.class);
        superiorApiMapper.insert(dbSuperiorApi);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SuperiorApiSaveReqVO updateReqVO = randomPojo(SuperiorApiSaveReqVO.class, o -> {
            o.setId(dbSuperiorApi.getId()); // 设置更新的 ID
        });

        // 调用
        superiorApiService.updateSuperiorApi(updateReqVO);
        // 校验是否更新正确
        SuperiorApiDO superiorApi = superiorApiMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, superiorApi);
    }

    @Test
    public void testUpdateSuperiorApi_notExists() {
        // 准备参数
        SuperiorApiSaveReqVO updateReqVO = randomPojo(SuperiorApiSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> superiorApiService.updateSuperiorApi(updateReqVO), SUPERIOR_API_NOT_EXISTS);
    }

    @Test
    public void testDeleteSuperiorApi_success() {
        // mock 数据
        SuperiorApiDO dbSuperiorApi = randomPojo(SuperiorApiDO.class);
        superiorApiMapper.insert(dbSuperiorApi);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSuperiorApi.getId();

        // 调用
        superiorApiService.deleteSuperiorApi(id);
       // 校验数据不存在了
       assertNull(superiorApiMapper.selectById(id));
    }

    @Test
    public void testDeleteSuperiorApi_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> superiorApiService.deleteSuperiorApi(id), SUPERIOR_API_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSuperiorApiPage() {
       // mock 数据
       SuperiorApiDO dbSuperiorApi = randomPojo(SuperiorApiDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setEnableSelectNum(null);
           o.setAbnormalOrderHandleMethod(null);
           o.setStatus(null);
           o.setPublishTime(null);
           o.setIsDevConfined(null);
           o.setIsSkuConfined(null);
           o.setDeptId(null);
           o.setCreateTime(null);
       });
       superiorApiMapper.insert(dbSuperiorApi);
       // 测试 name 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setName(null)));
       // 测试 enableSelectNum 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setEnableSelectNum(null)));
       // 测试 abnormalOrderHandleMethod 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setAbnormalOrderHandleMethod(null)));
       // 测试 status 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setStatus(null)));
       // 测试 publishTime 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setPublishTime(null)));
       // 测试 isDevConfined 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setIsDevConfined(null)));
       // 测试 isSkuConfined 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setIsSkuConfined(null)));
       // 测试 deptId 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setDeptId(null)));
       // 测试 createTime 不匹配
       superiorApiMapper.insert(cloneIgnoreId(dbSuperiorApi, o -> o.setCreateTime(null)));
       // 准备参数
       SuperiorApiPageReqVO reqVO = new SuperiorApiPageReqVO();
       reqVO.setName(null);
       reqVO.setEnableSelectNum(null);
       reqVO.setAbnormalOrderHandleMethod(null);
       reqVO.setStatus(null);
       reqVO.setPublishTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setIsDevConfined(null);
       reqVO.setIsSkuConfined(null);
       reqVO.setDeptId(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<SuperiorApiDO> pageResult = superiorApiService.getSuperiorApiPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSuperiorApi, pageResult.getList().get(0));
    }

}