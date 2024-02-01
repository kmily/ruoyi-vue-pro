package cn.iocoder.yudao.module.steam.service.selrarity;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.selrarity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selrarity.SelRarityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.selrarity.SelRarityMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 品质选择 Service 实现类
 *
 * @author lgm
 */
@Service
@Validated
public class SelRarityServiceImpl implements SelRarityService {

    @Resource
    private SelRarityMapper selRarityMapper;

    @Override
    public Long createSelRarity(SelRaritySaveReqVO createReqVO) {
        // 插入
        SelRarityDO selRarity = BeanUtils.toBean(createReqVO, SelRarityDO.class);
        selRarityMapper.insert(selRarity);
        // 返回
        return selRarity.getId();
    }

    @Override
    public void updateSelRarity(SelRaritySaveReqVO updateReqVO) {
        // 校验存在
        validateSelRarityExists(updateReqVO.getId());
        // 更新
        SelRarityDO updateObj = BeanUtils.toBean(updateReqVO, SelRarityDO.class);
        selRarityMapper.updateById(updateObj);
    }

    @Override
    public void deleteSelRarity(Long id) {
        // 校验存在
        validateSelRarityExists(id);
        // 删除
        selRarityMapper.deleteById(id);
    }

    private void validateSelRarityExists(Long id) {
        if (selRarityMapper.selectById(id) == null) {
            throw exception(SEL_RARITY_NOT_EXISTS);
        }
    }

    @Override
    public SelRarityDO getSelRarity(Long id) {
        return selRarityMapper.selectById(id);
    }

    @Override
    public PageResult<SelRarityDO> getSelRarityPage(SelRarityPageReqVO pageReqVO) {
        return selRarityMapper.selectPage(pageReqVO);
    }

}