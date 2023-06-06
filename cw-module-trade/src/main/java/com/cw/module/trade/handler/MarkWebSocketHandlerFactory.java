package com.cw.module.trade.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.binance.client.SubscriptionClient;
import com.binance.client.model.event.SymbolMiniTickerEvent;

import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;

@Component
public class MarkWebSocketHandlerFactory {

    private static MarkWebSocketHandlerFactory singleton;
    
    
    @Getter
    private Map<String, BigDecimal> newPrices = new ConcurrentHashMap<String, BigDecimal>();
    
    public static MarkWebSocketHandlerFactory get() {
        if (singleton == null) {
            synchronized (MarkWebSocketHandlerFactory.class) {
                if (singleton == null) {
                    singleton = SpringUtil.getBean(MarkWebSocketHandlerFactory.class);
                }
            }
        }
        return singleton;
    }
    
    @PostConstruct
    public void init() {
        SubscriptionClient client = SubscriptionClient.create();
        client.subscribeAllMiniTickerEvent(this::refreshPrice, null);
    }
    
    private void refreshPrice(List<SymbolMiniTickerEvent> notifyData) {
        for(SymbolMiniTickerEvent data : notifyData) {
            newPrices.put(data.getSymbol(), data.getClose());
        }
    }
    
}