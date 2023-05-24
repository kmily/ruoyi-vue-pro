package cn.iocoder.yudao.module.oa.dal.mysql.borrow;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowExportReqVO;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.BorrowPageReqVO;
import cn.iocoder.yudao.module.oa.dal.dataobject.borrow.BorrowDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 借支申请 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface BorrowMapper extends BaseMapperX<BorrowDO> {

    default PageResult<BorrowDO> selectPage(BorrowPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BorrowDO>()
                .eqIfPresent(BorrowDO::getBorrowReason, reqVO.getBorrowReason())
                .eqIfPresent(BorrowDO::getBorrowFee, reqVO.getBorrowFee())
                .eqIfPresent(BorrowDO::getRepaymentFee, reqVO.getRepaymentFee())
                .eqIfPresent(BorrowDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BorrowDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(BorrowDO::getRemark, reqVO.getRemark())
                .eqIfPresent(BorrowDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(BorrowDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(BorrowDO::getUpdater, reqVO.getUpdater())
                .orderByDesc(BorrowDO::getId));
    }

    default List<BorrowDO> selectList(BorrowExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BorrowDO>()
                .eqIfPresent(BorrowDO::getBorrowReason, reqVO.getBorrowReason())
                .eqIfPresent(BorrowDO::getBorrowFee, reqVO.getBorrowFee())
                .eqIfPresent(BorrowDO::getRepaymentFee, reqVO.getRepaymentFee())
                .eqIfPresent(BorrowDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BorrowDO::getApprovalStatus, reqVO.getApprovalStatus())
                .eqIfPresent(BorrowDO::getRemark, reqVO.getRemark())
                .eqIfPresent(BorrowDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(BorrowDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(BorrowDO::getUpdater, reqVO.getUpdater())
                .orderByDesc(BorrowDO::getId));
    }

}
