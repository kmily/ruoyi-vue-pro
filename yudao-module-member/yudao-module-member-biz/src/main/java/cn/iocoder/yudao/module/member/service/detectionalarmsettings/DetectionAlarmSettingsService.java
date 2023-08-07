package cn.iocoder.yudao.module.member.service.detectionalarmsettings;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.detectionalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.detectionalarmsettings.DetectionAlarmSettingsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 人体检测雷达设置 Service 接口
 *
 * @author 芋道源码
 */
public interface DetectionAlarmSettingsService {

    /**
     * 创建人体检测雷达设置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDetectionAlarmSettings(@Valid AppDetectionAlarmSettingsCreateReqVO createReqVO);

    /**
     * 更新人体检测雷达设置
     *
     * @param updateReqVO 更新信息
     */
    void updateDetectionAlarmSettings(@Valid AppDetectionAlarmSettingsUpdateReqVO updateReqVO);

    /**
     * 删除人体检测雷达设置
     *
     * @param id 编号
     */
    void deleteDetectionAlarmSettings(Long id);

    /**
     * 获得人体检测雷达设置
     *
     * @param id 编号
     * @return 人体检测雷达设置
     */
    DetectionAlarmSettingsDO getDetectionAlarmSettings(Long id);

    /**
     * 获得人体检测雷达设置列表
     *
     * @param ids 编号
     * @return 人体检测雷达设置列表
     */
    List<DetectionAlarmSettingsDO> getDetectionAlarmSettingsList(Collection<Long> ids);

    /**
     * 获得人体检测雷达设置分页
     *
     * @param pageReqVO 分页查询
     * @return 人体检测雷达设置分页
     */
    PageResult<DetectionAlarmSettingsDO> getDetectionAlarmSettingsPage(AppDetectionAlarmSettingsPageReqVO pageReqVO);

    /**
     * 获得人体检测雷达设置列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 人体检测雷达设置列表
     */
    List<DetectionAlarmSettingsDO> getDetectionAlarmSettingsList(AppDetectionAlarmSettingsExportReqVO exportReqVO);

}
