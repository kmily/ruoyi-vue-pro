package cn.iocoder.yudao.module.oa.service.opportunityfollowlog;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunityfollowlog.OpportunityFollowLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 商机-跟进日志 Service 接口
 *
 * @author 东海
 */
public interface OpportunityFollowLogService {

    /**
     * 创建商机-跟进日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOpportunityFollowLog(@Valid OpportunityFollowLogCreateReqVO createReqVO);

    /**
     * 更新商机-跟进日志
     *
     * @param updateReqVO 更新信息
     */
    void updateOpportunityFollowLog(@Valid OpportunityFollowLogUpdateReqVO updateReqVO);

    /**
     * 删除商机-跟进日志
     *
     * @param id 编号
     */
    void deleteOpportunityFollowLog(Long id);

    /**
     * 获得商机-跟进日志
     *
     * @param id 编号
     * @return 商机-跟进日志
     */
    OpportunityFollowLogDO getOpportunityFollowLog(Long id);

    /**
     * 获得商机-跟进日志列表
     *
     * @param ids 编号
     * @return 商机-跟进日志列表
     */
    List<OpportunityFollowLogDO> getOpportunityFollowLogList(Collection<Long> ids);

    /**
     * 获得商机-跟进日志分页
     *
     * @param pageReqVO 分页查询
     * @return 商机-跟进日志分页
     */
    PageResult<OpportunityFollowLogDO> getOpportunityFollowLogPage(OpportunityFollowLogPageReqVO pageReqVO);

    /**
     * 获得商机-跟进日志列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 商机-跟进日志列表
     */
    List<OpportunityFollowLogDO> getOpportunityFollowLogList(OpportunityFollowLogExportReqVO exportReqVO);

}
