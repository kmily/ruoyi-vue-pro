package com.cw.module.trade.handler;

import com.binance.client.RequestOptions;
import com.binance.client.SubscriptionClient;
import com.binance.client.SubscriptionListener;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.user.UserDataUpdateEvent;

import lombok.Getter;

public class AccountUserDataHandler extends AbstractWebSocketHander {

    @Getter
    private SyncRequestClient syncRequestClient;
    
    private String listenKey;
    
    private SubscriptionClient client;
    
    public void subscribeUserData(String apiKey, String apiSecret, 
            SubscriptionListener<UserDataUpdateEvent> callback, Long sysAccountId) {
        RequestOptions options = new RequestOptions();
        syncRequestClient = SyncRequestClient.create(apiKey, apiSecret,
                options);

        // Start user data stream
        listenKey = syncRequestClient.startUserDataStream();
        System.out.println("listenKey: " + listenKey);

        // Close user data stream
        // syncRequestClient.closeUserDataStream(listenKey);

        client = SubscriptionClient.create();

        client.subscribeUserDataEvent(listenKey, callback, null, sysAccountId);
    }
    
    public void keepListenKey() {
        // Keep user data stream
        syncRequestClient.keepUserDataStream(listenKey);
    }
    
    public void stopSubscribe() {
        client.unsubscribeAll();
    }
}
