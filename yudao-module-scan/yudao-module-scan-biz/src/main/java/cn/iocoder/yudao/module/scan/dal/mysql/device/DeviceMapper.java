package cn.iocoder.yudao.module.scan.dal.mysql.device;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.scan.dal.dataobject.device.DeviceDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.scan.controller.admin.device.vo.*;

/**
 * 设备 Mapper
 *
 * @author lyz
 */
@Mapper
public interface DeviceMapper extends BaseMapperX<DeviceDO> {

    default PageResult<DeviceDO> selectPage(DevicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceDO>()
                .betweenIfPresent(DeviceDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .eqIfPresent(DeviceDO::getCode, reqVO.getCode())
                .eqIfPresent(DeviceDO::getContact, reqVO.getContact())
                .eqIfPresent(DeviceDO::getPhone, reqVO.getPhone())
                .eqIfPresent(DeviceDO::getSerialNo, reqVO.getSerialNo())
                .likeIfPresent(DeviceDO::getProvinceName, reqVO.getProvinceName())
                .eqIfPresent(DeviceDO::getProvinceCode, reqVO.getProvinceCode())
                .likeIfPresent(DeviceDO::getCityName, reqVO.getCityName())
                .eqIfPresent(DeviceDO::getCityCode, reqVO.getCityCode())
                .likeIfPresent(DeviceDO::getAreaName, reqVO.getAreaName())
                .eqIfPresent(DeviceDO::getAreaCode, reqVO.getAreaCode())
                .eqIfPresent(DeviceDO::getAddress, reqVO.getAddress())
                .eqIfPresent(DeviceDO::getPassword, reqVO.getPassword())
                .likeIfPresent(DeviceDO::getStoreName, reqVO.getStoreName())
                .orderByDesc(DeviceDO::getId));
    }

    default List<DeviceDO> selectList(DeviceExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceDO>()
                .betweenIfPresent(DeviceDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .eqIfPresent(DeviceDO::getCode, reqVO.getCode())
                .eqIfPresent(DeviceDO::getContact, reqVO.getContact())
                .eqIfPresent(DeviceDO::getPhone, reqVO.getPhone())
                .eqIfPresent(DeviceDO::getSerialNo, reqVO.getSerialNo())
                .likeIfPresent(DeviceDO::getProvinceName, reqVO.getProvinceName())
                .eqIfPresent(DeviceDO::getProvinceCode, reqVO.getProvinceCode())
                .likeIfPresent(DeviceDO::getCityName, reqVO.getCityName())
                .eqIfPresent(DeviceDO::getCityCode, reqVO.getCityCode())
                .likeIfPresent(DeviceDO::getAreaName, reqVO.getAreaName())
                .eqIfPresent(DeviceDO::getAreaCode, reqVO.getAreaCode())
                .eqIfPresent(DeviceDO::getAddress, reqVO.getAddress())
                .eqIfPresent(DeviceDO::getPassword, reqVO.getPassword())
                .likeIfPresent(DeviceDO::getStoreName, reqVO.getStoreName())
                .orderByDesc(DeviceDO::getId));
    }
}
