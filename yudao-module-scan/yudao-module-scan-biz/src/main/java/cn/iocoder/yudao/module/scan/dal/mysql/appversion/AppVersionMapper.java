package cn.iocoder.yudao.module.scan.dal.mysql.appversion;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.scan.dal.dataobject.appversion.AppVersionDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.scan.controller.admin.appversion.vo.*;
import org.apache.ibatis.annotations.Select;

/**
 * 应用版本记录 Mapper
 *
 * @author lyz
 */
@Mapper
public interface AppVersionMapper extends BaseMapperX<AppVersionDO> {

    default PageResult<AppVersionDO> selectPage(AppVersionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AppVersionDO>()
                .eqIfPresent(AppVersionDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(AppVersionDO::getPubKey, reqVO.getPubKey())
                .betweenIfPresent(AppVersionDO::getOnlineTime, reqVO.getOnlineTime())
                .betweenIfPresent(AppVersionDO::getOfflineTime, reqVO.getOfflineTime())
                .betweenIfPresent(AppVersionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AppVersionDO::getSysType, reqVO.getSysType())
                .likeIfPresent(AppVersionDO::getFileName, reqVO.getFileName())
                .eqIfPresent(AppVersionDO::getVerNo, reqVO.getVerNo())
                .betweenIfPresent(AppVersionDO::getForceUpdate, reqVO.getForceUpdate())
                .eqIfPresent(AppVersionDO::getAuditState, reqVO.getAuditState())
                .eqIfPresent(AppVersionDO::getRemark, reqVO.getRemark())
                .eqIfPresent(AppVersionDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AppVersionDO::getPackageHash, reqVO.getPackageHash())
                .eqIfPresent(AppVersionDO::getState, reqVO.getState())
                .eqIfPresent(AppVersionDO::getVerCode, reqVO.getVerCode())
                .orderByDesc(AppVersionDO::getId));
    }

    default List<AppVersionDO> selectList(AppVersionExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AppVersionDO>()
                .eqIfPresent(AppVersionDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(AppVersionDO::getPubKey, reqVO.getPubKey())
                .betweenIfPresent(AppVersionDO::getOnlineTime, reqVO.getOnlineTime())
                .betweenIfPresent(AppVersionDO::getOfflineTime, reqVO.getOfflineTime())
                .betweenIfPresent(AppVersionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AppVersionDO::getSysType, reqVO.getSysType())
                .likeIfPresent(AppVersionDO::getFileName, reqVO.getFileName())
                .eqIfPresent(AppVersionDO::getVerNo, reqVO.getVerNo())
                .betweenIfPresent(AppVersionDO::getForceUpdate, reqVO.getForceUpdate())
                .eqIfPresent(AppVersionDO::getAuditState, reqVO.getAuditState())
                .eqIfPresent(AppVersionDO::getRemark, reqVO.getRemark())
                .eqIfPresent(AppVersionDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AppVersionDO::getPackageHash, reqVO.getPackageHash())
                .eqIfPresent(AppVersionDO::getState, reqVO.getState())
                .eqIfPresent(AppVersionDO::getVerCode, reqVO.getVerCode())
                .orderByDesc(AppVersionDO::getId));
    }

    @Select("SELECT * FROM scan_app_version WHERE state =0 and audit_state=1 and sys_type=0 order by ver_code desc limit 1")
    AppVersionDO selectNewVersion();
}
