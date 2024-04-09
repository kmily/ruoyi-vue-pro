package cn.iocoder.yudao.module.steam.service.otherselling;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.otherselling.OtherSellingMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 其他平台在售 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class OtherSellingServiceImpl implements OtherSellingService {

    @Resource
    private OtherSellingMapper otherSellingMapper;

    @Override
    public Integer createOtherSelling(OtherSellingSaveReqVO createReqVO) {
        // 插入
        OtherSellingDO otherSelling = BeanUtils.toBean(createReqVO, OtherSellingDO.class);
        otherSellingMapper.insert(otherSelling);
        // 返回
        return otherSelling.getId();
    }

    @Override
    public void updateOtherSelling(OtherSellingSaveReqVO updateReqVO) {
        // 校验存在
        validateOtherSellingExists(updateReqVO.getId());
        // 更新
        OtherSellingDO updateObj = BeanUtils.toBean(updateReqVO, OtherSellingDO.class);
        otherSellingMapper.updateById(updateObj);
    }

    @Override
    public void deleteOtherSelling(Integer id) {
        // 校验存在
        validateOtherSellingExists(id);
        // 删除
        otherSellingMapper.deleteById(id);
    }

    private void validateOtherSellingExists(Integer id) {
        if (otherSellingMapper.selectById(id) == null) {
            throw exception(OTHER_SELLING_NOT_EXISTS);
        }
    }

    @Override
    public OtherSellingDO getOtherSelling(Integer id) {
        return otherSellingMapper.selectById(id);
    }

    @Override
    public PageResult<OtherSellingDO> getOtherSellingPage(OtherSellingPageReqVO pageReqVO) {
        return otherSellingMapper.selectPage(pageReqVO);
    }

}