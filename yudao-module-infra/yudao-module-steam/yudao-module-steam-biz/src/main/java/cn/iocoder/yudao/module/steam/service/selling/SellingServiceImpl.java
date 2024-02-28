package cn.iocoder.yudao.module.steam.service.selling;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 在售饰品 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class SellingServiceImpl implements SellingService {

    @Resource
    private SellingMapper sellingMapper;

    @Override
    public Long createSelling(SellingSaveReqVO createReqVO) {
        // 插入
        SellingDO selling = BeanUtils.toBean(createReqVO, SellingDO.class);
        sellingMapper.insert(selling);
        // 返回
        return selling.getId();
    }

    @Override
    public void updateSelling(SellingSaveReqVO updateReqVO) {
        // 校验存在
        validateSellingExists(updateReqVO.getId());
        // 更新
        SellingDO updateObj = BeanUtils.toBean(updateReqVO, SellingDO.class);
        sellingMapper.updateById(updateObj);
    }

    @Override
    public void deleteSelling(Long id) {
        // 校验存在
        validateSellingExists(id);
        // 删除
        sellingMapper.deleteById(id);
    }

    private void validateSellingExists(Long id) {
        if (sellingMapper.selectById(id) == null) {
            throw exception(SELLING_NOT_EXISTS);
        }
    }

    @Override
    public SellingDO getSelling(Long id) {
        return sellingMapper.selectById(id);
    }

    @Override
    public PageResult<SellingDO> getSellingPage(SellingPageReqVO pageReqVO) {
        return sellingMapper.selectPage(pageReqVO);
    }

}