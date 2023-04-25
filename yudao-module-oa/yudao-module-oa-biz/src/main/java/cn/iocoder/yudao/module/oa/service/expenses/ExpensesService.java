package cn.iocoder.yudao.module.oa.service.expenses;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expenses.ExpensesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 报销申请 Service 接口
 *
 * @author 管理员
 */
public interface ExpensesService {

    /**
     * 创建报销申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExpenses(@Valid ExpensesCreateReqVO createReqVO);

    /**
     * 更新报销申请
     *
     * @param updateReqVO 更新信息
     */
    void updateExpenses(@Valid ExpensesUpdateReqVO updateReqVO);

    /**
     * 删除报销申请
     *
     * @param id 编号
     */
    void deleteExpenses(Long id);

    /**
     * 获得报销申请
     *
     * @param id 编号
     * @return 报销申请
     */
    ExpensesDO getExpenses(Long id);

    /**
     * 获得报销申请列表
     *
     * @param ids 编号
     * @return 报销申请列表
     */
    List<ExpensesDO> getExpensesList(Collection<Long> ids);

    /**
     * 获得报销申请分页
     *
     * @param pageReqVO 分页查询
     * @return 报销申请分页
     */
    PageResult<ExpensesDO> getExpensesPage(ExpensesPageReqVO pageReqVO);

    /**
     * 获得报销申请列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 报销申请列表
     */
    List<ExpensesDO> getExpensesList(ExpensesExportReqVO exportReqVO);

}
