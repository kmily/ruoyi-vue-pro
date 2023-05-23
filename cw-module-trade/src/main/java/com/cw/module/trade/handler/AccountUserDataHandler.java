package com.cw.module.trade.handler;

import com.binance.client.RequestOptions;
import com.binance.client.SubscriptionClient;
import com.binance.client.SubscriptionListener;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.user.UserDataUpdateEvent;

public class AccountUserDataHandler extends AbstractWebSocketHander {

    
    public void subscribeUserData(String apiKey, String apiSecret, 
            SubscriptionListener<UserDataUpdateEvent> callback, Long sysAccountId) {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(apiKey, apiSecret,
                options);

        // Start user data stream
        String listenKey = syncRequestClient.startUserDataStream();
        System.out.println("listenKey: " + listenKey);

        // Keep user data stream
        syncRequestClient.keepUserDataStream(listenKey);

        // Close user data stream
        syncRequestClient.closeUserDataStream(listenKey);

        SubscriptionClient client = SubscriptionClient.create();

        client.subscribeUserDataEvent(listenKey, callback, null, sysAccountId);
    }
}
