package cn.iocoder.yudao.module.member.dal.mysql.deviceuser;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.deviceuser.DeviceUserDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.deviceuser.vo.*;

/**
 * 设备和用户绑定 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceUserMapper extends BaseMapperX<DeviceUserDO> {

    default PageResult<DeviceUserDO> selectPage(AppDeviceUserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceUserDO>()
                .eqIfPresent(DeviceUserDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceUserDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DeviceUserDO::getFamilyId, reqVO.getFamilyId())
                .eqIfPresent(DeviceUserDO::getRoomId, reqVO.getRoomId())
                .likeIfPresent(DeviceUserDO::getCustomName, reqVO.getCustomName())

                .betweenIfPresent(DeviceUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceUserDO::getId));
    }

    default List<DeviceUserDO> selectList(AppDeviceUserExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceUserDO>()
                .eqIfPresent(DeviceUserDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceUserDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DeviceUserDO::getFamilyId, reqVO.getFamilyId())
                .eqIfPresent(DeviceUserDO::getRoomId, reqVO.getRoomId())
                .likeIfPresent(DeviceUserDO::getCustomName, reqVO.getCustomName())

                .betweenIfPresent(DeviceUserDO::getCreateTime, reqVO.getCreateTime()));
                //.orderByDesc(DeviceUserDO::getId));
    }

}
