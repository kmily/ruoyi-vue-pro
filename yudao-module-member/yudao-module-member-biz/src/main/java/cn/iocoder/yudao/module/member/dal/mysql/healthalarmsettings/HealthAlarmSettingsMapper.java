package cn.iocoder.yudao.module.member.dal.mysql.healthalarmsettings;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.healthalarmsettings.HealthAlarmSettingsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo.*;

/**
 * 体征检测雷达设置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface HealthAlarmSettingsMapper extends BaseMapperX<HealthAlarmSettingsDO> {

    default PageResult<HealthAlarmSettingsDO> selectPage(AppHealthAlarmSettingsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HealthAlarmSettingsDO>()
                .eqIfPresent(HealthAlarmSettingsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(HealthAlarmSettingsDO::getNotice, reqVO.getNotice())
                .eqIfPresent(HealthAlarmSettingsDO::getHeart, reqVO.getHeart())
                .eqIfPresent(HealthAlarmSettingsDO::getHeartRange, reqVO.getHeartRange())
                .eqIfPresent(HealthAlarmSettingsDO::getBreathe, reqVO.getBreathe())
                .eqIfPresent(HealthAlarmSettingsDO::getBreatheRange, reqVO.getBreatheRange())
                .eqIfPresent(HealthAlarmSettingsDO::getMove, reqVO.getMove())
                .eqIfPresent(HealthAlarmSettingsDO::getSitUp, reqVO.getSitUp())
                .eqIfPresent(HealthAlarmSettingsDO::getOutBed, reqVO.getOutBed())
                .betweenIfPresent(HealthAlarmSettingsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HealthAlarmSettingsDO::getId));
    }

    default List<HealthAlarmSettingsDO> selectList(AppHealthAlarmSettingsExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<HealthAlarmSettingsDO>()
                .eqIfPresent(HealthAlarmSettingsDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(HealthAlarmSettingsDO::getNotice, reqVO.getNotice())
                .eqIfPresent(HealthAlarmSettingsDO::getHeart, reqVO.getHeart())
                .eqIfPresent(HealthAlarmSettingsDO::getHeartRange, reqVO.getHeartRange())
                .eqIfPresent(HealthAlarmSettingsDO::getBreathe, reqVO.getBreathe())
                .eqIfPresent(HealthAlarmSettingsDO::getBreatheRange, reqVO.getBreatheRange())
                .eqIfPresent(HealthAlarmSettingsDO::getMove, reqVO.getMove())
                .eqIfPresent(HealthAlarmSettingsDO::getSitUp, reqVO.getSitUp())
                .eqIfPresent(HealthAlarmSettingsDO::getOutBed, reqVO.getOutBed())
                .betweenIfPresent(HealthAlarmSettingsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HealthAlarmSettingsDO::getId));
    }

}
