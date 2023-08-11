package cn.iocoder.yudao.module.member.service.devicenotice;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.devicenotice.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.devicenotice.DeviceNoticeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 设备通知 Service 接口
 *
 * @author 芋道源码
 */
public interface DeviceNoticeService {

    /**
     * 创建设备通知
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDeviceNotice(@Valid AppDeviceNoticeCreateReqVO createReqVO);

    /**
     * 更新设备通知
     *
     * @param updateReqVO 更新信息
     */
    void updateDeviceNotice(@Valid AppDeviceNoticeUpdateReqVO updateReqVO);

    /**
     * 删除设备通知
     *
     * @param id 编号
     */
    void deleteDeviceNotice(Long id);

    /**
     * 获得设备通知
     *
     * @param id 编号
     * @return 设备通知
     */
    DeviceNoticeDO getDeviceNotice(Long id);

    /**
     * 获得设备通知列表
     *
     * @param ids 编号
     * @return 设备通知列表
     */
    List<DeviceNoticeDO> getDeviceNoticeList(Collection<Long> ids);

    /**
     * 获得设备通知分页
     *
     * @param pageReqVO 分页查询
     * @return 设备通知分页
     */
    PageResult<DeviceNoticeDO> getDeviceNoticePage(AppDeviceNoticePageReqVO pageReqVO);

    /**
     * 获得设备通知列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 设备通知列表
     */
    List<DeviceNoticeDO> getDeviceNoticeList(AppDeviceNoticeExportReqVO exportReqVO);

    /**
     * 收到消息
     * @param type
     * @param content
     * @param number
     */
    void createDeviceNotice(int type, String content, String number);
}
