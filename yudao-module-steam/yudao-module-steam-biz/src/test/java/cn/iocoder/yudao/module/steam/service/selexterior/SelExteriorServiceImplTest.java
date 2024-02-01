package cn.iocoder.yudao.module.steam.service.selexterior;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selexterior.SelExteriorDO;
import cn.iocoder.yudao.module.steam.dal.mysql.selexterior.SelExteriorMapper;
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
 * {@link SelExteriorServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(SelExteriorServiceImpl.class)
public class SelExteriorServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SelExteriorServiceImpl selExteriorService;

    @Resource
    private SelExteriorMapper selExteriorMapper;

    @Test
    public void testCreateSelExterior_success() {
        // 准备参数
        SelExteriorSaveReqVO createReqVO = randomPojo(SelExteriorSaveReqVO.class).setId(null);

        // 调用
        Long selExteriorId = selExteriorService.createSelExterior(createReqVO);
        // 断言
        assertNotNull(selExteriorId);
        // 校验记录的属性是否正确
        SelExteriorDO selExterior = selExteriorMapper.selectById(selExteriorId);
        assertPojoEquals(createReqVO, selExterior, "id");
    }

    @Test
    public void testUpdateSelExterior_success() {
        // mock 数据
        SelExteriorDO dbSelExterior = randomPojo(SelExteriorDO.class);
        selExteriorMapper.insert(dbSelExterior);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SelExteriorSaveReqVO updateReqVO = randomPojo(SelExteriorSaveReqVO.class, o -> {
            o.setId(dbSelExterior.getId()); // 设置更新的 ID
        });

        // 调用
        selExteriorService.updateSelExterior(updateReqVO);
        // 校验是否更新正确
        SelExteriorDO selExterior = selExteriorMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, selExterior);
    }

    @Test
    public void testUpdateSelExterior_notExists() {
        // 准备参数
        SelExteriorSaveReqVO updateReqVO = randomPojo(SelExteriorSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> selExteriorService.updateSelExterior(updateReqVO), SEL_EXTERIOR_NOT_EXISTS);
    }

    @Test
    public void testDeleteSelExterior_success() {
        // mock 数据
        SelExteriorDO dbSelExterior = randomPojo(SelExteriorDO.class);
        selExteriorMapper.insert(dbSelExterior);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSelExterior.getId();

        // 调用
        selExteriorService.deleteSelExterior(id);
       // 校验数据不存在了
       assertNull(selExteriorMapper.selectById(id));
    }

    @Test
    public void testDeleteSelExterior_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> selExteriorService.deleteSelExterior(id), SEL_EXTERIOR_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSelExteriorPage() {
       // mock 数据
       SelExteriorDO dbSelExterior = randomPojo(SelExteriorDO.class, o -> { // 等会查询到
           o.setInternalName(null);
           o.setLocalizedTagName(null);
           o.setCreateTime(null);
       });
       selExteriorMapper.insert(dbSelExterior);
       // 测试 internalName 不匹配
       selExteriorMapper.insert(cloneIgnoreId(dbSelExterior, o -> o.setInternalName(null)));
       // 测试 localizedTagName 不匹配
       selExteriorMapper.insert(cloneIgnoreId(dbSelExterior, o -> o.setLocalizedTagName(null)));
       // 测试 createTime 不匹配
       selExteriorMapper.insert(cloneIgnoreId(dbSelExterior, o -> o.setCreateTime(null)));
       // 准备参数
       SelExteriorPageReqVO reqVO = new SelExteriorPageReqVO();
       reqVO.setInternalName(null);
       reqVO.setLocalizedTagName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<SelExteriorDO> pageResult = selExteriorService.getSelExteriorPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSelExterior, pageResult.getList().get(0));
    }

}