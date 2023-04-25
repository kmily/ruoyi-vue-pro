package cn.iocoder.yudao.module.oa.service.expensesdetail;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expensesdetail.ExpensesDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 报销明细 Service 接口
 *
 * @author 管理员
 */
public interface ExpensesDetailService {

    /**
     * 创建报销明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExpensesDetail(@Valid ExpensesDetailCreateReqVO createReqVO);

    /**
     * 更新报销明细
     *
     * @param updateReqVO 更新信息
     */
    void updateExpensesDetail(@Valid ExpensesDetailUpdateReqVO updateReqVO);

    /**
     * 删除报销明细
     *
     * @param id 编号
     */
    void deleteExpensesDetail(Long id);

    /**
     * 获得报销明细
     *
     * @param id 编号
     * @return 报销明细
     */
    ExpensesDetailDO getExpensesDetail(Long id);

    /**
     * 获得报销明细列表
     *
     * @param ids 编号
     * @return 报销明细列表
     */
    List<ExpensesDetailDO> getExpensesDetailList(Collection<Long> ids);

    /**
     * 获得报销明细分页
     *
     * @param pageReqVO 分页查询
     * @return 报销明细分页
     */
    PageResult<ExpensesDetailDO> getExpensesDetailPage(ExpensesDetailPageReqVO pageReqVO);

    /**
     * 获得报销明细列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 报销明细列表
     */
    List<ExpensesDetailDO> getExpensesDetailList(ExpensesDetailExportReqVO exportReqVO);

}
