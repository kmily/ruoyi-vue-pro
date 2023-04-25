package cn.iocoder.yudao.module.oa.dal.mysql.expensesdetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.oa.dal.dataobject.expensesdetail.ExpensesDetailDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo.*;

/**
 * 报销明细 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ExpensesDetailMapper extends BaseMapperX<ExpensesDetailDO> {

    default PageResult<ExpensesDetailDO> selectPage(ExpensesDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ExpensesDetailDO>()
                .eqIfPresent(ExpensesDetailDO::getDetailType, reqVO.getDetailType())
                .orderByDesc(ExpensesDetailDO::getId));
    }

    default List<ExpensesDetailDO> selectList(ExpensesDetailExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ExpensesDetailDO>()
                .eqIfPresent(ExpensesDetailDO::getDetailType, reqVO.getDetailType())
                .orderByDesc(ExpensesDetailDO::getId));
    }

}
