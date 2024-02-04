package cn.iocoder.yudao.module.steam.service.invdesc;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.invdesc.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 库存信息详情 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InvDescServiceImpl implements InvDescService {

    @Resource
    private InvDescMapper invDescMapper;

    @Override
    public Long createInvDesc(InvDescSaveReqVO createReqVO) {
        // 插入
        InvDescDO invDesc = BeanUtils.toBean(createReqVO, InvDescDO.class);
        invDescMapper.insert(invDesc);
        // 返回
        return invDesc.getId();
    }

    @Override
    public void updateInvDesc(InvDescSaveReqVO updateReqVO) {
        // 校验存在
        validateInvDescExists(updateReqVO.getId());
        // 更新
        InvDescDO updateObj = BeanUtils.toBean(updateReqVO, InvDescDO.class);
        invDescMapper.updateById(updateObj);
    }

    @Override
    public void deleteInvDesc(Long id) {
        // 校验存在
        validateInvDescExists(id);
        // 删除
        invDescMapper.deleteById(id);
    }

    private void validateInvDescExists(Long id) {
        if (invDescMapper.selectById(id) == null) {
            throw exception(INV_DESC_NOT_EXISTS);
        }
    }

    @Override
    public InvDescDO getInvDesc(Long id) {
        return invDescMapper.selectById(id);
    }

    @Override
    public PageResult<InvDescDO> getInvDescPage(InvDescPageReqVO pageReqVO) {
        return invDescMapper.selectPage(pageReqVO);
    }

}