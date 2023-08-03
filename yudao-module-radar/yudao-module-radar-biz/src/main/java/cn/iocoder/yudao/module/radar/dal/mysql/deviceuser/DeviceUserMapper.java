package cn.iocoder.yudao.module.radar.dal.mysql.deviceuser;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.radar.dal.dataobject.deviceuser.DeviceUserDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.radar.controller.admin.deviceuser.vo.*;

/**
 * 设备和用户绑定 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceUserMapper extends BaseMapperX<DeviceUserDO> {

    default PageResult<DeviceUserDO> selectPage(DeviceUserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceUserDO>()
                .eqIfPresent(DeviceUserDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceUserDO::getDeviceId, reqVO.getDeviceId())
                .betweenIfPresent(DeviceUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceUserDO::getId));
    }

    default List<DeviceUserDO> selectList(DeviceUserExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceUserDO>()
                .eqIfPresent(DeviceUserDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceUserDO::getDeviceId, reqVO.getDeviceId())
                .betweenIfPresent(DeviceUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceUserDO::getId));
    }

}
