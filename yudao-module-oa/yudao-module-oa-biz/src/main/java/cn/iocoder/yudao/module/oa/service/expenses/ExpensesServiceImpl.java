package cn.iocoder.yudao.module.oa.service.expenses;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expenses.ExpensesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.expenses.ExpensesConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.expenses.ExpensesMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 报销申请 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class ExpensesServiceImpl implements ExpensesService {

    @Resource
    private ExpensesMapper expensesMapper;

    @Override
    public Long createExpenses(ExpensesCreateReqVO createReqVO) {
        // 插入
        ExpensesDO expenses = ExpensesConvert.INSTANCE.convert(createReqVO);
        expensesMapper.insert(expenses);
        // 返回
        return expenses.getId();
    }

    @Override
    public void updateExpenses(ExpensesUpdateReqVO updateReqVO) {
        // 校验存在
        validateExpensesExists(updateReqVO.getId());
        // 更新
        ExpensesDO updateObj = ExpensesConvert.INSTANCE.convert(updateReqVO);
        expensesMapper.updateById(updateObj);
    }

    @Override
    public void deleteExpenses(Long id) {
        // 校验存在
        validateExpensesExists(id);
        // 删除
        expensesMapper.deleteById(id);
    }

    private void validateExpensesExists(Long id) {
        if (expensesMapper.selectById(id) == null) {
            throw exception(EXPENSES_NOT_EXISTS);
        }
    }

    @Override
    public ExpensesDO getExpenses(Long id) {
        return expensesMapper.selectById(id);
    }

    @Override
    public List<ExpensesDO> getExpensesList(Collection<Long> ids) {
        return expensesMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ExpensesDO> getExpensesPage(ExpensesPageReqVO pageReqVO) {
        return expensesMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ExpensesDO> getExpensesList(ExpensesExportReqVO exportReqVO) {
        return expensesMapper.selectList(exportReqVO);
    }

}
