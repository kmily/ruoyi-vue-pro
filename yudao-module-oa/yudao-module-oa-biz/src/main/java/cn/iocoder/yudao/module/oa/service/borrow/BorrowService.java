package cn.iocoder.yudao.module.oa.service.borrow;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.borrow.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.borrow.BorrowDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 借支申请 Service 接口
 *
 * @author 东海
 */
public interface BorrowService {

    /**
     * 创建借支申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBorrow(@Valid BorrowCreateReqVO createReqVO);

    /**
     * 更新借支申请
     *
     * @param updateReqVO 更新信息
     */
    void updateBorrow(@Valid BorrowUpdateReqVO updateReqVO);

    /**
     * 删除借支申请
     *
     * @param id 编号
     */
    void deleteBorrow(Long id);

    /**
     * 获得借支申请
     *
     * @param id 编号
     * @return 借支申请
     */
    BorrowDO getBorrow(Long id);

    /**
     * 获得借支申请列表
     *
     * @param ids 编号
     * @return 借支申请列表
     */
    List<BorrowDO> getBorrowList(Collection<Long> ids);

    /**
     * 获得借支申请分页
     *
     * @param pageReqVO 分页查询
     * @return 借支申请分页
     */
    PageResult<BorrowDO> getBorrowPage(BorrowPageReqVO pageReqVO);

    /**
     * 获得借支申请列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 借支申请列表
     */
    List<BorrowDO> getBorrowList(BorrowExportReqVO exportReqVO);

}
