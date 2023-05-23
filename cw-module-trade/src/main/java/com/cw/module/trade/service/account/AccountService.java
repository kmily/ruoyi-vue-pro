package com.cw.module.trade.service.account;

import java.util.*;
import javax.validation.*;

import com.cw.module.trade.controller.admin.account.vo.*;
import com.cw.module.trade.dal.dataobject.account.AccountDO;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 交易账号 Service 接口
 *
 * @author chengjiale
 */
public interface AccountService {

    /**
     * 创建交易账号
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAccount(@Valid AccountCreateReqVO createReqVO);

    /**
     * 更新交易账号
     *
     * @param updateReqVO 更新信息
     */
    void updateAccount(@Valid AccountUpdateReqVO updateReqVO);

    /**
     * 删除交易账号
     *
     * @param id 编号
     */
    void deleteAccount(Long id);

    /**
     * 获得交易账号
     *
     * @param id 编号
     * @return 交易账号
     */
    AccountDO getAccount(Long id);

    /**
     * 获得交易账号列表
     *
     * @param ids 编号
     * @return 交易账号列表
     */
    List<AccountDO> getAccountList(Collection<Long> ids);

    /**
     * 获得交易账号分页
     *
     * @param pageReqVO 分页查询
     * @return 交易账号分页
     */
    PageResult<AccountDO> getAccountPage(AccountPageReqVO pageReqVO);

    /**
     * 获得交易账号列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 交易账号列表
     */
    List<AccountDO> getAccountList(AccountExportReqVO exportReqVO);

    /**
     * * 同步账号余额信息，并返回余额信息
     * @date 2023年5月11日
     * @author wuqiaoxin
     */
    String syncBalance(Long accountId);
    
    /**
     * * 需要监控的账号
     * @date 2023年5月17日
     * @author wuqiaoxin
     */
    List<AccountDO> listMonitorAccount();

}
