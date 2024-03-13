package cn.iocoder.yudao.module.im.service.inbox;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import cn.iocoder.yudao.module.im.dal.mysql.inbox.InboxMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.INBOX_NOT_EXISTS;

/**
 * 收件箱 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InboxServiceImpl implements InboxService {

    @Resource
    private InboxMapper inboxMapper;

    @Override
    public Long createInbox(InboxSaveReqVO createReqVO) {
        // 插入
        ImInboxDO inbox = BeanUtils.toBean(createReqVO, ImInboxDO.class);
        inboxMapper.insert(inbox);
        // 返回
        return inbox.getId();
    }

    @Override
    public void updateInbox(InboxSaveReqVO updateReqVO) {
        // 校验存在
        validateInboxExists(updateReqVO.getId());
        // 更新
        ImInboxDO updateObj = BeanUtils.toBean(updateReqVO, ImInboxDO.class);
        inboxMapper.updateById(updateObj);
    }

    @Override
    public void deleteInbox(Long id) {
        // 校验存在
        validateInboxExists(id);
        // 删除
        inboxMapper.deleteById(id);
    }

    private void validateInboxExists(Long id) {
        if (inboxMapper.selectById(id) == null) {
            throw exception(INBOX_NOT_EXISTS);
        }
    }

    @Override
    public ImInboxDO getInbox(Long id) {
        return inboxMapper.selectById(id);
    }

    @Override
    public PageResult<ImInboxDO> getInboxPage(InboxPageReqVO pageReqVO) {
        return inboxMapper.selectPage(pageReqVO);
    }

}