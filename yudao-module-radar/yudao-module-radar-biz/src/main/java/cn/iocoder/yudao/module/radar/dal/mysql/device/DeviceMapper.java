package cn.iocoder.yudao.module.radar.dal.mysql.device;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.radar.dal.dataobject.device.DeviceDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.radar.controller.admin.device.vo.*;

/**
 * 设备信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceMapper extends BaseMapperX<DeviceDO> {

    default PageResult<DeviceDO> selectPage(DevicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceDO>()
                .eqIfPresent(DeviceDO::getSn, reqVO.getSn())
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .eqIfPresent(DeviceDO::getType, reqVO.getType())
                .eqIfPresent(DeviceDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceDO::getId));
    }

    default List<DeviceDO> selectList(DeviceExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceDO>()
                .eqIfPresent(DeviceDO::getSn, reqVO.getSn())
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .eqIfPresent(DeviceDO::getType, reqVO.getType())
                .eqIfPresent(DeviceDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceDO::getId));
    }

}
