package com.cw.module.trade.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.enums.WorkingType;
import com.binance.client.model.market.ExchangeInfoEntry;
import com.binance.client.model.market.ExchangeInformation;
import com.binance.client.model.trade.AccountInformation;
import com.binance.client.model.trade.Order;
import com.binance.client.model.user.OrderUpdate;
import com.binance.client.model.user.UserDataUpdateEvent;
import com.cw.module.trade.controller.admin.followrecord.vo.FollowRecordCreateReqVO;
import com.cw.module.trade.controller.admin.notifymsg.vo.NotifyMsgCreateReqVO;
import com.cw.module.trade.dal.dataobject.account.AccountDO;
import com.cw.module.trade.dal.dataobject.followrecord.FollowRecordDO;
import com.cw.module.trade.dal.dataobject.position.PositionDO;
import com.cw.module.trade.service.account.AccountService;
import com.cw.module.trade.service.followrecord.FollowRecordService;
import com.cw.module.trade.service.notifymsg.NotifyMsgService;
import com.cw.module.trade.service.position.PositionService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.tb.utils.NumberUtils;
import com.tb.utils.json.JsonUtil;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketHandlerFactory {

    @Autowired
    private AccountService accountServiceImpl;
    
    @Autowired 
    FollowRecordService  followRecordServiceImpl;
    
    @Autowired
    NotifyMsgService notifyMsgServiceImpl;
    
    public static WebSocketHandlerFactory singleton;
    
    @Autowired
    public PositionService positionServiceImpl;
    
    // 先默认每个账户针对同一个交易对的限制是一致的
    private ExchangeInformation exchangeInformation;
    
    private Map<Long, AccountUserDataHandler> handlers = Maps.newHashMap();
    
    @Getter
    private Map<Long, SyncRequestClient> clients = Maps.newHashMap();
    
    @Getter
    private Map<Long, BigDecimal> accountDayInitBalance = Maps.newHashMap();
    
    @Getter
    private List<Long> stopAccounts = Lists.newArrayList();
    
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
            addHandler(account);

            List<AccountDO> listFollowAccount = accountServiceImpl.listFollowAccount(account.getId());
            if(CollectionUtils.isNotEmpty(listFollowAccount)) {
                for(AccountDO followaccount : listFollowAccount) {
                    syncAToBAccount(account, followaccount);
                }
            }
        }
        getTradeRuler(monitorAccounts.get(0));
    }

    public void addHandler(AccountDO account) {
        AccountUserDataHandler userDataHandler = new AccountUserDataHandler();
        userDataHandler.subscribeUserData(account.getAppKey(), account.getAppSecret(), 
                this::followOrder, account.getId());
        clients.put(account.getId(), userDataHandler.getSyncRequestClient());
        handlers.put(account.getId(), userDataHandler);
    }
    
    
    
    public void followOrder(UserDataUpdateEvent data) {
        // 记录通知记录
        NotifyMsgCreateReqVO notifyCreateReqVO = new NotifyMsgCreateReqVO();
        Long notifyAccountId = data.getSysAccountId();
        notifyCreateReqVO.setAccountId(notifyAccountId);
        notifyCreateReqVO.setAcceptTime(new Date());
        notifyCreateReqVO.setAcceptInfo(JsonUtil.object2String(data));
        Long notifyId = notifyMsgServiceImpl.createNotifyMsg(notifyCreateReqVO);
        
        // 判断是否需要跟随下单 TODO 先简单判断一下订单是否为空，等之后拿到更多数据后再进行更多的判断
        OrderUpdate order = data.getOrderUpdate();
        List<AccountDO> listFollowAccount = accountServiceImpl.listFollowAccount(notifyAccountId);
        if("ORDER_TRADE_UPDATE".equals(data.getEventType())) {
            
            AccountDO notifyAccount = accountServiceImpl.getAccount(notifyAccountId);
            String executionType = order.getExecutionType();
            String symbol = order.getSymbol();
            ExchangeInfoEntry symbolRule = this.exchangeInformation.getSymbols().stream().filter(
                    item -> symbol.equals(item.getSymbol())).findFirst().orElse(null);
            if("NEW".equals(executionType)) {
                if(CollectionUtil.isEmpty(listFollowAccount)) {
                    log.info("当前未有需要跟随的账号,通知数据为:{}", data);
                    return ;
                }
                // 新订单
                for(AccountDO account : listFollowAccount) {
                    // 下单则需要保存跟随记录
                    FollowRecordCreateReqVO reqVo = new FollowRecordCreateReqVO();
                    reqVo.setAccountNotifyId(notifyId);
                    reqVo.setFollowAccount(notifyAccountId);
                    reqVo.setOperateAccount(account.getId());
                    reqVo.setOperateTime(new Date());
                    reqVo.setFollowThridOrderId(order.getOrderId());
                    StringBuilder desc = new StringBuilder();
                    Boolean success = false;
                    try {
                        // 调用第三方接口，进行下单操作。 FIXME 还未处理订单修改的逻辑，初步判定很复杂，先实现新订单的买入、卖出
                        // 获取当前持仓，看是否有持仓，有持仓以持仓锚定，如果没有持仓，则以总资产锚定。
                        PositionDO lastestPosition = 
                                positionServiceImpl.selectLastestPosition(notifyAccount.getId(), order.getSymbol());
                        PositionDO followLastestPosition = 
                                positionServiceImpl.selectLastestPosition(notifyAccount.getId(), order.getSymbol());
                        if(lastestPosition.getQuantity().compareTo(new BigDecimal(0)) != 0
                                && followLastestPosition.getQuantity().compareTo(new BigDecimal(0)) == 0) {
                            desc.append("主账户有持仓，跟随账户未有持仓，不进行跟单操作。");
                        } else {
                            BigDecimal followOrderPrice = new BigDecimal(0);
                            // 计算金额
                            if(OrderSide.BUY.toString().equals(order.getSide())) {
                                followOrderPrice = order.getPrice()
                                        .subtract(symbolRule.getFiltersFormat().get("PRICE_FILTER_tickSize"));
                            } else if (OrderSide.SELL.toString().equals(order.getSide())) {
                                followOrderPrice = order.getPrice()
                                        .add(symbolRule.getFiltersFormat().get("PRICE_FILTER_tickSize"));
                            }
                            // 计算数量
                            BigDecimal followOrderQty = new BigDecimal(0);
                            if(lastestPosition.getQuantity().compareTo(new BigDecimal(0)) == 0) {
                                BigDecimal positionProp  = order.getPrice()
                                        .multiply(order.getOrigQty()).divide(notifyAccount.formatTypeBalance("USDT"));
                                int scale = symbolRule.getFiltersFormat().get("LOT_SIZE_stepSize").scale();
                                BigDecimal accountBalance = account.formatTypeBalance("USDT");
                                BigDecimal followAmount = positionProp.multiply(accountBalance);
                                followOrderQty = followAmount.divide(followOrderPrice).setScale(scale, RoundingMode.DOWN);
                            } else {
                                followOrderQty = order.getOrigQty().
                                        divide(lastestPosition.getQuantity().abs())
                                        .multiply(followLastestPosition.getQuantity().abs());
                            }
                            postOrder(order, account, reqVo, desc, followOrderPrice,
                                    followOrderQty);
                        }
                    } catch (Exception e) {
                        log.error("跟随下单出现错误:{}", e);
                        desc.append("程序发生错误,请联系开发人员");
                    }
                    reqVo.setOperateSuccess(success);
                    reqVo.setOperateDesc(desc.toString());
                    
                    followRecordServiceImpl.createFollowRecord(reqVo);
                }
            } else if ("AMENDMENT".equals(executionType)) {
                // 订单修改 TODO
            } else if ("CANCELED".equals(executionType)) {
                // 订单取消 TODO
                orderCancel(notifyAccountId, notifyId, order);
            } else if("CALCULATED".equals(executionType)) {
                // 订单ADL或爆仓 TODO
            } else if("EXPIRED".equals(executionType)) {
                // 订单失效 TODO
            } else if("TRADE".equals(executionType)) {
                // 订单交易 同步仓位信息，为二次交易提供信息 系统每分钟同步持仓信息，这里暂时不同步
                // positionServiceImpl.syncAccountPositionById(notifyAccountId);
            }
        } else if("ACCOUNT_CONFIG_UPDATE".equals(data.getEventType())) {
            if(Strings.isNotBlank(data.getSymbol())) {
                changeInitialLeverage(listFollowAccount, data.getSymbol(), data.getLever());
            }
        }
        
        
    }

    private void orderCancel(Long notifyAccountId, Long notifyId, OrderUpdate order) {
        // 根据订单id查询出来跟随的记录
        List<FollowRecordDO> listFollowRecord = followRecordServiceImpl.listFollowRecord(order.getOrderId());
        for(FollowRecordDO record : listFollowRecord) {
            AccountDO account = accountServiceImpl.getAccount(record.getOperateAccount());
            
            FollowRecordCreateReqVO reqVo = new FollowRecordCreateReqVO();
            reqVo.setAccountNotifyId(notifyId);
            reqVo.setFollowAccount(notifyAccountId);
            reqVo.setOperateAccount(account.getId());
            reqVo.setOperateTime(new Date());
            reqVo.setFollowThridOrderId(order.getOrderId());
            StringBuilder desc = new StringBuilder();
            
            // 调用第三方
            JsonObject params = new JsonObject();
            params.addProperty("symbol", order.getSymbol());
            params.addProperty("orderId", record.getThirdOrderId());
            reqVo.setOperateInfo(JsonUtil.object2String(params));
            
            Order cancelOrder = clients.get(record.getOperateAccount()).cancelOrder(
                    order.getSymbol(), record.getThirdOrderId(), null);
            
            reqVo.setThirdOrderId(cancelOrder.getOrderId());
            reqVo.setOperateResult(JsonUtil.object2String(cancelOrder));

            reqVo.setOperateSuccess(true);
            reqVo.setOperateDesc(desc.toString());
            
            followRecordServiceImpl.createFollowRecord(reqVo);
        }
    }

    private Boolean postOrder(OrderUpdate order, AccountDO account, FollowRecordCreateReqVO reqVo,
            StringBuilder desc, BigDecimal followOrderPrice, BigDecimal followOrderQty) {
        JsonObject params = new JsonObject();
        params.addProperty("symbol", order.getSymbol());
        params.addProperty("side", order.getSide());
        params.addProperty("positionSide", order.getPositionSide());
        params.addProperty("timeInForce", order.getTimeInForce());
        params.addProperty("quantity", followOrderQty.toString());
        params.addProperty("price", followOrderPrice.toString());
        params.addProperty("reduceOnly", order.getIsReduceOnly());
        params.addProperty("stopPrice", order.getStopPrice().toString());
        params.addProperty("workingType", order.getWorkingType());
        params.addProperty("newOrderRespType", NewOrderRespType.RESULT.toString());
        Order orderResp = clients.get(account.getId()).postOrder(
                order.getSymbol(),  //symbol    交易对
                OrderSide.valueOf(order.getSide()), //side  买卖方向
                PositionSide.format(order.getPositionSide()), //positionSide  持仓方向，单向持仓模式下非必填，默认且仅可填BOTH;在双向持仓模式下必填,且仅可选择 LONG 或 SHORT
                OrderType.lookup(order.getType()),    // orderType 订单类型 LIMIT, MARKET, STOP, TAKE_PROFIT, STOP_MARKET, TAKE_PROFIT_MARKET, TRAILING_STOP_MARKET
                TimeInForce.valueOf(order.getTimeInForce()) ,    // timeInForce  有效方法
                followOrderQty.toString(),    // quantity     下单数量,使用closePosition不支持此参数。
                followOrderPrice.toString(), // price    委托价格
                order.getIsReduceOnly().toString(),   // reduceOnly true, false; 非双开模式下默认false；双开模式下不接受此参数； 使用closePosition不支持此参数。
                null,   // newClientOrderId 用户自定义的订单号，不可以重复出现在挂单中。如空缺系统会自动赋值。必须满足正则规则 ^[\.A-Z\:/a-z0-9_-]{1,36}$
                order.getStopPrice().toString(),   // stopPrice 触发价, 仅 STOP, STOP_MARKET, TAKE_PROFIT, TAKE_PROFIT_MARKET 需要此参数
                WorkingType.valueOf(order.getWorkingType()),   // workingType stopPrice 触发类型: MARK_PRICE(标记价格), CONTRACT_PRICE(合约最新价). 默认 CONTRACT_PRICE
                NewOrderRespType.RESULT); //newOrderRespType "ACK", "RESULT", 默认 "ACK"
        reqVo.setOperateResult(JsonUtil.object2String(orderResp));
        reqVo.setThirdOrderId(orderResp.getOrderId());
        desc.append("成功请求第三方。");
        return true;
    }
    
    private void getTradeRuler(AccountDO account) {
        this.exchangeInformation = clients.get(account.getId()).getExchangeInformation();
    }
    
    public void keepListenKey() {
        for(AccountUserDataHandler handler : handlers.values()) {
            handler.keepListenKey();
        }
    }
    
    
    /**
     * * 将账号A的交易对的杠杆信息同步到交易A
     * @date 2023年5月29日
     * @author wuqiaoxin
     */
    public void syncAToBAccount(AccountDO accountA, AccountDO AccountB) {
        AccountInformation accountAInfo = clients.get(accountA.getId()).getAccountInformation();
        AccountInformation accountBInfo = clients.get(AccountB.getId()).getAccountInformation();
        Set<String> symbols = accountAInfo.getSymbolLeverage().keySet();
        for(String symbol : symbols) {
            if(!NumberUtils.equals(accountAInfo.getSymbolLeverage().get(symbol),
                    accountBInfo.getSymbolLeverage().get(symbol))) {
                clients.get(AccountB.getId()).changeInitialLeverage(symbol, 
                        accountAInfo.getSymbolLeverage().get(symbol));
            }
        }
    }
    
    public void changeInitialLeverage(List<AccountDO> followAccounts, String symbol, int leverage) {
        for(AccountDO followAccount : followAccounts) {
            clients.get(followAccount.getId()).changeInitialLeverage(symbol, leverage);
        }
    }

    public void syncZeroBalance() {
        
    }
    
}