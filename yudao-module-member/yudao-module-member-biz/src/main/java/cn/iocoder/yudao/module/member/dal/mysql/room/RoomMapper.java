package cn.iocoder.yudao.module.member.dal.mysql.room;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.room.RoomDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.room.vo.*;

/**
 * 用户房间 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RoomMapper extends BaseMapperX<RoomDO> {

    default PageResult<RoomDO> selectPage(RoomPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RoomDO>()
                .eqIfPresent(RoomDO::getUserId, reqVO.getUserId())
                .eqIfPresent(RoomDO::getFamilyId, reqVO.getFamilyId())
                .likeIfPresent(RoomDO::getName, reqVO.getName())
                .betweenIfPresent(RoomDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RoomDO::getId));
    }

    default List<RoomDO> selectList(RoomExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RoomDO>()
                .eqIfPresent(RoomDO::getUserId, reqVO.getUserId())
                .eqIfPresent(RoomDO::getFamilyId, reqVO.getFamilyId())
                .likeIfPresent(RoomDO::getName, reqVO.getName())
                .betweenIfPresent(RoomDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RoomDO::getId));
    }

}
