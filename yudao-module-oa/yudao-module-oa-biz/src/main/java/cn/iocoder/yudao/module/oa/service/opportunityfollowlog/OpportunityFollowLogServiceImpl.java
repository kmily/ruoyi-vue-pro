package cn.iocoder.yudao.module.oa.service.opportunityfollowlog;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunityfollowlog.OpportunityFollowLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.opportunityfollowlog.OpportunityFollowLogConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.opportunityfollowlog.OpportunityFollowLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 商机-跟进日志 Service 实现类
 *
 * @author 东海
 */
@Service
@Validated
public class OpportunityFollowLogServiceImpl implements OpportunityFollowLogService {

    @Resource
    private OpportunityFollowLogMapper opportunityFollowLogMapper;

    @Override
    public Long createOpportunityFollowLog(OpportunityFollowLogCreateReqVO createReqVO) {
        // 插入
        OpportunityFollowLogDO opportunityFollowLog = OpportunityFollowLogConvert.INSTANCE.convert(createReqVO);
        opportunityFollowLogMapper.insert(opportunityFollowLog);
        // 返回
        return opportunityFollowLog.getId();
    }

    @Override
    public void updateOpportunityFollowLog(OpportunityFollowLogUpdateReqVO updateReqVO) {
        // 校验存在
        validateOpportunityFollowLogExists(updateReqVO.getId());
        // 更新
        OpportunityFollowLogDO updateObj = OpportunityFollowLogConvert.INSTANCE.convert(updateReqVO);
        opportunityFollowLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteOpportunityFollowLog(Long id) {
        // 校验存在
        validateOpportunityFollowLogExists(id);
        // 删除
        opportunityFollowLogMapper.deleteById(id);
    }

    private void validateOpportunityFollowLogExists(Long id) {
        if (opportunityFollowLogMapper.selectById(id) == null) {
            throw exception(OPPORTUNITY_FOLLOW_LOG_NOT_EXISTS);
        }
    }

    @Override
    public OpportunityFollowLogDO getOpportunityFollowLog(Long id) {
        return opportunityFollowLogMapper.selectById(id);
    }

    @Override
    public List<OpportunityFollowLogDO> getOpportunityFollowLogList(Collection<Long> ids) {
        return opportunityFollowLogMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<OpportunityFollowLogDO> getOpportunityFollowLogPage(OpportunityFollowLogPageReqVO pageReqVO) {
        return opportunityFollowLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OpportunityFollowLogDO> getOpportunityFollowLogList(OpportunityFollowLogExportReqVO exportReqVO) {
        return opportunityFollowLogMapper.selectList(exportReqVO);
    }

}
