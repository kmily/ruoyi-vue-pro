package cn.iocoder.yudao.module.steam.service.offlinerechange;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.offlinerechange.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.offlinerechange.OfflineRechangeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.offlinerechange.OfflineRechangeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 线下人工充值 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class OfflineRechangeServiceImpl implements OfflineRechangeService {

    @Resource
    private OfflineRechangeMapper offlineRechangeMapper;
    @Resource
    private PayWalletService payWalletService;
    @Resource
    private MemberUserService memberUserService;

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public Long createOfflineRechange(OfflineRechangeSaveReqVO createReqVO) {
        // 插入
        OfflineRechangeDO offlineRechange = BeanUtils.toBean(createReqVO, OfflineRechangeDO.class);
        offlineRechange.setUserType(UserTypeEnum.MEMBER.getValue());
        MemberUserDO userByMobile = memberUserService.getUserByMobile(offlineRechange.getMobile());
        if(!Objects.nonNull(userByMobile)){
            throw new ServiceException(-1,"用户不存在");
        }
        offlineRechange.setUserId(userByMobile.getId());
        offlineRechange.setUserType(UserTypeEnum.MEMBER.getValue());
        offlineRechangeMapper.insert(offlineRechange);

        Optional<PayWalletBizTypeEnum> first = Arrays.stream(PayWalletBizTypeEnum.values()).filter(i -> i.getType().equals(offlineRechange.getRechangeType())).findFirst();
        PayWalletBizTypeEnum rechangeEnum = first.orElseThrow(()->{return new ServiceException(-1, "充值类型不存在");});

        PayWalletDO orCreateWallet = payWalletService.getOrCreateWallet(userByMobile.getId(), UserTypeEnum.MEMBER.getValue());
        PayWalletTransactionDO payWalletTransactionDO1 = payWalletService.addWalletBalance(orCreateWallet.getId(), String.valueOf(offlineRechange.getId()),
                rechangeEnum, offlineRechange.getAmount());
        offlineRechange.setWalletInfo(JacksonUtils.writeValueAsString(payWalletTransactionDO1));
        offlineRechangeMapper.updateById(offlineRechange);
        // 返回
        return offlineRechange.getId();
    }

    @Override
    public void updateOfflineRechange(OfflineRechangeSaveReqVO updateReqVO) {
        // 校验存在
        throw new ServiceException(-1,"不支持的操作");
//        validateOfflineRechangeExists(updateReqVO.getId());
//        // 更新
//        OfflineRechangeDO updateObj = BeanUtils.toBean(updateReqVO, OfflineRechangeDO.class);
//        offlineRechangeMapper.updateById(updateObj);
    }

    @Override
    public void deleteOfflineRechange(Long id) {
        throw new ServiceException(-1,"不支持的操作");
//        // 校验存在
//        validateOfflineRechangeExists(id);
//        // 删除
//        offlineRechangeMapper.deleteById(id);
    }

    private void validateOfflineRechangeExists(Long id) {
        if (offlineRechangeMapper.selectById(id) == null) {
            throw exception(OFFLINE_RECHANGE_NOT_EXISTS);
        }
    }

    @Override
    public OfflineRechangeDO getOfflineRechange(Long id) {
        return offlineRechangeMapper.selectById(id);
    }

    @Override
    public PageResult<OfflineRechangeDO> getOfflineRechangePage(OfflineRechangePageReqVO pageReqVO) {
        return offlineRechangeMapper.selectPage(pageReqVO);
    }

}