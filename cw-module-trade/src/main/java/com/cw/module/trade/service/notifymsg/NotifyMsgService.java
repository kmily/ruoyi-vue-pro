package com.cw.module.trade.service.notifymsg;

import java.util.*;
import javax.validation.*;
import com.cw.module.trade.controller.admin.notifymsg.vo.*;
import com.cw.module.trade.dal.dataobject.notifymsg.NotifyMsgDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 账号通知记录 Service 接口
 *
 * @author chengjiale
 */
public interface NotifyMsgService {

    /**
     * 创建账号通知记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createNotifyMsg(@Valid NotifyMsgCreateReqVO createReqVO);

    /**
     * 更新账号通知记录
     *
     * @param updateReqVO 更新信息
     */
    void updateNotifyMsg(@Valid NotifyMsgUpdateReqVO updateReqVO);

    /**
     * 删除账号通知记录
     *
     * @param id 编号
     */
    void deleteNotifyMsg(Long id);

    /**
     * 获得账号通知记录
     *
     * @param id 编号
     * @return 账号通知记录
     */
    NotifyMsgDO getNotifyMsg(Long id);

    /**
     * 获得账号通知记录列表
     *
     * @param ids 编号
     * @return 账号通知记录列表
     */
    List<NotifyMsgDO> getNotifyMsgList(Collection<Long> ids);

    /**
     * 获得账号通知记录分页
     *
     * @param pageReqVO 分页查询
     * @return 账号通知记录分页
     */
    PageResult<NotifyMsgDO> getNotifyMsgPage(NotifyMsgPageReqVO pageReqVO);

    /**
     * 获得账号通知记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 账号通知记录列表
     */
    List<NotifyMsgDO> getNotifyMsgList(NotifyMsgExportReqVO exportReqVO);

}
