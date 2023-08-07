package cn.iocoder.yudao.module.member.service.fallalarmsettings;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.fallalarmsettings.FallAlarmSettingsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 跌倒雷达设置 Service 接口
 *
 * @author 芋道源码
 */
public interface FallAlarmSettingsService {

    /**
     * 创建跌倒雷达设置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFallAlarmSettings(@Valid AppFallAlarmSettingsCreateReqVO createReqVO);

    /**
     * 更新跌倒雷达设置
     *
     * @param updateReqVO 更新信息
     */
    void updateFallAlarmSettings(@Valid AppFallAlarmSettingsUpdateReqVO updateReqVO);

    /**
     * 删除跌倒雷达设置
     *
     * @param id 编号
     */
    void deleteFallAlarmSettings(Long id);

    /**
     * 获得跌倒雷达设置
     *
     * @param id 编号
     * @return 跌倒雷达设置
     */
    FallAlarmSettingsDO getFallAlarmSettings(Long id);

    /**
     * 获得跌倒雷达设置列表
     *
     * @param ids 编号
     * @return 跌倒雷达设置列表
     */
    List<FallAlarmSettingsDO> getFallAlarmSettingsList(Collection<Long> ids);

    /**
     * 获得跌倒雷达设置分页
     *
     * @param pageReqVO 分页查询
     * @return 跌倒雷达设置分页
     */
    PageResult<FallAlarmSettingsDO> getFallAlarmSettingsPage(AppFallAlarmSettingsPageReqVO pageReqVO);

    /**
     * 获得跌倒雷达设置列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 跌倒雷达设置列表
     */
    List<FallAlarmSettingsDO> getFallAlarmSettingsList(AppFallAlarmSettingsExportReqVO exportReqVO);

}
