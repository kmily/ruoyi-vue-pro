package cn.iocoder.yudao.module.member.dal.mysql.noticeuser;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.noticeuser.NoticeUserDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.noticeuser.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 用户消息关联 Mapper
 *
 * @author 和尘同光
 */
@Mapper
public interface NoticeUserMapper extends BaseMapperX<NoticeUserDO> {

    default PageResult<NoticeUserDO> selectPage(AppNoticeUserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<NoticeUserDO>()
                .eqIfPresent(NoticeUserDO::getNoticeId, reqVO.getNoticeId())
                .eqIfPresent(NoticeUserDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(NoticeUserDO::getReadTime, reqVO.getReadTime())
                .eqIfPresent(NoticeUserDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(NoticeUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(NoticeUserDO::getId));
    }

    default List<NoticeUserDO> selectList(AppNoticeUserExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<NoticeUserDO>()
                .eqIfPresent(NoticeUserDO::getNoticeId, reqVO.getNoticeId())
                .eqIfPresent(NoticeUserDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(NoticeUserDO::getReadTime, reqVO.getReadTime())
                .eqIfPresent(NoticeUserDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(NoticeUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(NoticeUserDO::getId));
    }

    /**
     * 查询最大时间
     * @param userId 用户ID
     * @return
     */
    LocalDateTime selectMaxDate(@Param("userId") Long userId);
}
