package cn.iocoder.yudao.module.haoka.service.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.haoka.controller.admin.demo.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.demo.HaokaDemoDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.demo.HaokaDemoMapper;
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
 * {@link HaokaDemoServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(HaokaDemoServiceImpl.class)
public class HaokaDemoServiceImplTest extends BaseDbUnitTest {

    @Resource
    private HaokaDemoServiceImpl demoService;

    @Resource
    private HaokaDemoMapper demoMapper;

    @Test
    public void testCreateDemo_success() {
        // 准备参数
        HaokaDemoSaveReqVO createReqVO = randomPojo(HaokaDemoSaveReqVO.class).setId(null);

        // 调用
        Long demoId = demoService.createDemo(createReqVO);
        // 断言
        assertNotNull(demoId);
        // 校验记录的属性是否正确
        HaokaDemoDO demo = demoMapper.selectById(demoId);
        assertPojoEquals(createReqVO, demo, "id");
    }

    @Test
    public void testUpdateDemo_success() {
        // mock 数据
        HaokaDemoDO dbDemo = randomPojo(HaokaDemoDO.class);
        demoMapper.insert(dbDemo);// @Sql: 先插入出一条存在的数据
        // 准备参数
        HaokaDemoSaveReqVO updateReqVO = randomPojo(HaokaDemoSaveReqVO.class, o -> {
            o.setId(dbDemo.getId()); // 设置更新的 ID
        });

        // 调用
        demoService.updateDemo(updateReqVO);
        // 校验是否更新正确
        HaokaDemoDO demo = demoMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, demo);
    }

    @Test
    public void testUpdateDemo_notExists() {
        // 准备参数
        HaokaDemoSaveReqVO updateReqVO = randomPojo(HaokaDemoSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> demoService.updateDemo(updateReqVO), DEMO_NOT_EXISTS);
    }

    @Test
    public void testDeleteDemo_success() {
        // mock 数据
        HaokaDemoDO dbDemo = randomPojo(HaokaDemoDO.class);
        demoMapper.insert(dbDemo);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbDemo.getId();

        // 调用
        demoService.deleteDemo(id);
       // 校验数据不存在了
       assertNull(demoMapper.selectById(id));
    }

    @Test
    public void testDeleteDemo_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> demoService.deleteDemo(id), DEMO_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetDemoPage() {
       // mock 数据
       HaokaDemoDO dbDemo = randomPojo(HaokaDemoDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setAge(null);
           o.setAgent(null);
           o.setCreateTime(null);
       });
       demoMapper.insert(dbDemo);
       // 测试 name 不匹配
       demoMapper.insert(cloneIgnoreId(dbDemo, o -> o.setName(null)));
       // 测试 age 不匹配
       demoMapper.insert(cloneIgnoreId(dbDemo, o -> o.setAge(null)));
       // 测试 agent 不匹配
       demoMapper.insert(cloneIgnoreId(dbDemo, o -> o.setAgent(null)));
       // 测试 createTime 不匹配
       demoMapper.insert(cloneIgnoreId(dbDemo, o -> o.setCreateTime(null)));
       // 准备参数
       HaokaDemoPageReqVO reqVO = new HaokaDemoPageReqVO();
       reqVO.setName(null);
       reqVO.setAge(null);
       reqVO.setAgent(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<HaokaDemoDO> pageResult = demoService.getDemoPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbDemo, pageResult.getList().get(0));
    }

}