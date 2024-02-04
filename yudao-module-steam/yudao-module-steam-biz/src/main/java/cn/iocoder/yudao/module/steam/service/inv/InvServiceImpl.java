package cn.iocoder.yudao.module.steam.service.inv;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * steam用户库存储 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InvServiceImpl implements InvService {

    @Resource
    private InvMapper invMapper;

    @Override
    public Integer createInv(InvSaveReqVO createReqVO) {
        // 插入
        InvDO inv = BeanUtils.toBean(createReqVO, InvDO.class);
        invMapper.insert(inv);
        // 返回
        return inv.getId();
    }

    @Override
    public void updateInv(InvSaveReqVO updateReqVO) {
        // 校验存在
        validateInvExists(updateReqVO.getId());
        // 更新
        InvDO updateObj = BeanUtils.toBean(updateReqVO, InvDO.class);
        invMapper.updateById(updateObj);
    }

    @Override
    public void deleteInv(Integer id) {
        // 校验存在
        validateInvExists(id);
        // 删除
        invMapper.deleteById(id);
    }

    private void validateInvExists(Integer id) {
        if (invMapper.selectById(id) == null) {
            throw exception(INV_NOT_EXISTS);
        }
    }

    @Override
    public InvDO getInv(Integer id) {
        return invMapper.selectById(id);
    }

    @Override
    public PageResult<InvDO> getInvPage(InvPageReqVO pageReqVO) {
        return invMapper.selectPage(pageReqVO);
    }

}