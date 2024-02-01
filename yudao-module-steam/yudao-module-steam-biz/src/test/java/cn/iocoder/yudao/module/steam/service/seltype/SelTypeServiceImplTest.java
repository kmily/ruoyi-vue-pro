package cn.iocoder.yudao.module.steam.service.seltype;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.seltype.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelTypeDO;
import cn.iocoder.yudao.module.steam.dal.mysql.seltype.SelTypeMapper;
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
 * {@link SelTypeServiceImpl} 的单元测试类
 *
 * @author glzaboy
 */
@Import(SelTypeServiceImpl.class)
public class SelTypeServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SelTypeServiceImpl selTypeService;

    @Resource
    private SelTypeMapper selTypeMapper;

    @Test
    public void testCreateSelType_success() {
        // 准备参数
        SelTypeSaveReqVO createReqVO = randomPojo(SelTypeSaveReqVO.class).setId(null);

        // 调用
        Long selTypeId = selTypeService.createSelType(createReqVO);
        // 断言
        assertNotNull(selTypeId);
        // 校验记录的属性是否正确
        SelTypeDO selType = selTypeMapper.selectById(selTypeId);
        assertPojoEquals(createReqVO, selType, "id");
    }

    @Test
    public void testUpdateSelType_success() {
        // mock 数据
        SelTypeDO dbSelType = randomPojo(SelTypeDO.class);
        selTypeMapper.insert(dbSelType);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SelTypeSaveReqVO updateReqVO = randomPojo(SelTypeSaveReqVO.class, o -> {
            o.setId(dbSelType.getId()); // 设置更新的 ID
        });

        // 调用
        selTypeService.updateSelType(updateReqVO);
        // 校验是否更新正确
        SelTypeDO selType = selTypeMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, selType);
    }

    @Test
    public void testUpdateSelType_notExists() {
        // 准备参数
        SelTypeSaveReqVO updateReqVO = randomPojo(SelTypeSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> selTypeService.updateSelType(updateReqVO), SEL_TYPE_NOT_EXISTS);
    }

    @Test
    public void testDeleteSelType_success() {
        // mock 数据
        SelTypeDO dbSelType = randomPojo(SelTypeDO.class);
        selTypeMapper.insert(dbSelType);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSelType.getId();

        // 调用
        selTypeService.deleteSelType(id);
       // 校验数据不存在了
       assertNull(selTypeMapper.selectById(id));
    }

    @Test
    public void testDeleteSelType_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> selTypeService.deleteSelType(id), SEL_TYPE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSelTypePage() {
       // mock 数据
       SelTypeDO dbSelType = randomPojo(SelTypeDO.class, o -> { // 等会查询到
           o.setLocalizedTagName(null);
           o.setColor(null);
       });
       selTypeMapper.insert(dbSelType);
       // 测试 localizedTagName 不匹配
       selTypeMapper.insert(cloneIgnoreId(dbSelType, o -> o.setLocalizedTagName(null)));
       // 测试 color 不匹配
       selTypeMapper.insert(cloneIgnoreId(dbSelType, o -> o.setColor(null)));
       // 准备参数
       SelTypePageReqVO reqVO = new SelTypePageReqVO();
       reqVO.setLocalizedTagName(null);
       reqVO.setColor(null);

       // 调用
       PageResult<SelTypeDO> pageResult = selTypeService.getSelTypePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbSelType, pageResult.getList().get(0));
    }

}