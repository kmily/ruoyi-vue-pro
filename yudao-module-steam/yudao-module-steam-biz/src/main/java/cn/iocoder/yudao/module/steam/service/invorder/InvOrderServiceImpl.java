package cn.iocoder.yudao.module.steam.service.invorder;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invorder.InvOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.invorder.InvOrderMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * steam订单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InvOrderServiceImpl implements InvOrderService {

    @Resource
    private InvOrderMapper invOrderMapper;

    @Override
    public Long createInvOrder(InvOrderSaveReqVO createReqVO) {
        // 插入
        InvOrderDO invOrder = BeanUtils.toBean(createReqVO, InvOrderDO.class);
        invOrderMapper.insert(invOrder);
        // 返回
        return invOrder.getId();
    }

    @Override
    public void updateInvOrder(InvOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateInvOrderExists(updateReqVO.getId());
        // 更新
        InvOrderDO updateObj = BeanUtils.toBean(updateReqVO, InvOrderDO.class);
        invOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteInvOrder(Long id) {
        // 校验存在
        validateInvOrderExists(id);
        // 删除
        invOrderMapper.deleteById(id);
    }

    private void validateInvOrderExists(Long id) {
        if (invOrderMapper.selectById(id) == null) {
            throw exception(INV_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public InvOrderDO getInvOrder(Long id) {
        return invOrderMapper.selectById(id);
    }

    @Override
    public PageResult<InvOrderDO> getInvOrderPage(InvOrderPageReqVO pageReqVO) {
        return invOrderMapper.selectPage(pageReqVO);
    }

}