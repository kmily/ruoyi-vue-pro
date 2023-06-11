package com.cw.module.trade.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Component;

import com.binance.client.SubscriptionClient;
import com.binance.client.model.event.SymbolTickerEvent;
import com.google.common.collect.Maps;
import com.tb.utils.DateUtils;
import com.tb.utils.json.JsonUtil;

import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;

@Component
public class MarkWebSocketHandlerFactory {

    private static MarkWebSocketHandlerFactory singleton;
    
    
    private static Map<String, SymbolTickerEvent> spotsSymbols = Maps.newConcurrentMap();
    private static Map<String, SymbolTickerEvent> symbols = Maps.newConcurrentMap();
    private static Map<String, Long> sendInterval = Maps.newConcurrentMap();
    
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
        client.subscribeAllTickerEvent(((event) -> {
            symbols.putAll(event);
            }), null, false);
        client.subscribeAllTickerEvent(this::compareSpots, null, true);
    }
    
    private void compareSpots(Map<String, SymbolTickerEvent> symbolchange) {
        spotsSymbols.putAll(symbolchange);
        for(SymbolTickerEvent symbolTicker : symbolchange.values()) {
            SymbolTickerEvent symbol = symbols.get(symbolTicker.getSymbol());
            if(symbol != null) {
                if(symbolTicker.getEventTime() - symbol.getEventTime() < 2000) {
                    BigDecimal diff = symbol.getLastPrice().subtract(symbolTicker.getLastPrice()).abs();
                    BigDecimal min = symbol.getLastPrice().compareTo(symbolTicker.getLastPrice()) != 1 
                            ? symbol.getLastPrice() : symbolTicker.getLastPrice();
                    if(diff.divide(min, 2, RoundingMode.HALF_DOWN).compareTo(new BigDecimal(0.05)) > 0) {
                        sendDingDingMessage(symbol.getSymbol(), DateUtils.formatForyyyyMMddHHmmss(new Date(symbolTicker.getEventTime())),
                                symbol.getLastPrice().toString(), symbolTicker.getLastPrice().toString(),
                                diff.toString(), diff.divide(min, 2, RoundingMode.HALF_DOWN).toString());
                    }
                }
            }
        }
    }
    
    private static String dingdingSercert = "SEC684c898fbd56e34cfe35fdd1dd77e7b07d98098e6b359a9fc5971fd75fd88bae";
    private static String webhook= "https://oapi.dingtalk.com/robot/send?access_token=8f66015e323c718fbbd5f745ad3d1514b646a4d12787c69d8f37576776e96c36";
    public synchronized void sendDingDingMessage(String symbol, String eventTime, String price,
            String spotPrice, String diff, String rati) {
        Long timestamp = System.currentTimeMillis();
        if(sendInterval.get(symbol) != null 
                && timestamp - sendInterval.get(symbol) < TimeUnit.MINUTES.toMillis(5)) {
            return ;
        } 
        sendInterval.put(symbol, timestamp);
        String uri =  String.format("%s&timestamp=%s&sign=%s", webhook, timestamp, getSign(timestamp));
        try {
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
            PostMethod postMethod = new PostMethod(uri);
            postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
            // 调用第三方
            postMethod.setRequestHeader("Content-Type", "application/json");
            Map<String, Object> messageJson = new HashMap<>();
            messageJson.put("msgtype", "markdown");

            Map<String, Object> content = new HashMap<>();
            content.put("title", "行情预警");
            StringBuilder text = new StringBuilder("# 行情预警 \n");
            text.append(String.format("- 交易对  %s  \n", symbol));
            text.append(String.format("- 交易时间  %s  \n", eventTime));
            text.append(String.format("- 合约价格  %s  \n", price));
            text.append(String.format("- 现货价格  %s  \n", spotPrice));
            text.append(String.format("- 价格差  %s  \n", diff));
            text.append(String.format("- 价格占比  %s  \n", rati));
            content.put("text", text.toString());
            messageJson.put("markdown", content);
            postMethod.setRequestEntity(new StringRequestEntity(JsonUtil.object2String(messageJson), "application/json", "utf-8"));
            
            httpClient.executeMethod(postMethod);
            String res = postMethod.getResponseBodyAsString();
            postMethod.releaseConnection();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String getSign(Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + dingdingSercert;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(dingdingSercert.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}