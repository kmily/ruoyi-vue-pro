package cn.iocoder.yudao.module.im.service.message;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.dal.mysql.message.ImMessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.im.enums.ErrorCodeConstants.MESSAGE_NOT_EXISTS;

/**
 * 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ImMessageServiceImpl implements ImMessageService {

    @Resource
    private ImMessageMapper imMessageMapper;

    @Override
    public Long createMessage(ImMessageSaveReqVO createReqVO) {
        // 插入
        ImMessageDO message = BeanUtils.toBean(createReqVO, ImMessageDO.class);
        imMessageMapper.insert(message);
        // 返回
        return message.getId();
    }

    @Override
    public void updateMessage(ImMessageSaveReqVO updateReqVO) {
        // 校验存在
        validateMessageExists(updateReqVO.getId());
        // 更新
        ImMessageDO updateObj = BeanUtils.toBean(updateReqVO, ImMessageDO.class);
        imMessageMapper.updateById(updateObj);
    }

    @Override
    public void deleteMessage(Long id) {
        // 校验存在
        validateMessageExists(id);
        // 删除
        imMessageMapper.deleteById(id);
    }

    private void validateMessageExists(Long id) {
        if (imMessageMapper.selectById(id) == null) {
            throw exception(MESSAGE_NOT_EXISTS);
        }
    }

    @Override
    public ImMessageDO getMessage(Long id) {
        return imMessageMapper.selectById(id);
    }

    @Override
    public PageResult<ImMessageDO> getMessagePage(ImMessagePageReqVO pageReqVO) {
        return imMessageMapper.selectPage(pageReqVO);
    }

    @Override
    public Long sendPrivateMessage(ImMessageSaveReqVO imMessageSaveReqVO) {
        return 0L;
    }

}