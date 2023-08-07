package cn.iocoder.yudao.module.member.service.healthalarmsettings;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.healthalarmsettings.HealthAlarmSettingsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 体征检测雷达设置 Service 接口
 *
 * @author 芋道源码
 */
public interface HealthAlarmSettingsService {

    /**
     * 创建体征检测雷达设置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createHealthAlarmSettings(@Valid AppHealthAlarmSettingsCreateReqVO createReqVO);

    /**
     * 更新体征检测雷达设置
     *
     * @param updateReqVO 更新信息
     */
    void updateHealthAlarmSettings(@Valid AppHealthAlarmSettingsUpdateReqVO updateReqVO);

    /**
     * 删除体征检测雷达设置
     *
     * @param id 编号
     */
    void deleteHealthAlarmSettings(Long id);

    /**
     * 获得体征检测雷达设置
     *
     * @param id 编号
     * @return 体征检测雷达设置
     */
    HealthAlarmSettingsDO getHealthAlarmSettings(Long id);

    /**
     * 获得体征检测雷达设置列表
     *
     * @param ids 编号
     * @return 体征检测雷达设置列表
     */
    List<HealthAlarmSettingsDO> getHealthAlarmSettingsList(Collection<Long> ids);

    /**
     * 获得体征检测雷达设置分页
     *
     * @param pageReqVO 分页查询
     * @return 体征检测雷达设置分页
     */
    PageResult<HealthAlarmSettingsDO> getHealthAlarmSettingsPage(AppHealthAlarmSettingsPageReqVO pageReqVO);

    /**
     * 获得体征检测雷达设置列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 体征检测雷达设置列表
     */
    List<HealthAlarmSettingsDO> getHealthAlarmSettingsList(AppHealthAlarmSettingsExportReqVO exportReqVO);

}
