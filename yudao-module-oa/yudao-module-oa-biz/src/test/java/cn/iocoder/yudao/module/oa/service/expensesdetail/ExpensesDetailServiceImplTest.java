package cn.iocoder.yudao.module.oa.service.expensesdetail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expensesdetail.ExpensesDetailDO;
import cn.iocoder.yudao.module.oa.dal.mysql.expensesdetail.ExpensesDetailMapper;
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
* {@link ExpensesDetailServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(ExpensesDetailServiceImpl.class)
public class ExpensesDetailServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ExpensesDetailServiceImpl expensesDetailService;

    @Resource
    private ExpensesDetailMapper expensesDetailMapper;

    @Test
    public void testCreateExpensesDetail_success() {
        // 准备参数
        ExpensesDetailCreateReqVO reqVO = randomPojo(ExpensesDetailCreateReqVO.class);

        // 调用
        Long expensesDetailId = expensesDetailService.createExpensesDetail(reqVO);
        // 断言
        assertNotNull(expensesDetailId);
        // 校验记录的属性是否正确
        ExpensesDetailDO expensesDetail = expensesDetailMapper.selectById(expensesDetailId);
        assertPojoEquals(reqVO, expensesDetail);
    }

    @Test
    public void testUpdateExpensesDetail_success() {
        // mock 数据
        ExpensesDetailDO dbExpensesDetail = randomPojo(ExpensesDetailDO.class);
        expensesDetailMapper.insert(dbExpensesDetail);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ExpensesDetailUpdateReqVO reqVO = randomPojo(ExpensesDetailUpdateReqVO.class, o -> {
            o.setId(dbExpensesDetail.getId()); // 设置更新的 ID
        });

        // 调用
        expensesDetailService.updateExpensesDetail(reqVO);
        // 校验是否更新正确
        ExpensesDetailDO expensesDetail = expensesDetailMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, expensesDetail);
    }

    @Test
    public void testUpdateExpensesDetail_notExists() {
        // 准备参数
        ExpensesDetailUpdateReqVO reqVO = randomPojo(ExpensesDetailUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> expensesDetailService.updateExpensesDetail(reqVO), EXPENSES_DETAIL_NOT_EXISTS);
    }

    @Test
    public void testDeleteExpensesDetail_success() {
        // mock 数据
        ExpensesDetailDO dbExpensesDetail = randomPojo(ExpensesDetailDO.class);
        expensesDetailMapper.insert(dbExpensesDetail);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbExpensesDetail.getId();

        // 调用
        expensesDetailService.deleteExpensesDetail(id);
       // 校验数据不存在了
       assertNull(expensesDetailMapper.selectById(id));
    }

    @Test
    public void testDeleteExpensesDetail_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> expensesDetailService.deleteExpensesDetail(id), EXPENSES_DETAIL_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetExpensesDetailPage() {
       // mock 数据
       ExpensesDetailDO dbExpensesDetail = randomPojo(ExpensesDetailDO.class, o -> { // 等会查询到
           o.setDetailType(null);
       });
       expensesDetailMapper.insert(dbExpensesDetail);
       // 测试 detailType 不匹配
       expensesDetailMapper.insert(cloneIgnoreId(dbExpensesDetail, o -> o.setDetailType(null)));
       // 准备参数
       ExpensesDetailPageReqVO reqVO = new ExpensesDetailPageReqVO();
       reqVO.setDetailType(null);

       // 调用
       PageResult<ExpensesDetailDO> pageResult = expensesDetailService.getExpensesDetailPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbExpensesDetail, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetExpensesDetailList() {
       // mock 数据
       ExpensesDetailDO dbExpensesDetail = randomPojo(ExpensesDetailDO.class, o -> { // 等会查询到
           o.setDetailType(null);
       });
       expensesDetailMapper.insert(dbExpensesDetail);
       // 测试 detailType 不匹配
       expensesDetailMapper.insert(cloneIgnoreId(dbExpensesDetail, o -> o.setDetailType(null)));
       // 准备参数
       ExpensesDetailExportReqVO reqVO = new ExpensesDetailExportReqVO();
       reqVO.setDetailType(null);

       // 调用
       List<ExpensesDetailDO> list = expensesDetailService.getExpensesDetailList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbExpensesDetail, list.get(0));
    }

}
