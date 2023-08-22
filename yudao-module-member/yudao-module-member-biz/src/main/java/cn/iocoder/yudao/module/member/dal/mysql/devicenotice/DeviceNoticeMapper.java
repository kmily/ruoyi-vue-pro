package cn.iocoder.yudao.module.member.dal.mysql.devicenotice;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.devicenotice.DeviceNoticeDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.devicenotice.vo.*;
import org.apache.ibatis.annotations.Param;

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
                .eqIfPresent(DeviceNoticeDO::getStatus, reqVO.getStatus())
                .and(reqVO.getFamilyId() != null, wrapper -> wrapper.eq(DeviceNoticeDO::getFamilyId, reqVO.getFamilyId())
                        .or()
                        .eq(DeviceNoticeDO::getCategory, 1))
                .orderByDesc(DeviceNoticeDO::getHappenTime));
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

    /**
     * 查询最大阅读时间
     * @param userId
     * @return
     */
    LocalDateTime selectMaxDate(@Param("userId") Long userId);

    /**
     * 查询微阅读条数
     * @param userId 用户ID
     * @param familyId 家庭ID
     * @return 未阅读条数
     */
    Long selectUnReadCount(@Param("userId") Long userId, @Param("familyId") Long familyId);

    void updateByNoticeId(@Param("noticeId") Long noticeId, @Param("content") String content);


}
