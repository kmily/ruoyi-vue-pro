package cn.iocoder.yudao.module.member.service.noticeuser;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.noticeuser.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.noticeuser.NoticeUserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 用户消息关联 Service 接口
 *
 * @author 和尘同光
 */
public interface NoticeUserService {

    /**
     * 创建用户消息关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createNoticeUser(@Valid AppNoticeUserCreateReqVO createReqVO);

    /**
     * 更新用户消息关联
     *
     * @param updateReqVO 更新信息
     */
    void updateNoticeUser(@Valid AppNoticeUserUpdateReqVO updateReqVO);

    /**
     * 删除用户消息关联
     *
     * @param id 编号
     */
    void deleteNoticeUser(Long id);

    /**
     * 获得用户消息关联
     *
     * @param id 编号
     * @return 用户消息关联
     */
    NoticeUserDO getNoticeUser(Long id);

    /**
     * 获得用户消息关联列表
     *
     * @param ids 编号
     * @return 用户消息关联列表
     */
    List<NoticeUserDO> getNoticeUserList(Collection<Long> ids);

    /**
     * 获得用户消息关联分页
     *
     * @param pageReqVO 分页查询
     * @return 用户消息关联分页
     */
    PageResult<NoticeUserDO> getNoticeUserPage(AppNoticeUserPageReqVO pageReqVO);

    /**
     * 获得用户消息关联列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 用户消息关联列表
     */
    List<NoticeUserDO> getNoticeUserList(AppNoticeUserExportReqVO exportReqVO);

    /**
     * 查询未读消息数量
     * @param loginUserId 用户ID
     * @return
     */
    Long getUnRead(Long loginUserId, Long familyId);
}
