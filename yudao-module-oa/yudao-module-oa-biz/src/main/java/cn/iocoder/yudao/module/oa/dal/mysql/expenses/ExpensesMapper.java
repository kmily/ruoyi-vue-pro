package cn.iocoder.yudao.module.oa.dal.mysql.expenses;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.ExpensesExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.ExpensesPageReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.expenses.ExpensesDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 报销申请 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ExpensesMapper extends BaseMapperX<ExpensesDO> {

    default PageResult<ExpensesDO> selectPage(ExpensesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ExpensesDO>()
                .eqIfPresent(ExpensesDO::getExpensesType, reqVO.getExpensesType())
                .likeIfPresent(ExpensesDO::getExhibitName, reqVO.getExhibitName())
                .eqIfPresent(ExpensesDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(ExpensesDO::getCreator, reqVO.getCreator())
                .orderByDesc(ExpensesDO::getId));
    }

    default List<ExpensesDO> selectList(ExpensesExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ExpensesDO>()
                .eqIfPresent(ExpensesDO::getExpensesType, reqVO.getExpensesType())
                .likeIfPresent(ExpensesDO::getExhibitName, reqVO.getExhibitName())
                .eqIfPresent(ExpensesDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(ExpensesDO::getCreator, reqVO.getCreator())
                .orderByDesc(ExpensesDO::getId));
    }

}
