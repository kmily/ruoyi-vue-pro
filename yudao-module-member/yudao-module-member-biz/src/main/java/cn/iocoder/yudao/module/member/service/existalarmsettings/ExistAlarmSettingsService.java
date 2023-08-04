package cn.iocoder.yudao.module.member.service.existalarmsettings;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.existalarmsettings.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.existalarmsettings.ExistAlarmSettingsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 人员存在感知雷达设置 Service 接口
 *
 * @author 芋道源码
 */
public interface ExistAlarmSettingsService {

    /**
     * 创建人员存在感知雷达设置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExistAlarmSettings(@Valid AppExistAlarmSettingsCreateReqVO createReqVO);

    /**
     * 更新人员存在感知雷达设置
     *
     * @param updateReqVO 更新信息
     */
    void updateExistAlarmSettings(@Valid AppExistAlarmSettingsUpdateReqVO updateReqVO);

    /**
     * 删除人员存在感知雷达设置
     *
     * @param id 编号
     */
    void deleteExistAlarmSettings(Long id);

    /**
     * 获得人员存在感知雷达设置
     *
     * @param id 编号
     * @return 人员存在感知雷达设置
     */
    ExistAlarmSettingsDO getExistAlarmSettings(Long id);

    /**
     * 获得人员存在感知雷达设置列表
     *
     * @param ids 编号
     * @return 人员存在感知雷达设置列表
     */
    List<ExistAlarmSettingsDO> getExistAlarmSettingsList(Collection<Long> ids);

    /**
     * 获得人员存在感知雷达设置分页
     *
     * @param pageReqVO 分页查询
     * @return 人员存在感知雷达设置分页
     */
    PageResult<ExistAlarmSettingsDO> getExistAlarmSettingsPage(AppExistAlarmSettingsPageReqVO pageReqVO);

    /**
     * 获得人员存在感知雷达设置列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 人员存在感知雷达设置列表
     */
    List<ExistAlarmSettingsDO> getExistAlarmSettingsList(AppExistAlarmSettingsExportReqVO exportReqVO);

}
