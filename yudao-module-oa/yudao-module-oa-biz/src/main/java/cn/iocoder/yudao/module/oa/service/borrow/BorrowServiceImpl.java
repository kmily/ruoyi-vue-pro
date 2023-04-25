package cn.iocoder.yudao.module.oa.service.borrow;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.borrow.BorrowDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.borrow.BorrowConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.borrow.BorrowMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 借支申请 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class BorrowServiceImpl implements BorrowService {

    @Resource
    private BorrowMapper borrowMapper;

    @Override
    public Long createBorrow(BorrowCreateReqVO createReqVO) {
        // 插入
        BorrowDO borrow = BorrowConvert.INSTANCE.convert(createReqVO);
        borrowMapper.insert(borrow);
        // 返回
        return borrow.getId();
    }

    @Override
    public void updateBorrow(BorrowUpdateReqVO updateReqVO) {
        // 校验存在
        validateBorrowExists(updateReqVO.getId());
        // 更新
        BorrowDO updateObj = BorrowConvert.INSTANCE.convert(updateReqVO);
        borrowMapper.updateById(updateObj);
    }

    @Override
    public void deleteBorrow(Long id) {
        // 校验存在
        validateBorrowExists(id);
        // 删除
        borrowMapper.deleteById(id);
    }

    private void validateBorrowExists(Long id) {
        if (borrowMapper.selectById(id) == null) {
            throw exception(BORROW_NOT_EXISTS);
        }
    }

    @Override
    public BorrowDO getBorrow(Long id) {
        return borrowMapper.selectById(id);
    }

    @Override
    public List<BorrowDO> getBorrowList(Collection<Long> ids) {
        return borrowMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BorrowDO> getBorrowPage(BorrowPageReqVO pageReqVO) {
        return borrowMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BorrowDO> getBorrowList(BorrowExportReqVO exportReqVO) {
        return borrowMapper.selectList(exportReqVO);
    }

}
