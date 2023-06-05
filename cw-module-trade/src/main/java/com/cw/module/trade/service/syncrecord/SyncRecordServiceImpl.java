package com.cw.module.trade.service.syncrecord;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.cw.module.trade.controller.admin.syncrecord.vo.*;
import com.cw.module.trade.dal.dataobject.syncrecord.SyncRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import com.cw.module.trade.convert.syncrecord.SyncRecordConvert;
import com.cw.module.trade.dal.mysql.syncrecord.SyncRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cw.module.trade.enums.ErrorCodeConstants.*;

/**
 * 账号同步记录 Service 实现类
 *
 * @author chengjiale
 */
@Service
@Validated
public class SyncRecordServiceImpl implements SyncRecordService {

    @Resource
    private SyncRecordMapper syncRecordMapper;

    @Override
    public Long createSyncRecord(SyncRecordCreateReqVO createReqVO) {
        // 插入
        SyncRecordDO syncRecord = SyncRecordConvert.INSTANCE.convert(createReqVO);
        syncRecordMapper.insert(syncRecord);
        // 返回
        return syncRecord.getId();
    }

    @Override
    public void updateSyncRecord(SyncRecordUpdateReqVO updateReqVO) {
        // 校验存在
        validateSyncRecordExists(updateReqVO.getId());
        // 更新
        SyncRecordDO updateObj = SyncRecordConvert.INSTANCE.convert(updateReqVO);
        syncRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteSyncRecord(Long id) {
        // 校验存在
        validateSyncRecordExists(id);
        // 删除
        syncRecordMapper.deleteById(id);
    }

    private void validateSyncRecordExists(Long id) {
        if (syncRecordMapper.selectById(id) == null) {
//            throw exception(SYNC_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public SyncRecordDO getSyncRecord(Long id) {
        return syncRecordMapper.selectById(id);
    }

    @Override
    public List<SyncRecordDO> getSyncRecordList(Collection<Long> ids) {
        return syncRecordMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<SyncRecordDO> getSyncRecordPage(SyncRecordPageReqVO pageReqVO) {
        return syncRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SyncRecordDO> getSyncRecordList(SyncRecordExportReqVO exportReqVO) {
        return syncRecordMapper.selectList(exportReqVO);
    }

}
