package com.cw.module.trade.service.syncrecord;

import java.util.*;
import javax.validation.*;
import com.cw.module.trade.controller.admin.syncrecord.vo.*;
import com.cw.module.trade.dal.dataobject.syncrecord.SyncRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 账号同步记录 Service 接口
 *
 * @author chengjiale
 */
public interface SyncRecordService {

    /**
     * 创建账号同步记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSyncRecord(@Valid SyncRecordCreateReqVO createReqVO);

    /**
     * 更新账号同步记录
     *
     * @param updateReqVO 更新信息
     */
    void updateSyncRecord(@Valid SyncRecordUpdateReqVO updateReqVO);

    /**
     * 删除账号同步记录
     *
     * @param id 编号
     */
    void deleteSyncRecord(Long id);

    /**
     * 获得账号同步记录
     *
     * @param id 编号
     * @return 账号同步记录
     */
    SyncRecordDO getSyncRecord(Long id);

    /**
     * 获得账号同步记录列表
     *
     * @param ids 编号
     * @return 账号同步记录列表
     */
    List<SyncRecordDO> getSyncRecordList(Collection<Long> ids);

    /**
     * 获得账号同步记录分页
     *
     * @param pageReqVO 分页查询
     * @return 账号同步记录分页
     */
    PageResult<SyncRecordDO> getSyncRecordPage(SyncRecordPageReqVO pageReqVO);

    /**
     * 获得账号同步记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 账号同步记录列表
     */
    List<SyncRecordDO> getSyncRecordList(SyncRecordExportReqVO exportReqVO);

}
