package cn.iocoder.yudao.module.system.service.codingrules;

import cn.iocoder.yudao.module.system.dal.dataobject.codingrulesdetails.CodingRulesDetailsDO;
import cn.iocoder.yudao.module.system.dal.mysql.codingrulesdetails.CodingRulesDetailsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import cn.iocoder.yudao.module.system.controller.admin.codingrules.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.codingrules.CodingRulesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.dal.mysql.codingrules.CodingRulesMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 编码规则 Service 实现类
 *
 * @author panjiabao
 */
@Service
@Validated
public class CodingRulesServiceImpl implements CodingRulesService {

    @Resource
    private CodingRulesMapper codingRulesMapper;

    @Resource
    private CodingRulesDetailsMapper codingRulesDetailsMapper;


    @Override
    public String createCodingRules(CodingRulesSaveReqVO createReqVO) {
        // 插入
        CodingRulesDO codingRules = BeanUtils.toBean(createReqVO, CodingRulesDO.class);
        codingRulesMapper.insert(codingRules);
        // 返回
        return codingRules.getId();
    }

    @Override
    public void updateCodingRules(CodingRulesSaveReqVO updateReqVO) {
        // 校验存在
        validateCodingRulesExists(updateReqVO.getId());
        // 更新
        CodingRulesDO updateObj = BeanUtils.toBean(updateReqVO, CodingRulesDO.class);
        codingRulesMapper.updateById(updateObj);
    }

    @Override
    public void deleteCodingRules(String id) {
        // 校验存在
        validateCodingRulesExists(id);
        // 删除
        codingRulesMapper.deleteById(id);
    }

    private void validateCodingRulesExists(String id) {
        if (codingRulesMapper.selectById(id) == null) {
            throw exception(CODING_RULES_NOT_EXISTS);
        }
    }

    private void validateCodingRulesDetailsExists(String id) {
        if (codingRulesDetailsMapper.selectById(id) == null) {
            throw exception(CODING_RULES_DETAILS_NOT_EXISTS);
        }
    }

    @Override
    public CodingRulesDO getCodingRules(String id) {
        return codingRulesMapper.selectById(id);
    }

    @Override
    public PageResult<CodingRulesDO> getCodingRulesPage(CodingRulesPageReqVO pageReqVO) {
        return codingRulesMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CodingRulesDetailsDO> getDetails(String id) {
        List<CodingRulesDetailsDO> codingRulesDetails = codingRulesDetailsMapper.selectList(new LambdaQueryWrapper<CodingRulesDetailsDO>()
                .eq(CodingRulesDetailsDO::getRuleId, id).orderByAsc(CodingRulesDetailsDO::getOrderNum));
        return codingRulesDetails;
    }

    @Override
    public String insertDetails(CodingRulesDetailsSaveReqVO codingRulesDetailsSaveReqVO) {
        // 插入
        CodingRulesDetailsDO codingRulesDetailsDO = BeanUtils.toBean(codingRulesDetailsSaveReqVO, CodingRulesDetailsDO.class);
        codingRulesDetailsMapper.insert(codingRulesDetailsDO);
        // 返回
        return codingRulesDetailsDO.getId();
    }

    @Override
    public void updateDetails(CodingRulesDetailsSaveReqVO updateReqVO) {
        // 校验存在
        validateCodingRulesDetailsExists(updateReqVO.getId());
        // 更新
        CodingRulesDetailsDO updateObj = BeanUtils.toBean(updateReqVO, CodingRulesDetailsDO.class);
        codingRulesDetailsMapper.updateById(updateObj);
    }

    @Override
    public void deleteDetails(String id) {
        // 校验存在
        validateCodingRulesDetailsExists(id);
        // 删除
        codingRulesDetailsMapper.deleteById(id);
    }


}
