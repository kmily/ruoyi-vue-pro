package cn.iocoder.yudao.module.steam.service.youyouorder;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyouorder.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyouorder.YouyouOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.youyouorder.YouyouOrderMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * steam订单 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class YouyouOrderServiceImpl implements YouyouOrderService {

    @Resource
    private YouyouOrderMapper youyouOrderMapper;

    @Override
    public Long createYouyouOrder(YouyouOrderSaveReqVO createReqVO) {
        // 插入
        YouyouOrderDO youyouOrder = BeanUtils.toBean(createReqVO, YouyouOrderDO.class);
        youyouOrderMapper.insert(youyouOrder);
        // 返回
        return youyouOrder.getId();
    }

    @Override
    public void updateYouyouOrder(YouyouOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateYouyouOrderExists(updateReqVO.getId());
        // 更新
        YouyouOrderDO updateObj = BeanUtils.toBean(updateReqVO, YouyouOrderDO.class);
        youyouOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteYouyouOrder(Long id) {
        // 校验存在
        validateYouyouOrderExists(id);
        // 删除
        youyouOrderMapper.deleteById(id);
    }

    private void validateYouyouOrderExists(Long id) {
        if (youyouOrderMapper.selectById(id) == null) {
            throw exception(YOUYOU_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public YouyouOrderDO getYouyouOrder(Long id) {
        return youyouOrderMapper.selectById(id);
    }

    @Override
    public PageResult<YouyouOrderDO> getYouyouOrderPage(YouyouOrderPageReqVO pageReqVO) {
        return youyouOrderMapper.selectPage(pageReqVO);
    }

}