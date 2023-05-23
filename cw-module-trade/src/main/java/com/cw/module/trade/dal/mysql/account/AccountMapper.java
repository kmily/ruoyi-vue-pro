package com.cw.module.trade.dal.mysql.account;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cw.module.trade.controller.admin.account.vo.*;
import com.cw.module.trade.dal.dataobject.account.AccountDO;

/**
 * 交易账号 Mapper
 *
 * @author chengjiale
 */
@Mapper
public interface AccountMapper extends BaseMapperX<AccountDO> {

    default PageResult<AccountDO> selectPage(AccountPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AccountDO>()
                .likeIfPresent(AccountDO::getName, reqVO.getName())
                .eqIfPresent(AccountDO::getAppKey, reqVO.getAppKey())
                .eqIfPresent(AccountDO::getAppSecret, reqVO.getAppSecret())
                .eqIfPresent(AccountDO::getBalance, reqVO.getBalance())
                .betweenIfPresent(AccountDO::getLastBalanceQueryTime, reqVO.getLastBalanceQueryTime())
                .betweenIfPresent(AccountDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AccountDO::getRelateUser, reqVO.getRelateUser())
                .orderByDesc(AccountDO::getId));
    }

    default List<AccountDO> selectList(AccountExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AccountDO>()
                .likeIfPresent(AccountDO::getName, reqVO.getName())
                .eqIfPresent(AccountDO::getAppKey, reqVO.getAppKey())
                .eqIfPresent(AccountDO::getAppSecret, reqVO.getAppSecret())
                .eqIfPresent(AccountDO::getBalance, reqVO.getBalance())
                .betweenIfPresent(AccountDO::getLastBalanceQueryTime, reqVO.getLastBalanceQueryTime())
                .betweenIfPresent(AccountDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AccountDO::getRelateUser, reqVO.getRelateUser())
                .orderByDesc(AccountDO::getId));
    }

    
    @Select("select * from trade_account where deleted = 0  ")
    //        + " id in (select distinct follow_account from trade_account where deleted = 0)")
    List<AccountDO> listMonitorAccount();
}
