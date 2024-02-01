package cn.iocoder.yudao.module.steam.service.selitemset;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.selitemset.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selitemset.SelItemsetDO;
import cn.iocoder.yudao.module.steam.dal.mysql.selitemset.SelItemsetMapper;
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
 * {@link SelItemsetServiceImpl} 的单元测试类
 *
 * @author glzaboy
 */
@Import(SelItemsetServiceImpl.class)
public class SelItemsetServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SelItemsetServiceImpl selItemsetService;

    @Resource
    private SelItemsetMapper selItemsetMapper;

    @Test
    public void testCreateSelItemset_success() {
        // 准备参数
        SelItemsetSaveReqVO createReqVO = randomPojo(SelItemsetSaveReqVO.class).setId(null);

        // 调用
        Long selItemsetId = selItemsetService.createSelItemset(createReqVO);
        // 断言
        assertNotNull(selItemsetId);
        // 校验记录的属性是否正确
        SelItemsetDO selItemset = selItemsetMapper.selectById(selItemsetId);
        assertPojoEquals(createReqVO, selItemset, "id");
    }

    @Test
    public void testUpdateSelItemset_success() {
        // mock 数据
        SelItemsetDO dbSelItemset = randomPojo(SelItemsetDO.class);
        selItemsetMapper.insert(dbSelItemset);// @Sql: 先插入出一条存在的数据
        // 准备参数
        SelItemsetSaveReqVO updateReqVO = randomPojo(SelItemsetSaveReqVO.class, o -> {
            o.setId(dbSelItemset.getId()); // 设置更新的 ID
        });

        // 调用
        selItemsetService.updateSelItemset(updateReqVO);
        // 校验是否更新正确
        SelItemsetDO selItemset = selItemsetMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, selItemset);
    }

    @Test
    public void testUpdateSelItemset_notExists() {
        // 准备参数
        SelItemsetSaveReqVO updateReqVO = randomPojo(SelItemsetSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> selItemsetService.updateSelItemset(updateReqVO), SEL_ITEMSET_NOT_EXISTS);
    }

    @Test
    public void testDeleteSelItemset_success() {
        // mock 数据
        SelItemsetDO dbSelItemset = randomPojo(SelItemsetDO.class);
        selItemsetMapper.insert(dbSelItemset);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSelItemset.getId();

        // 调用
        selItemsetService.deleteSelItemset(id);
       // 校验数据不存在了
       assertNull(selItemsetMapper.selectById(id));
    }

    @Test
    public void testDeleteSelItemset_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> selItemsetService.deleteSelItemset(id), SEL_ITEMSET_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetSelItemsetList() {
       // mock 数据
       SelItemsetDO dbSelItemset = randomPojo(SelItemsetDO.class, o -> { // 等会查询到
           o.setParentId(null);
           o.setInternalName(null);
           o.setLocalizedTagName(null);
       });
       selItemsetMapper.insert(dbSelItemset);
       // 测试 parentId 不匹配
       selItemsetMapper.insert(cloneIgnoreId(dbSelItemset, o -> o.setParentId(null)));
       // 测试 internalName 不匹配
       selItemsetMapper.insert(cloneIgnoreId(dbSelItemset, o -> o.setInternalName(null)));
       // 测试 localizedTagName 不匹配
       selItemsetMapper.insert(cloneIgnoreId(dbSelItemset, o -> o.setLocalizedTagName(null)));
       // 准备参数
       SelItemsetListReqVO reqVO = new SelItemsetListReqVO();
       reqVO.setParentId(null);
       reqVO.setInternalName(null);
       reqVO.setLocalizedTagName(null);

       // 调用
       List<SelItemsetDO> list = selItemsetService.getSelItemsetList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbSelItemset, list.get(0));
    }

}