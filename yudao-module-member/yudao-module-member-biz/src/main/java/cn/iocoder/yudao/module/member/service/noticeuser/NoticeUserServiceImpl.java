package cn.iocoder.yudao.module.member.service.noticeuser;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.service.devicenotice.DeviceNoticeService;
import cn.iocoder.yudao.module.system.api.notice.NoticeApi;
import cn.iocoder.yudao.module.system.api.notice.dto.NoticeDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.member.controller.app.noticeuser.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.noticeuser.NoticeUserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.noticeuser.NoticeUserConvert;
import cn.iocoder.yudao.module.member.dal.mysql.noticeuser.NoticeUserMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 用户消息关联 Service 实现类
 *
 * @author 和尘同光
 */
@Service
@Validated
public class NoticeUserServiceImpl implements NoticeUserService {

    @Resource
    private NoticeUserMapper noticeUserMapper;

    @Resource
    private DeviceNoticeService deviceNoticeService;

    @Resource
    private NoticeApi noticeApi;


    @Override
    public Long createNoticeUser(AppNoticeUserCreateReqVO createReqVO) {
        // 插入
        NoticeUserDO noticeUser = NoticeUserConvert.INSTANCE.convert(createReqVO);
        noticeUserMapper.insert(noticeUser);
        // 返回
        return noticeUser.getId();
    }

    @Override
    public void updateNoticeUser(AppNoticeUserUpdateReqVO updateReqVO) {
        // 校验存在
        validateNoticeUserExists(updateReqVO.getId());
        // 更新
        NoticeUserDO updateObj = NoticeUserConvert.INSTANCE.convert(updateReqVO);
        noticeUserMapper.updateById(updateObj);
    }

    @Override
    public void deleteNoticeUser(Long id) {
        // 校验存在
        validateNoticeUserExists(id);
        // 删除
        noticeUserMapper.deleteById(id);
    }

    private void validateNoticeUserExists(Long id) {
        if (noticeUserMapper.selectById(id) == null) {
            throw exception(NOTICE_USER_NOT_EXISTS);
        }
    }

    @Override
    public NoticeUserDO getNoticeUser(Long id) {
        return noticeUserMapper.selectById(id);
    }

    @Override
    public List<NoticeUserDO> getNoticeUserList(Collection<Long> ids) {
        return noticeUserMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<NoticeUserDO> getNoticeUserPage(AppNoticeUserPageReqVO pageReqVO) {
        return noticeUserMapper.selectPage(pageReqVO);
    }

    @Override
    public List<NoticeUserDO> getNoticeUserList(AppNoticeUserExportReqVO exportReqVO) {
        return noticeUserMapper.selectList(exportReqVO);
    }

    @Override
    public Long getUnRead(Long loginUserId, Long familyId) {
        Long count = deviceNoticeService.getUnReadCount(loginUserId, familyId);

        Long aLong = noticeUserMapper.selectCount(new LambdaQueryWrapperX<NoticeUserDO>().eqIfPresent(NoticeUserDO::getUserId, loginUserId)
                .eq(NoticeUserDO::getStatus, 0));

        LocalDateTime maxDate = noticeUserMapper.selectMaxDate(loginUserId);
        List<NoticeDTO> notices = noticeApi.getNotices(maxDate);
        if(CollUtil.isNotEmpty(notices)){
            List<NoticeUserDO> noticeUserDOs = notices.stream().map(item -> new NoticeUserDO()
                    .setNoticeId(item.getId())
                    .setUserId(loginUserId)).collect(Collectors.toList());
            noticeUserMapper.insertBatch(noticeUserDOs, 100);
        }
        return (count == null? 0: count) + (aLong == null? 0: aLong) + notices.size();
    }

}
