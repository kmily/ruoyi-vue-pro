package cn.iocoder.yudao.module.oa.service.opportunity;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunity.OpportunityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 商机 Service 接口
 *
 * @author 东海
 */
public interface OpportunityService {

    /**
     * 创建商机
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOpportunity(@Valid OpportunityCreateReqVO createReqVO);

    /**
     * 更新商机
     *
     * @param updateReqVO 更新信息
     */
    void updateOpportunity(@Valid OpportunityUpdateReqVO updateReqVO);

    /**
     * 删除商机
     *
     * @param id 编号
     */
    void deleteOpportunity(Long id);

    /**
     * 获得商机
     *
     * @param id 编号
     * @return 商机
     */
    OpportunityDO getOpportunity(Long id);

    /**
     * 获得商机列表
     *
     * @param ids 编号
     * @return 商机列表
     */
    List<OpportunityDO> getOpportunityList(Collection<Long> ids);

    /**
     * 获得商机分页
     *
     * @param pageReqVO 分页查询
     * @return 商机分页
     */
    PageResult<OpportunityDO> getOpportunityPage(OpportunityPageReqVO pageReqVO);

    /**
     * 获得商机列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 商机列表
     */
    List<OpportunityDO> getOpportunityList(OpportunityExportReqVO exportReqVO);

}
