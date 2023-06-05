package com.cw.module.trade.service.followrecord;

import java.util.*;
import javax.validation.*;
import com.cw.module.trade.controller.admin.followrecord.vo.*;
import com.cw.module.trade.dal.dataobject.followrecord.FollowRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 账号跟随记录 Service 接口
 *
 * @author chengjiale
 */
public interface FollowRecordService {

    /**
     * 创建账号跟随记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFollowRecord(@Valid FollowRecordCreateReqVO createReqVO);

    /**
     * 更新账号跟随记录
     *
     * @param updateReqVO 更新信息
     */
    void updateFollowRecord(@Valid FollowRecordUpdateReqVO updateReqVO);

    /**
     * 删除账号跟随记录
     *
     * @param id 编号
     */
    void deleteFollowRecord(Long id);

    /**
     * 获得账号跟随记录
     *
     * @param id 编号
     * @return 账号跟随记录
     */
    FollowRecordDO getFollowRecord(Long id);

    /**
     * 获得账号跟随记录列表
     *
     * @param ids 编号
     * @return 账号跟随记录列表
     */
    List<FollowRecordDO> getFollowRecordList(Collection<Long> ids);

    /**
     * 获得账号跟随记录分页
     *
     * @param pageReqVO 分页查询
     * @return 账号跟随记录分页
     */
    PageResult<FollowRecordDO> getFollowRecordPage(FollowRecordPageReqVO pageReqVO);

    /**
     * 获得账号跟随记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 账号跟随记录列表
     */
    List<FollowRecordDO> getFollowRecordList(FollowRecordExportReqVO exportReqVO);

    /**
     * * 根据第三方订单号查找跟随订单
     * @date 2023年5月25日
     * @author wuqiaoxin
     */
    List<FollowRecordDO> listFollowRecord(Long thirdOrderId);

}
