package cn.iocoder.yudao.module.steam.service.offlinerechange;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.steam.controller.admin.offlinerechange.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.offlinerechange.OfflineRechangeDO;
import cn.iocoder.yudao.module.steam.dal.mysql.offlinerechange.OfflineRechangeMapper;
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
 * {@link OfflineRechangeServiceImpl} 的单元测试类
 *
 * @author 管理员
 */
@Import(OfflineRechangeServiceImpl.class)
public class OfflineRechangeServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OfflineRechangeServiceImpl offlineRechangeService;

    @Resource
    private OfflineRechangeMapper offlineRechangeMapper;

    @Test
    public void testCreateOfflineRechange_success() {
        // 准备参数
        OfflineRechangeSaveReqVO createReqVO = randomPojo(OfflineRechangeSaveReqVO.class).setId(null);

        // 调用
        Long offlineRechangeId = offlineRechangeService.createOfflineRechange(createReqVO);
        // 断言
        assertNotNull(offlineRechangeId);
        // 校验记录的属性是否正确
        OfflineRechangeDO offlineRechange = offlineRechangeMapper.selectById(offlineRechangeId);
        assertPojoEquals(createReqVO, offlineRechange, "id");
    }

    @Test
    public void testUpdateOfflineRechange_success() {
        // mock 数据
        OfflineRechangeDO dbOfflineRechange = randomPojo(OfflineRechangeDO.class);
        offlineRechangeMapper.insert(dbOfflineRechange);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OfflineRechangeSaveReqVO updateReqVO = randomPojo(OfflineRechangeSaveReqVO.class, o -> {
            o.setId(dbOfflineRechange.getId()); // 设置更新的 ID
        });

        // 调用
        offlineRechangeService.updateOfflineRechange(updateReqVO);
        // 校验是否更新正确
        OfflineRechangeDO offlineRechange = offlineRechangeMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, offlineRechange);
    }

    @Test
    public void testUpdateOfflineRechange_notExists() {
        // 准备参数
        OfflineRechangeSaveReqVO updateReqVO = randomPojo(OfflineRechangeSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> offlineRechangeService.updateOfflineRechange(updateReqVO), OFFLINE_RECHANGE_NOT_EXISTS);
    }

    @Test
    public void testDeleteOfflineRechange_success() {
        // mock 数据
        OfflineRechangeDO dbOfflineRechange = randomPojo(OfflineRechangeDO.class);
        offlineRechangeMapper.insert(dbOfflineRechange);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbOfflineRechange.getId();

        // 调用
        offlineRechangeService.deleteOfflineRechange(id);
       // 校验数据不存在了
       assertNull(offlineRechangeMapper.selectById(id));
    }

    @Test
    public void testDeleteOfflineRechange_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> offlineRechangeService.deleteOfflineRechange(id), OFFLINE_RECHANGE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOfflineRechangePage() {
       // mock 数据
       OfflineRechangeDO dbOfflineRechange = randomPojo(OfflineRechangeDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setUserId(null);
           o.setUserType(null);
           o.setAmount(null);
           o.setComment(null);
       });
       offlineRechangeMapper.insert(dbOfflineRechange);
       // 测试 createTime 不匹配
       offlineRechangeMapper.insert(cloneIgnoreId(dbOfflineRechange, o -> o.setCreateTime(null)));
       // 测试 userId 不匹配
       offlineRechangeMapper.insert(cloneIgnoreId(dbOfflineRechange, o -> o.setUserId(null)));
       // 测试 userType 不匹配
       offlineRechangeMapper.insert(cloneIgnoreId(dbOfflineRechange, o -> o.setUserType(null)));
       // 测试 amount 不匹配
       offlineRechangeMapper.insert(cloneIgnoreId(dbOfflineRechange, o -> o.setAmount(null)));
       // 测试 comment 不匹配
       offlineRechangeMapper.insert(cloneIgnoreId(dbOfflineRechange, o -> o.setComment(null)));
       // 准备参数
       OfflineRechangePageReqVO reqVO = new OfflineRechangePageReqVO();
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setUserId(null);
       reqVO.setUserType(null);
       reqVO.setAmount(null);
       reqVO.setComment(null);

       // 调用
       PageResult<OfflineRechangeDO> pageResult = offlineRechangeService.getOfflineRechangePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOfflineRechange, pageResult.getList().get(0));
    }

}