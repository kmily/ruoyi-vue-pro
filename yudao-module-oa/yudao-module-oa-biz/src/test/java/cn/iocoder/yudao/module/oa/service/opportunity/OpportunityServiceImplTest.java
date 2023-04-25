package cn.iocoder.yudao.module.oa.service.opportunity;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunity.OpportunityDO;
import cn.iocoder.yudao.module.oa.dal.mysql.opportunity.OpportunityMapper;
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
* {@link OpportunityServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(OpportunityServiceImpl.class)
public class OpportunityServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OpportunityServiceImpl opportunityService;

    @Resource
    private OpportunityMapper opportunityMapper;

    @Test
    public void testCreateOpportunity_success() {
        // 准备参数
        OpportunityCreateReqVO reqVO = randomPojo(OpportunityCreateReqVO.class);

        // 调用
        Long opportunityId = opportunityService.createOpportunity(reqVO);
        // 断言
        assertNotNull(opportunityId);
        // 校验记录的属性是否正确
        OpportunityDO opportunity = opportunityMapper.selectById(opportunityId);
        assertPojoEquals(reqVO, opportunity);
    }

    @Test
    public void testUpdateOpportunity_success() {
        // mock 数据
        OpportunityDO dbOpportunity = randomPojo(OpportunityDO.class);
        opportunityMapper.insert(dbOpportunity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        OpportunityUpdateReqVO reqVO = randomPojo(OpportunityUpdateReqVO.class, o -> {
            o.setId(dbOpportunity.getId()); // 设置更新的 ID
        });

        // 调用
        opportunityService.updateOpportunity(reqVO);
        // 校验是否更新正确
        OpportunityDO opportunity = opportunityMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, opportunity);
    }

    @Test
    public void testUpdateOpportunity_notExists() {
        // 准备参数
        OpportunityUpdateReqVO reqVO = randomPojo(OpportunityUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> opportunityService.updateOpportunity(reqVO), OPPORTUNITY_NOT_EXISTS);
    }

    @Test
    public void testDeleteOpportunity_success() {
        // mock 数据
        OpportunityDO dbOpportunity = randomPojo(OpportunityDO.class);
        opportunityMapper.insert(dbOpportunity);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbOpportunity.getId();

        // 调用
        opportunityService.deleteOpportunity(id);
       // 校验数据不存在了
       assertNull(opportunityMapper.selectById(id));
    }

    @Test
    public void testDeleteOpportunity_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> opportunityService.deleteOpportunity(id), OPPORTUNITY_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOpportunityPage() {
       // mock 数据
       OpportunityDO dbOpportunity = randomPojo(OpportunityDO.class, o -> { // 等会查询到
           o.setBusinessTitle(null);
           o.setStatus(null);
           o.setCreateBy(null);
       });
       opportunityMapper.insert(dbOpportunity);
       // 测试 businessTitle 不匹配
       opportunityMapper.insert(cloneIgnoreId(dbOpportunity, o -> o.setBusinessTitle(null)));
       // 测试 status 不匹配
       opportunityMapper.insert(cloneIgnoreId(dbOpportunity, o -> o.setStatus(null)));
       // 测试 createBy 不匹配
       opportunityMapper.insert(cloneIgnoreId(dbOpportunity, o -> o.setCreateBy(null)));
       // 准备参数
       OpportunityPageReqVO reqVO = new OpportunityPageReqVO();
       reqVO.setBusinessTitle(null);
       reqVO.setStatus(null);
       reqVO.setCreateBy(null);

       // 调用
       PageResult<OpportunityDO> pageResult = opportunityService.getOpportunityPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbOpportunity, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetOpportunityList() {
       // mock 数据
       OpportunityDO dbOpportunity = randomPojo(OpportunityDO.class, o -> { // 等会查询到
           o.setBusinessTitle(null);
           o.setStatus(null);
           o.setCreateBy(null);
       });
       opportunityMapper.insert(dbOpportunity);
       // 测试 businessTitle 不匹配
       opportunityMapper.insert(cloneIgnoreId(dbOpportunity, o -> o.setBusinessTitle(null)));
       // 测试 status 不匹配
       opportunityMapper.insert(cloneIgnoreId(dbOpportunity, o -> o.setStatus(null)));
       // 测试 createBy 不匹配
       opportunityMapper.insert(cloneIgnoreId(dbOpportunity, o -> o.setCreateBy(null)));
       // 准备参数
       OpportunityExportReqVO reqVO = new OpportunityExportReqVO();
       reqVO.setBusinessTitle(null);
       reqVO.setStatus(null);
       reqVO.setCreateBy(null);

       // 调用
       List<OpportunityDO> list = opportunityService.getOpportunityList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbOpportunity, list.get(0));
    }

}
