package cn.iocoder.yudao.module.oa.service.opportunity;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.opportunity.OpportunityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.oa.convert.opportunity.OpportunityConvert;
import cn.iocoder.yudao.module.oa.dal.mysql.opportunity.OpportunityMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.oa.enums.ErrorCodeConstants.*;

/**
 * 商机 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class OpportunityServiceImpl implements OpportunityService {

    @Resource
    private OpportunityMapper opportunityMapper;

    @Override
    public Long createOpportunity(OpportunityCreateReqVO createReqVO) {
        // 插入
        OpportunityDO opportunity = OpportunityConvert.INSTANCE.convert(createReqVO);
        opportunityMapper.insert(opportunity);
        // 返回
        return opportunity.getId();
    }

    @Override
    public void updateOpportunity(OpportunityUpdateReqVO updateReqVO) {
        // 校验存在
        validateOpportunityExists(updateReqVO.getId());
        // 更新
        OpportunityDO updateObj = OpportunityConvert.INSTANCE.convert(updateReqVO);
        opportunityMapper.updateById(updateObj);
    }

    @Override
    public void deleteOpportunity(Long id) {
        // 校验存在
        validateOpportunityExists(id);
        // 删除
        opportunityMapper.deleteById(id);
    }

    private void validateOpportunityExists(Long id) {
        if (opportunityMapper.selectById(id) == null) {
            throw exception(OPPORTUNITY_NOT_EXISTS);
        }
    }

    @Override
    public OpportunityDO getOpportunity(Long id) {
        return opportunityMapper.selectById(id);
    }

    @Override
    public List<OpportunityDO> getOpportunityList(Collection<Long> ids) {
        return opportunityMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<OpportunityDO> getOpportunityPage(OpportunityPageReqVO pageReqVO) {
        return opportunityMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OpportunityDO> getOpportunityList(OpportunityExportReqVO exportReqVO) {
        return opportunityMapper.selectList(exportReqVO);
    }

}
