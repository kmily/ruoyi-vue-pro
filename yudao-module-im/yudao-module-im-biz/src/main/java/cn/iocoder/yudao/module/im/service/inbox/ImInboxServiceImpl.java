package cn.iocoder.yudao.module.im.service.inbox;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import cn.iocoder.yudao.module.im.dal.mysql.inbox.ImInboxMapper;
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
public class ImInboxServiceImpl implements ImInboxService {

    @Resource
    private ImInboxMapper imInboxMapper;

    @Override
    public Long createInbox(ImInboxSaveReqVO createReqVO) {
        // 插入
        ImInboxDO inbox = BeanUtils.toBean(createReqVO, ImInboxDO.class);
        imInboxMapper.insert(inbox);
        // 返回
        return inbox.getId();
    }

    @Override
    public void updateInbox(ImInboxSaveReqVO updateReqVO) {
        // 校验存在
        validateInboxExists(updateReqVO.getId());
        // 更新
        ImInboxDO updateObj = BeanUtils.toBean(updateReqVO, ImInboxDO.class);
        imInboxMapper.updateById(updateObj);
    }

    @Override
    public void deleteInbox(Long id) {
        // 校验存在
        validateInboxExists(id);
        // 删除
        imInboxMapper.deleteById(id);
    }

    private void validateInboxExists(Long id) {
        if (imInboxMapper.selectById(id) == null) {
            throw exception(INBOX_NOT_EXISTS);
        }
    }

    @Override
    public ImInboxDO getInbox(Long id) {
        return imInboxMapper.selectById(id);
    }

    @Override
    public PageResult<ImInboxDO> getInboxPage(ImInboxPageReqVO pageReqVO) {
        return imInboxMapper.selectPage(pageReqVO);
    }

}