package cn.iocoder.yudao.module.oa.service.expensesdetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.expensesdetail.ExpensesDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.expensesdetail.ExpensesDetailConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.expensesdetail.ExpensesDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 报销明细 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class ExpensesDetailServiceImpl implements ExpensesDetailService {

    @Resource
    private ExpensesDetailMapper expensesDetailMapper;

    @Override
    public Long createExpensesDetail(ExpensesDetailCreateReqVO createReqVO) {
        // 插入
        ExpensesDetailDO expensesDetail = ExpensesDetailConvert.INSTANCE.convert(createReqVO);
        expensesDetailMapper.insert(expensesDetail);
        // 返回
        return expensesDetail.getId();
    }

    @Override
    public void updateExpensesDetail(ExpensesDetailUpdateReqVO updateReqVO) {
        // 校验存在
        validateExpensesDetailExists(updateReqVO.getId());
        // 更新
        ExpensesDetailDO updateObj = ExpensesDetailConvert.INSTANCE.convert(updateReqVO);
        expensesDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteExpensesDetail(Long id) {
        // 校验存在
        validateExpensesDetailExists(id);
        // 删除
        expensesDetailMapper.deleteById(id);
    }

    private void validateExpensesDetailExists(Long id) {
        if (expensesDetailMapper.selectById(id) == null) {
            throw exception(EXPENSES_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ExpensesDetailDO getExpensesDetail(Long id) {
        return expensesDetailMapper.selectById(id);
    }

    @Override
    public List<ExpensesDetailDO> getExpensesDetailList(Collection<Long> ids) {
        return expensesDetailMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ExpensesDetailDO> getExpensesDetailPage(ExpensesDetailPageReqVO pageReqVO) {
        return expensesDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ExpensesDetailDO> getExpensesDetailList(ExpensesDetailExportReqVO exportReqVO) {
        return expensesDetailMapper.selectList(exportReqVO);
    }

}
