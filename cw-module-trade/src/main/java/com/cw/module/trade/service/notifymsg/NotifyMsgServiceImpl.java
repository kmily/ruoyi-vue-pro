package com.cw.module.trade.service.notifymsg;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.cw.module.trade.controller.admin.notifymsg.vo.*;
import com.cw.module.trade.dal.dataobject.notifymsg.NotifyMsgDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import com.cw.module.trade.convert.notifymsg.NotifyMsgConvert;
import com.cw.module.trade.dal.mysql.notifymsg.NotifyMsgMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cw.module.trade.enums.ErrorCodeConstants.*;

/**
 * 账号通知记录 Service 实现类
 *
 * @author chengjiale
 */
@Service
@Validated
public class NotifyMsgServiceImpl implements NotifyMsgService {

    @Resource
    private NotifyMsgMapper notifyMsgMapper;

    @Override
    public Long createNotifyMsg(NotifyMsgCreateReqVO createReqVO) {
        // 插入
        NotifyMsgDO notifyMsg = NotifyMsgConvert.INSTANCE.convert(createReqVO);
        notifyMsgMapper.insert(notifyMsg);
        // 返回
        return notifyMsg.getId();
    }

    @Override
    public void updateNotifyMsg(NotifyMsgUpdateReqVO updateReqVO) {
        // 校验存在
        validateNotifyMsgExists(updateReqVO.getId());
        // 更新
        NotifyMsgDO updateObj = NotifyMsgConvert.INSTANCE.convert(updateReqVO);
        notifyMsgMapper.updateById(updateObj);
    }

    @Override
    public void deleteNotifyMsg(Long id) {
        // 校验存在
        validateNotifyMsgExists(id);
        // 删除
        notifyMsgMapper.deleteById(id);
    }

    private void validateNotifyMsgExists(Long id) {
        if (notifyMsgMapper.selectById(id) == null) {
//            throw exception(NOTIFY_MSG_NOT_EXISTS);
        }
    }

    @Override
    public NotifyMsgDO getNotifyMsg(Long id) {
        return notifyMsgMapper.selectById(id);
    }

    @Override
    public List<NotifyMsgDO> getNotifyMsgList(Collection<Long> ids) {
        return notifyMsgMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<NotifyMsgDO> getNotifyMsgPage(NotifyMsgPageReqVO pageReqVO) {
        return notifyMsgMapper.selectPage(pageReqVO);
    }

    @Override
    public List<NotifyMsgDO> getNotifyMsgList(NotifyMsgExportReqVO exportReqVO) {
        return notifyMsgMapper.selectList(exportReqVO);
    }

}
