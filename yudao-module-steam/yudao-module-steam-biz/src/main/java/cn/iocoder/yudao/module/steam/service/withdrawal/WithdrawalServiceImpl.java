package cn.iocoder.yudao.module.steam.service.withdrawal;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.withdrawal.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.withdrawal.WithdrawalDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.withdrawal.WithdrawalMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 提现 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WithdrawalServiceImpl implements WithdrawalService {

    @Resource
    private WithdrawalMapper withdrawalMapper;

    @Override
    public Long createWithdrawal(WithdrawalSaveReqVO createReqVO) {
        // 插入
        WithdrawalDO withdrawal = BeanUtils.toBean(createReqVO, WithdrawalDO.class);
        withdrawalMapper.insert(withdrawal);
        // 返回
        return withdrawal.getId();
    }

    @Override
    public void updateWithdrawal(WithdrawalSaveReqVO updateReqVO) {
        // 校验存在
        validateWithdrawalExists(updateReqVO.getId());
        // 更新
        WithdrawalDO updateObj = BeanUtils.toBean(updateReqVO, WithdrawalDO.class);
        withdrawalMapper.updateById(updateObj);
    }

    @Override
    public void deleteWithdrawal(Long id) {
        // 校验存在
        validateWithdrawalExists(id);
        // 删除
        withdrawalMapper.deleteById(id);
    }

    private void validateWithdrawalExists(Long id) {
        if (withdrawalMapper.selectById(id) == null) {
            throw exception(WITHDRAWAL_NOT_EXISTS);
        }
    }

    @Override
    public WithdrawalDO getWithdrawal(Long id) {
        return withdrawalMapper.selectById(id);
    }

    @Override
    public PageResult<WithdrawalDO> getWithdrawalPage(WithdrawalPageReqVO pageReqVO) {
        return withdrawalMapper.selectPage(pageReqVO);
    }

}