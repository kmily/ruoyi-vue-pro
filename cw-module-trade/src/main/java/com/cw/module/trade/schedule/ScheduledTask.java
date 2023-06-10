package com.cw.module.trade.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cw.module.trade.dal.dataobject.account.AccountDO;
import com.cw.module.trade.handler.WebSocketHandlerFactory;
import com.cw.module.trade.service.account.AccountService;
import com.cw.module.trade.service.position.PositionService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * * 定时任务类
 * @date 2022年10月13日
 * @author wuqiaoxin
 */
@Slf4j
@Component
public class ScheduledTask {
    
    @Autowired
    private AccountService accountService;
    @Autowired
    private PositionService positionServiceImpl;

    /**
     * * 每5分钟同步一下账户余额
     * @date 2023年5月24日
     * @author wuqiaoxin
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void syncZeroBalance() {
        WebSocketHandlerFactory.get().syncZeroBalance();
    }
    
    /**
     * * 每分钟同步一下账户持仓信息
     * @date 2023年5月24日
     * @author wuqiaoxin
     */
    @Scheduled(fixedDelay = 60*1000)
    private void syncPosition() {
        positionServiceImpl.syncPosition();
    }
    
    /**
     * * 每分钟检测账户的亏损值，是否达到设定值
     * @date 2023年5月24日
     * @author wuqiaoxin
     */
    @Scheduled(fixedDelay = 60*1000)
    private void checkStopFollow() {
        try {
            WebSocketHandlerFactory.get().checkStopFollow();
        } catch (Exception e) {
            log.error("[亏损检测]:出现异常:{}", e);
        }
        try {
            WebSocketHandlerFactory.get().checkABAccountPosition();
        } catch (Exception e) {
            log.error("[持仓检测]:出现异常:{}", e);
        }
    }
    
    /**
     * * 每50分钟重新刷新长连接监听key的有效期
     * @date 2023年5月24日
     * @author wuqiaoxin
     */
    @Scheduled(fixedDelay = 50*60*1000)
    private void keepListenKey() {
        WebSocketHandlerFactory.get().keepListenKey();
    }
    
    /**
     * * 每60分钟同步一下交易对的杠杆配置
     * @date 2023年5月24日
     * @author wuqiaoxin
     */
    @Scheduled(fixedDelay = 60*60*1000, initialDelay = 3*60*1000)
    private void syncSymbolLeverage() {
        List<AccountDO> monitorAccounts = accountService.listMonitorAccount();
        if(CollectionUtil.isEmpty(monitorAccounts)) {
            log.info("当前未有需要监听的账号");
            return ;
        }
        for(AccountDO account : monitorAccounts) {
            List<AccountDO> listFollowAccount = accountService.listFollowAccount(account.getId());
            if(CollectionUtils.isNotEmpty(listFollowAccount)) {
                for(AccountDO followaccount : listFollowAccount) {
                    WebSocketHandlerFactory.get().syncAToBAccount(account, followaccount);
                }
            }
        }
    }
}
