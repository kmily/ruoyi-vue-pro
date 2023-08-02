package cn.iocoder.yudao.module.promotion.service.bargainactivity;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityExportReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityPageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityUpdateReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargainactivity.BargainActivityDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.bargainactivity.BargainActivityMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildBetweenTime;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.promotion.enums.ErrorCodeConstants.BARGAIN_ACTIVITY_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BargainActivityServiceImpl} 的单元测试类
 *
 * @author WangBosheng
 */
@Import(BargainActivityServiceImpl.class)
public class BargainActivityServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BargainActivityServiceImpl bargainActivityService;

    @Resource
    private BargainActivityMapper bargainActivityMapper;

    @Test
    public void testCreateBargainActivity_success() {
        // 准备参数
        BargainActivityCreateReqVO reqVO = randomPojo(BargainActivityCreateReqVO.class);

        // 调用
        Long bargainActivityId = bargainActivityService.createBargainActivity(reqVO);
        // 断言
        assertNotNull(bargainActivityId);
        // 校验记录的属性是否正确
        BargainActivityDO bargainActivity = bargainActivityMapper.selectById(bargainActivityId);
        assertPojoEquals(reqVO, bargainActivity);
    }

    @Test
    public void testUpdateBargainActivity_success() {
        // mock 数据
        BargainActivityDO dbBargainActivity = randomPojo(BargainActivityDO.class);
        bargainActivityMapper.insert(dbBargainActivity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        BargainActivityUpdateReqVO reqVO = randomPojo(BargainActivityUpdateReqVO.class, o -> {
            o.setId(dbBargainActivity.getId()); // 设置更新的 ID
        });

        // 调用
        bargainActivityService.updateBargainActivity(reqVO);
        // 校验是否更新正确
        BargainActivityDO bargainActivity = bargainActivityMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, bargainActivity);
    }

    @Test
    public void testUpdateBargainActivity_notExists() {
        // 准备参数
        BargainActivityUpdateReqVO reqVO = randomPojo(BargainActivityUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> bargainActivityService.updateBargainActivity(reqVO), BARGAIN_ACTIVITY_NOT_EXISTS);
    }

    @Test
    public void testDeleteBargainActivity_success() {
        // mock 数据
        BargainActivityDO dbBargainActivity = randomPojo(BargainActivityDO.class);
        bargainActivityMapper.insert(dbBargainActivity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbBargainActivity.getId();

        // 调用
        bargainActivityService.deleteBargainActivity(id);
       // 校验数据不存在了
       assertNull(bargainActivityMapper.selectById(id));
    }

    @Test
    public void testDeleteBargainActivity_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> bargainActivityService.deleteBargainActivity(id), BARGAIN_ACTIVITY_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBargainActivityPage() {
       // mock 数据
       BargainActivityDO dbBargainActivity = randomPojo(BargainActivityDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setStartTime(null);
           o.setEndTime(null);
           o.setStatus(null);
           o.setBargainCount(null);
           o.setStock(null);
           o.setCreateTime(null);
           o.setPeopleNum(null);
       });
       bargainActivityMapper.insert(dbBargainActivity);
       // 测试 name 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setName(null)));
       // 测试 startTime 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setStartTime(null)));
       // 测试 endTime 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setEndTime(null)));
       // 测试 status 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setStatus(null)));
       // 测试 bargainCount 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setBargainCount(null)));
       // 测试 stock 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setStock(null)));
       // 测试 createTime 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setCreateTime(null)));
       // 测试 peopleNum 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setPeopleNum(null)));
       // 准备参数
       BargainActivityPageReqVO reqVO = new BargainActivityPageReqVO();
       reqVO.setName(null);
       reqVO.setStartTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setEndTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);
       reqVO.setBargainCount(null);
       reqVO.setStock(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setPeopleNum(null);

       // 调用
       PageResult<BargainActivityDO> pageResult = bargainActivityService.getBargainActivityPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbBargainActivity, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBargainActivityList() {
       // mock 数据
       BargainActivityDO dbBargainActivity = randomPojo(BargainActivityDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setStartTime(null);
           o.setEndTime(null);
           o.setStatus(null);
           o.setBargainCount(null);
           o.setStock(null);
           o.setCreateTime(null);
           o.setPeopleNum(null);
       });
       bargainActivityMapper.insert(dbBargainActivity);
       // 测试 name 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setName(null)));
       // 测试 startTime 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setStartTime(null)));
       // 测试 endTime 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setEndTime(null)));
       // 测试 status 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setStatus(null)));
       // 测试 bargainCount 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setBargainCount(null)));
       // 测试 stock 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setStock(null)));
       // 测试 createTime 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setCreateTime(null)));
       // 测试 peopleNum 不匹配
       bargainActivityMapper.insert(cloneIgnoreId(dbBargainActivity, o -> o.setPeopleNum(null)));
       // 准备参数
       BargainActivityExportReqVO reqVO = new BargainActivityExportReqVO();
       reqVO.setName(null);
       reqVO.setStartTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setEndTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);
       reqVO.setBargainCount(null);
       reqVO.setStock(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setPeopleNum(null);

       // 调用
       List<BargainActivityDO> list = bargainActivityService.getBargainActivityList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbBargainActivity, list.get(0));
    }

}
