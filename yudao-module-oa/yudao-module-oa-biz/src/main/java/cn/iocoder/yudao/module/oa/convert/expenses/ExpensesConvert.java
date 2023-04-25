package cn.iocoder.yudao.module.oa.convert.expenses;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.expenses.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expenses.ExpensesDO;

/**
 * 报销申请 Convert
 *
 * @author 管理员
 */
@Mapper
public interface ExpensesConvert {

    ExpensesConvert INSTANCE = Mappers.getMapper(ExpensesConvert.class);

    ExpensesDO convert(ExpensesCreateReqVO bean);

    ExpensesDO convert(ExpensesUpdateReqVO bean);

    ExpensesRespVO convert(ExpensesDO bean);

    List<ExpensesRespVO> convertList(List<ExpensesDO> list);

    PageResult<ExpensesRespVO> convertPage(PageResult<ExpensesDO> page);

    List<ExpensesExcelVO> convertList02(List<ExpensesDO> list);

}
