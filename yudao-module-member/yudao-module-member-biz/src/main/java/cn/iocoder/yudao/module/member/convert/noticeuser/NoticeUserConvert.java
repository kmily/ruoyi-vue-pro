package cn.iocoder.yudao.module.member.convert.noticeuser;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.member.controller.app.noticeuser.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.noticeuser.NoticeUserDO;

/**
 * 用户消息关联 Convert
 *
 * @author 和尘同光
 */
@Mapper
public interface NoticeUserConvert {

    NoticeUserConvert INSTANCE = Mappers.getMapper(NoticeUserConvert.class);

    NoticeUserDO convert(AppNoticeUserCreateReqVO bean);

    NoticeUserDO convert(AppNoticeUserUpdateReqVO bean);

    AppNoticeUserRespVO convert(NoticeUserDO bean);

    List<AppNoticeUserRespVO> convertList(List<NoticeUserDO> list);

    PageResult<AppNoticeUserRespVO> convertPage(PageResult<NoticeUserDO> page);

    List<AppNoticeUserExcelVO> convertList02(List<NoticeUserDO> list);

}
