package cn.iocoder.yudao.module.steam.service.adblock;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.adblock.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.adblock.AdBlockDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.ad.AdDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.adblock.AdBlockMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.ad.AdMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 广告位 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class AdBlockServiceImpl implements AdBlockService {

    @Resource
    private AdBlockMapper adBlockMapper;
    @Resource
    private AdMapper adMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAdBlock(AdBlockSaveReqVO createReqVO) {
        // 插入
        AdBlockDO adBlock = BeanUtils.toBean(createReqVO, AdBlockDO.class);
        adBlockMapper.insert(adBlock);

        // 插入子表
        createAdList(adBlock.getId(), createReqVO.getAds());
        // 返回
        return adBlock.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdBlock(AdBlockSaveReqVO updateReqVO) {
        // 校验存在
        validateAdBlockExists(updateReqVO.getId());
        // 更新
        AdBlockDO updateObj = BeanUtils.toBean(updateReqVO, AdBlockDO.class);
        adBlockMapper.updateById(updateObj);

        // 更新子表
        updateAdList(updateReqVO.getId(), updateReqVO.getAds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAdBlock(Long id) {
        // 校验存在
        validateAdBlockExists(id);
        // 删除
        adBlockMapper.deleteById(id);

        // 删除子表
        deleteAdByBlockId(id);
    }

    private void validateAdBlockExists(Long id) {
        if (adBlockMapper.selectById(id) == null) {
            throw exception(AD_BLOCK_NOT_EXISTS);
        }
    }

    @Override
    public AdBlockDO getAdBlock(Long id) {
        return adBlockMapper.selectById(id);
    }

    @Override
    public PageResult<AdBlockDO> getAdBlockPage(AdBlockPageReqVO pageReqVO) {
        return adBlockMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（广告） ====================

    @Override
    public List<AdDO> getAdListByBlockId(Long blockId) {
        return adMapper.selectListByBlockId(blockId);
    }

    private void createAdList(Long blockId, List<AdDO> list) {
        list.forEach(o -> o.setBlockId(blockId));
        adMapper.insertBatch(list);
    }

    private void updateAdList(Long blockId, List<AdDO> list) {
        deleteAdByBlockId(blockId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createAdList(blockId, list);
    }

    private void deleteAdByBlockId(Long blockId) {
        adMapper.deleteByBlockId(blockId);
    }

}