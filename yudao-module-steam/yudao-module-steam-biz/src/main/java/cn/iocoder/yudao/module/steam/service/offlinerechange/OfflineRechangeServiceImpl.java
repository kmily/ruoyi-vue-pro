package cn.iocoder.yudao.module.steam.service.offlinerechange;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.offlinerechange.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.offlinerechange.OfflineRechangeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.offlinerechange.OfflineRechangeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 线下人工充值 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class OfflineRechangeServiceImpl implements OfflineRechangeService {

    @Resource
    private OfflineRechangeMapper offlineRechangeMapper;

    @Override
    public Long createOfflineRechange(OfflineRechangeSaveReqVO createReqVO) {
        // 插入
        OfflineRechangeDO offlineRechange = BeanUtils.toBean(createReqVO, OfflineRechangeDO.class);
        offlineRechangeMapper.insert(offlineRechange);
        // 返回
        return offlineRechange.getId();
    }

    @Override
    public void updateOfflineRechange(OfflineRechangeSaveReqVO updateReqVO) {
        // 校验存在
        validateOfflineRechangeExists(updateReqVO.getId());
        // 更新
        OfflineRechangeDO updateObj = BeanUtils.toBean(updateReqVO, OfflineRechangeDO.class);
        offlineRechangeMapper.updateById(updateObj);
    }

    @Override
    public void deleteOfflineRechange(Long id) {
        // 校验存在
        validateOfflineRechangeExists(id);
        // 删除
        offlineRechangeMapper.deleteById(id);
    }

    private void validateOfflineRechangeExists(Long id) {
        if (offlineRechangeMapper.selectById(id) == null) {
            throw exception(OFFLINE_RECHANGE_NOT_EXISTS);
        }
    }

    @Override
    public OfflineRechangeDO getOfflineRechange(Long id) {
        return offlineRechangeMapper.selectById(id);
    }

    @Override
    public PageResult<OfflineRechangeDO> getOfflineRechangePage(OfflineRechangePageReqVO pageReqVO) {
        return offlineRechangeMapper.selectPage(pageReqVO);
    }

}