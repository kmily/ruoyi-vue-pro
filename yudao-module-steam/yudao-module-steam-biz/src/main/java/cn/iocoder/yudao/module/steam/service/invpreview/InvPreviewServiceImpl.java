package cn.iocoder.yudao.module.steam.service.invpreview;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 饰品在售预览 Service 实现类
 *
 * @author LeeAm
 */
@Service
@Validated
public class InvPreviewServiceImpl implements InvPreviewService {

    @Resource
    private InvPreviewMapper invPreviewMapper;

    @Override
    public Long createInvPreview(InvPreviewSaveReqVO createReqVO) {
        // 插入
        InvPreviewDO invPreview = BeanUtils.toBean(createReqVO, InvPreviewDO.class);
        invPreviewMapper.insert(invPreview);
        // 返回
        return invPreview.getId();
    }

    @Override
    public void updateInvPreview(InvPreviewSaveReqVO updateReqVO) {
        // 校验存在
        validateInvPreviewExists(updateReqVO.getId());
        // 更新
        InvPreviewDO updateObj = BeanUtils.toBean(updateReqVO, InvPreviewDO.class);
        invPreviewMapper.updateById(updateObj);
    }

    @Override
    public void deleteInvPreview(Long id) {
        // 校验存在
        validateInvPreviewExists(id);
        // 删除
        invPreviewMapper.deleteById(id);
    }

    private void validateInvPreviewExists(Long id) {
        if (invPreviewMapper.selectById(id) == null) {
            throw exception(INV_PREVIEW_NOT_EXISTS);
        }
    }

    @Override
    public InvPreviewDO getInvPreview(Long id) {
        return invPreviewMapper.selectById(id);
    }

    @Override
    public PageResult<InvPreviewDO> getInvPreviewPage(InvPreviewPageReqVO pageReqVO) {
        return invPreviewMapper.selectPage(pageReqVO);
    }

}