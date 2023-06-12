package cn.iocoder.yudao.module.oa.service.opportunityfollowlog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunityfollowlog.OpportunityFollowLogDO;
import cn.iocoder.yudao.module.oa.dal.mysql.opportunityfollowlog.OpportunityFollowLogMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link OpportunityFollowLogServiceImpl} 的单元测试类
*
* @author 东海
*/
@Import(OpportunityFollowLogServiceImpl.class)
public class OpportunityFollowLogServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OpportunityFollowLogServiceImpl opportunityFollowLogService;

    @Resource
    private OpportunityFollowLogMapper opportunityFollowLogMapper;

    @Test
    public void testCreateOpportunityFollowLog_success() {
        // 准备参数
        OpportunityFollowLogCreateReqVO reqVO = randomPojo(OpportunityFollowLogCreateReqVO.class);

        // 调用
        Long opportunityFollowLogId = opportunityFollowLogService.createOpportunityFollowLog(reqVO);
        // 断言
        assertNotNull(opportunityFollowLogId);
        // 校验记录的属性是否正确
        OpportunityFollowLogDO opportunityFollowLog = opportunityFollowLogMapper.selectById(opportunityFollowLogId);
        assertPojoEquals(reqVO, opportunityFollowLog);
    }

    @Test
    public void testUpdateOpportunityFollowLog_success() {
        // mock 数据
        OpportunityFollowLogDO dbOpportunityFollowLog = randomPojo(OpportunityFollowLogDO.class);
        opportunityFollowLogMapper.insert(dbOpportunityFollowLog);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OpportunityFollowLogUpdateReqVO reqVO = randomPojo(OpportunityFollowLogUpdateReqVO.class, o -> {
            o.setId(dbOpportunityFollowLog.getId()); // 设置更新的 ID
        });

        // 调用
        opportunityFollowLogService.updateOpportunityFollowLog(reqVO);
        // 校验是否更新正确
        OpportunityFollowLogDO opportunityFollowLog = opportunityFollowLogMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, opportunityFollowLog);
    }

    @Test
    public void testUpdateOpportunityFollowLog_notExists() {
        // 准备参数
        OpportunityFollowLogUpdateReqVO reqVO = randomPojo(OpportunityFollowLogUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> opportunityFollowLogService.updateOpportunityFollowLog(reqVO), OPPORTUNITY_FOLLOW_LOG_NOT_EXISTS);
    }

    @Test
    public void testDeleteOpportunityFollowLog_success() {
        // mock 数据
        OpportunityFollowLogDO dbOpportunityFollowLog = randomPojo(OpportunityFollowLogDO.class);
        opportunityFollowLogMapper.insert(dbOpportunityFollowLog);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbOpportunityFollowLog.getId();

        // 调用
        opportunityFollowLogService.deleteOpportunityFollowLog(id);
       // 校验数据不存在了
       assertNull(opportunityFollowLogMapper.selectById(id));
    }

    @Test
    public void testDeleteOpportunityFollowLog_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> opportunityFollowLogService.deleteOpportunityFollowLog(id), OPPORTUNITY_FOLLOW_LOG_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOpportunityFollowLogPage() {
       // mock 数据
       OpportunityFollowLogDO dbOpportunityFollowLog = randomPojo(OpportunityFollowLogDO.class, o -> { // 等会查询到
           o.setBusinessId(null);
           o.setLogContent(null);
           o.setCreateTime(null);
       });
       opportunityFollowLogMapper.insert(dbOpportunityFollowLog);
       // 测试 businessId 不匹配
       opportunityFollowLogMapper.insert(cloneIgnoreId(dbOpportunityFollowLog, o -> o.setBusinessId(null)));
       // 测试 logContent 不匹配
       opportunityFollowLogMapper.insert(cloneIgnoreId(dbOpportunityFollowLog, o -> o.setLogContent(null)));
       // 测试 createTime 不匹配
       opportunityFollowLogMapper.insert(cloneIgnoreId(dbOpportunityFollowLog, o -> o.setCreateTime(null)));
       // 准备参数
       OpportunityFollowLogPageReqVO reqVO = new OpportunityFollowLogPageReqVO();
       reqVO.setBusinessId(null);
       reqVO.setLogContent(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<OpportunityFollowLogDO> pageResult = opportunityFollowLogService.getOpportunityFollowLogPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOpportunityFollowLog, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOpportunityFollowLogList() {
       // mock 数据
       OpportunityFollowLogDO dbOpportunityFollowLog = randomPojo(OpportunityFollowLogDO.class, o -> { // 等会查询到
           o.setBusinessId(null);
           o.setLogContent(null);
           o.setCreateTime(null);
       });
       opportunityFollowLogMapper.insert(dbOpportunityFollowLog);
       // 测试 businessId 不匹配
       opportunityFollowLogMapper.insert(cloneIgnoreId(dbOpportunityFollowLog, o -> o.setBusinessId(null)));
       // 测试 logContent 不匹配
       opportunityFollowLogMapper.insert(cloneIgnoreId(dbOpportunityFollowLog, o -> o.setLogContent(null)));
       // 测试 createTime 不匹配
       opportunityFollowLogMapper.insert(cloneIgnoreId(dbOpportunityFollowLog, o -> o.setCreateTime(null)));
       // 准备参数
       OpportunityFollowLogExportReqVO reqVO = new OpportunityFollowLogExportReqVO();
       reqVO.setBusinessId(null);
       reqVO.setLogContent(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       List<OpportunityFollowLogDO> list = opportunityFollowLogService.getOpportunityFollowLogList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbOpportunityFollowLog, list.get(0));
    }

}
