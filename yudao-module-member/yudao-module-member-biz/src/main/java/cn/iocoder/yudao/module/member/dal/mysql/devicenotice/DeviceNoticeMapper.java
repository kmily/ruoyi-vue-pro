package cn.iocoder.yudao.module.member.dal.mysql.devicenotice;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.devicenotice.DeviceNoticeDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.devicenotice.vo.*;

/**
 * 设备通知 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceNoticeMapper extends BaseMapperX<DeviceNoticeDO> {

    default PageResult<DeviceNoticeDO> selectPage(AppDeviceNoticePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceNoticeDO>()
                .eqIfPresent(DeviceNoticeDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceNoticeDO::getFamilyId, reqVO.getFamilyId())
                .eqIfPresent(DeviceNoticeDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DeviceNoticeDO::getContent, reqVO.getContent())
                .eqIfPresent(DeviceNoticeDO::getType, reqVO.getType())
                .eqIfPresent(DeviceNoticeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DeviceNoticeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceNoticeDO::getId));
    }

    default List<DeviceNoticeDO> selectList(AppDeviceNoticeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceNoticeDO>()
                .eqIfPresent(DeviceNoticeDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceNoticeDO::getFamilyId, reqVO.getFamilyId())
                .eqIfPresent(DeviceNoticeDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DeviceNoticeDO::getContent, reqVO.getContent())
                .eqIfPresent(DeviceNoticeDO::getType, reqVO.getType())
                .eqIfPresent(DeviceNoticeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DeviceNoticeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceNoticeDO::getId));
    }

}
