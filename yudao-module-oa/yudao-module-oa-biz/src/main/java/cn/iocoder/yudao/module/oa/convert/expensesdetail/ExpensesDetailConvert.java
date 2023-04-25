package cn.iocoder.yudao.module.oa.convert.expensesdetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expensesdetail.ExpensesDetailDO;

/**
 * 报销明细 Convert
 *
 * @author 管理员
 */
@Mapper
public interface ExpensesDetailConvert {

    ExpensesDetailConvert INSTANCE = Mappers.getMapper(ExpensesDetailConvert.class);

    ExpensesDetailDO convert(ExpensesDetailCreateReqVO bean);

    ExpensesDetailDO convert(ExpensesDetailUpdateReqVO bean);

    ExpensesDetailRespVO convert(ExpensesDetailDO bean);

    List<ExpensesDetailRespVO> convertList(List<ExpensesDetailDO> list);

    PageResult<ExpensesDetailRespVO> convertPage(PageResult<ExpensesDetailDO> page);

    List<ExpensesDetailExcelVO> convertList02(List<ExpensesDetailDO> list);

}
