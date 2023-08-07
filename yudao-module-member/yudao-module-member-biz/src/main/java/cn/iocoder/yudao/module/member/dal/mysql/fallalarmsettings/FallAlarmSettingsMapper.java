package cn.iocoder.yudao.module.member.dal.mysql.fallalarmsettings;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.fallalarmsettings.FallAlarmSettingsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo.*;

/**
 * 跌倒雷达设置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FallAlarmSettingsMapper extends BaseMapperX<FallAlarmSettingsDO> {

    default PageResult<FallAlarmSettingsDO> selectPage(AppFallAlarmSettingsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FallAlarmSettingsDO>()
                .eqIfPresent(FallAlarmSettingsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(FallAlarmSettingsDO::getNotice, reqVO.getNotice())
                .eqIfPresent(FallAlarmSettingsDO::getEnter, reqVO.getEnter())
                .eqIfPresent(FallAlarmSettingsDO::getLeave, reqVO.getLeave())
                .eqIfPresent(FallAlarmSettingsDO::getFall, reqVO.getFall())
                .eqIfPresent(FallAlarmSettingsDO::getGetUp, reqVO.getGetUp())
                .betweenIfPresent(FallAlarmSettingsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FallAlarmSettingsDO::getId));
    }

    default List<FallAlarmSettingsDO> selectList(AppFallAlarmSettingsExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<FallAlarmSettingsDO>()
                .eqIfPresent(FallAlarmSettingsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(FallAlarmSettingsDO::getNotice, reqVO.getNotice())
                .eqIfPresent(FallAlarmSettingsDO::getEnter, reqVO.getEnter())
                .eqIfPresent(FallAlarmSettingsDO::getLeave, reqVO.getLeave())
                .eqIfPresent(FallAlarmSettingsDO::getFall, reqVO.getFall())
                .eqIfPresent(FallAlarmSettingsDO::getGetUp, reqVO.getGetUp())
                .betweenIfPresent(FallAlarmSettingsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FallAlarmSettingsDO::getId));
    }

}
