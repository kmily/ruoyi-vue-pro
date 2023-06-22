package com.cw.module.trade.service.position;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.AccountInformation;
import com.binance.client.model.trade.Asset;
import com.binance.client.model.trade.Position;
import com.cw.module.trade.controller.admin.position.vo.PositionCreateReqVO;
import com.cw.module.trade.controller.admin.position.vo.PositionExportReqVO;
import com.cw.module.trade.controller.admin.position.vo.PositionPageReqVO;
import com.cw.module.trade.controller.admin.position.vo.PositionUpdateReqVO;
import com.cw.module.trade.controller.admin.syncrecord.vo.SyncRecordCreateReqVO;
import com.cw.module.trade.convert.position.PositionConvert;
import com.cw.module.trade.dal.dataobject.account.AccountDO;
import com.cw.module.trade.dal.dataobject.position.PositionDO;
import com.cw.module.trade.dal.mysql.account.AccountMapper;
import com.cw.module.trade.dal.mysql.position.PositionMapper;
import com.cw.module.trade.handler.WebSocketHandlerFactory;
import com.cw.module.trade.service.account.AccountService;
import com.cw.module.trade.service.syncrecord.SyncRecordService;
import com.tb.utils.NumberUtils;
import com.tb.utils.json.JsonUtil;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 账户持仓信息 Service 实现类
 *
 * @author chengjiale
 */
@Service
@Validated
public class PositionServiceImpl implements PositionService {

    @Resource
    private PositionMapper positionMapper;
    @Autowired
    private SyncRecordService syncRecordServiceImpl;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Long createPosition(PositionCreateReqVO createReqVO) {
        // 插入
        PositionDO position = PositionConvert.INSTANCE.convert(createReqVO);
        positionMapper.insert(position);
        // 返回
        return position.getId();
    }

    @Override
    public void updatePosition(PositionUpdateReqVO updateReqVO) {
        // 校验存在
        validatePositionExists(updateReqVO.getId());
        // 更新
        PositionDO updateObj = PositionConvert.INSTANCE.convert(updateReqVO);
        positionMapper.updateById(updateObj);
    }

    @Override
    public void deletePosition(Long id) {
        // 校验存在
        validatePositionExists(id);
        // 删除
        positionMapper.deleteById(id);
    }

    private void validatePositionExists(Long id) {
        if (positionMapper.selectById(id) == null) {
//            throw exception(POSITION_NOT_EXISTS);
        }
    }

    @Override
    public PositionDO getPosition(Long id) {
        return positionMapper.selectById(id);
    }

    @Override
    public List<PositionDO> getPositionList(Collection<Long> ids) {
        return positionMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<PositionDO> getPositionPage(PositionPageReqVO pageReqVO) {
        return positionMapper.selectPage(pageReqVO);
    }

    @Override
    public List<PositionDO> getPositionList(PositionExportReqVO exportReqVO) {
        return positionMapper.selectList(exportReqVO);
    }
    
    @Override
    public void syncPosition() {
        List<AccountDO> accounts = accountMapper.listMonitorAccount();
        for(AccountDO account : accounts) {
            syncAccountPositionById(account.getId());
        }
        
    }

    @Override
    public void syncAccountPositionById(Long accountId) {
        SyncRequestClient syncRequestClient = WebSocketHandlerFactory.get().getClients().get(accountId);
        AccountInformation accountInformation = syncRequestClient.getAccountInformation();
        
        Asset usdtBalance = accountInformation.getAssets().stream().filter(
                item -> "USDT".equals(item.getAsset())).findFirst().orElse(null);
        WebSocketHandlerFactory.get().getAccountLastestBalance().put(accountId, usdtBalance.getBalance());
        
        accountInformation.setSymbolLeverage(null);
        // 保存同步记录
        SyncRecordCreateReqVO syncRecord = new SyncRecordCreateReqVO();
        syncRecord.setAccountId(accountId);
        syncRecord.setType("account");
        syncRecord.setThirdData(JsonUtil.object2String(accountInformation));
        syncRecordServiceImpl.createSyncRecord(syncRecord);
        
        String balance = JSONUtil.toJsonStr(accountInformation.getAssets());
        AccountDO account = new AccountDO();
        account.setId(accountId);
        account.setBalance(balance);
        account.setLastBalanceQueryTime(System.currentTimeMillis());
        accountMapper.updateById(account);
        
        Map<String, Position> positions = accountInformation.getPositions();
        WebSocketHandlerFactory.get().getAccountPsitions().put(accountId, positions);
        List<String> symbols = positionMapper.selectAccountSymbolPosition(accountId);
        symbols.removeAll(positions.keySet());
        for(Position position : positions.values()) {
            updatePositon(accountId, position.getSymbol(), new BigDecimal(position.getEntryPrice()), 
                    position.getPositionAmt(), JsonUtil.object2String(position));
        }
        // 清仓数据处理
        if(CollectionUtil.isNotEmpty(symbols)) {
            for(String symbol : symbols) {
                updatePositon(accountId, symbol, new BigDecimal(0), new BigDecimal(0), "");
            }
        }
    }
    
    private void updatePositon(Long accountId, String symbol, 
            BigDecimal entryPrice, BigDecimal quantity, String thirdData) {
        PositionDO updateDO = positionMapper.selectOne(Wrappers.<PositionDO>lambdaQuery()
                .eq(PositionDO::getAccountId, accountId)
                .eq(PositionDO::getSymbol, symbol).last(" order by id desc limit 1"));
        if(updateDO == null) {
            updateDO = new PositionDO();
            updateDO.setAccountId(accountId);
            updateDO.setSymbol(symbol);
        }
        updateDO.setEntryPrice(entryPrice);
        updateDO.setQuantity(quantity);
        updateDO.setThirdData(thirdData);
        if(NumberUtils.gtz(updateDO.getId())) {
            positionMapper.updateById(updateDO);
        } else {
            positionMapper.insert(updateDO);
        }
    }

    @Override
    public void addPosition(Long accountId, String symbol, BigDecimal positionAmt, String position) {
//        PositionDO symbolPosition = positionMapper.selectLastestPosition(accountId, symbol);
//        if(symbolPosition == null) {
           
//            return ;
//        }
//        if(symbolPosition.getQuantity().compareTo(positionAmt) == 0) {
//            return;
//        }
//        symbolPosition.setQuantity(positionAmt);
//        symbolPosition.setThirdData(position);
//        positionMapper.updateById(symbolPosition);
    }
    
    @Override
    public PositionDO selectLastestPosition(Long accountId, String symbol) {
        return positionMapper.selectLastestPosition(accountId, symbol);
    }

}
