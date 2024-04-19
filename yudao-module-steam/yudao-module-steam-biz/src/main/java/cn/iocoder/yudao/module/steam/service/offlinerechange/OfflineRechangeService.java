package cn.iocoder.yudao.module.steam.service.offlinerechange;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.offlinerechange.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.offlinerechange.OfflineRechangeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 线下人工充值 Service 接口
 *
 * @author 管理员
 */
public interface OfflineRechangeService {

    /**
     * 创建线下人工充值
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOfflineRechange(@Valid OfflineRechangeSaveReqVO createReqVO);

    /**
     * 更新线下人工充值
     *
     * @param updateReqVO 更新信息
     */
    void updateOfflineRechange(@Valid OfflineRechangeSaveReqVO updateReqVO);

    /**
     * 删除线下人工充值
     *
     * @param id 编号
     */
    void deleteOfflineRechange(Long id);

    /**
     * 获得线下人工充值
     *
     * @param id 编号
     * @return 线下人工充值
     */
    OfflineRechangeDO getOfflineRechange(Long id);

    /**
     * 获得线下人工充值分页
     *
     * @param pageReqVO 分页查询
     * @return 线下人工充值分页
     */
    PageResult<OfflineRechangeDO> getOfflineRechangePage(OfflineRechangePageReqVO pageReqVO);

}