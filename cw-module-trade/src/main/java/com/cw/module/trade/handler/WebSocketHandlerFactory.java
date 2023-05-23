package com.cw.module.trade.handler;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.binance.client.model.user.UserDataUpdateEvent;
import com.cw.module.trade.dal.dataobject.account.AccountDO;
import com.cw.module.trade.service.account.AccountService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketHandlerFactory {

    @Autowired
    private AccountService accountServiceImpl;
    
    public static WebSocketHandlerFactory singleton;
    
    public static WebSocketHandlerFactory get() {
        if (singleton == null) {
            synchronized (WebSocketHandlerFactory.class) {
                if (singleton == null) {
                    singleton = SpringUtil.getBean(WebSocketHandlerFactory.class);
                }
            }
        }
        return singleton;
    }
    
    @PostConstruct
    public void init() {
        List<AccountDO> monitorAccounts = accountServiceImpl.listMonitorAccount();
        if(CollectionUtil.isEmpty(monitorAccounts)) {
            log.info("当前未有需要监听的账号");
            return ;
        }
        for(AccountDO account : monitorAccounts) {
            AccountUserDataHandler userDataHandler = new AccountUserDataHandler();
            userDataHandler.subscribeUserData(account.getAppKey(), account.getAppSecret(), 
                    this::followOrder, account.getId());
        }
    }
    
    
    
    public void followOrder(UserDataUpdateEvent data) {
        
    }
    
}
