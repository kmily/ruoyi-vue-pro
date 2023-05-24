package cn.iocoder.yudao.module.oa.service.expenses;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.ExpensesCreateReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.ExpensesExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.ExpensesPageReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.ExpensesUpdateReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.expenses.ExpensesDO;
import cn.iocoder.yudao.module.oa.dal.mysql.expenses.ExpensesMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.EXPENSES_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
* {@link ExpensesServiceImpl} 的单元测试类
*
* @author 管理员
*/
@Import(ExpensesServiceImpl.class)
public class ExpensesServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ExpensesServiceImpl expensesService;

    @Resource
    private ExpensesMapper expensesMapper;

    @Test
    public void testCreateExpenses_success() {
        // 准备参数
        ExpensesCreateReqVO reqVO = randomPojo(ExpensesCreateReqVO.class);

        // 调用
        Long expensesId = expensesService.createExpenses(reqVO);
        // 断言
        assertNotNull(expensesId);
        // 校验记录的属性是否正确
        ExpensesDO expenses = expensesMapper.selectById(expensesId);
        assertPojoEquals(reqVO, expenses);
    }

    @Test
    public void testUpdateExpenses_success() {
        // mock 数据
        ExpensesDO dbExpenses = randomPojo(ExpensesDO.class);
        expensesMapper.insert(dbExpenses);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ExpensesUpdateReqVO reqVO = randomPojo(ExpensesUpdateReqVO.class, o -> {
            o.setId(dbExpenses.getId()); // 设置更新的 ID
        });

        // 调用
        expensesService.updateExpenses(reqVO);
        // 校验是否更新正确
        ExpensesDO expenses = expensesMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, expenses);
    }

    @Test
    public void testUpdateExpenses_notExists() {
        // 准备参数
        ExpensesUpdateReqVO reqVO = randomPojo(ExpensesUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> expensesService.updateExpenses(reqVO), EXPENSES_NOT_EXISTS);
    }

    @Test
    public void testDeleteExpenses_success() {
        // mock 数据
        ExpensesDO dbExpenses = randomPojo(ExpensesDO.class);
        expensesMapper.insert(dbExpenses);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbExpenses.getId();

        // 调用
        expensesService.deleteExpenses(id);
       // 校验数据不存在了
       assertNull(expensesMapper.selectById(id));
    }

    @Test
    public void testDeleteExpenses_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> expensesService.deleteExpenses(id), EXPENSES_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetExpensesPage() {
       // mock 数据
       ExpensesDO dbExpenses = randomPojo(ExpensesDO.class, o -> { // 等会查询到
           o.setExpensesType(null);
           o.setExhibitName(null);
           o.setApprovalStatus(null);
           o.setCreator(null);
       });
       expensesMapper.insert(dbExpenses);
       // 测试 expensesType 不匹配
       expensesMapper.insert(cloneIgnoreId(dbExpenses, o -> o.setExpensesType(null)));
       // 测试 exhibitName 不匹配
       expensesMapper.insert(cloneIgnoreId(dbExpenses, o -> o.setExhibitName(null)));
       // 测试 approvalStatus 不匹配
       expensesMapper.insert(cloneIgnoreId(dbExpenses, o -> o.setApprovalStatus(null)));
       // 测试 creator 不匹配
       expensesMapper.insert(cloneIgnoreId(dbExpenses, o -> o.setCreator(null)));
       // 准备参数
       ExpensesPageReqVO reqVO = new ExpensesPageReqVO();
       reqVO.setExpensesType(null);
       reqVO.setExhibitName(null);
       reqVO.setApprovalStatus(null);
       reqVO.setCreator(null);

       // 调用
       PageResult<ExpensesDO> pageResult = expensesService.getExpensesPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbExpenses, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetExpensesList() {
       // mock 数据
       ExpensesDO dbExpenses = randomPojo(ExpensesDO.class, o -> { // 等会查询到
           o.setExpensesType(null);
           o.setExhibitName(null);
           o.setApprovalStatus(null);
           o.setCreator(null);
       });
       expensesMapper.insert(dbExpenses);
       // 测试 expensesType 不匹配
       expensesMapper.insert(cloneIgnoreId(dbExpenses, o -> o.setExpensesType(null)));
       // 测试 exhibitName 不匹配
       expensesMapper.insert(cloneIgnoreId(dbExpenses, o -> o.setExhibitName(null)));
       // 测试 approvalStatus 不匹配
       expensesMapper.insert(cloneIgnoreId(dbExpenses, o -> o.setApprovalStatus(null)));
       // 测试 creator 不匹配
       expensesMapper.insert(cloneIgnoreId(dbExpenses, o -> o.setCreator(null)));
       // 准备参数
       ExpensesExportReqVO reqVO = new ExpensesExportReqVO();
       reqVO.setExpensesType(null);
       reqVO.setExhibitName(null);
       reqVO.setApprovalStatus(null);
       reqVO.setCreator(null);

       // 调用
       List<ExpensesDO> list = expensesService.getExpensesList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbExpenses, list.get(0));
    }

}
