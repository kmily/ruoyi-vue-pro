package cn.iocoder.yudao.module.steam.service.selexterior;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.selexterior.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selexterior.SelExteriorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.selexterior.SelExteriorMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 外观选择 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Resource
public class SelExteriorServiceImpl implements SelExteriorService {

    @Resource
    private SelExteriorMapper selExteriorMapper;

    @Override
    public Long createSelExterior(SelExteriorSaveReqVO createReqVO) {
        // 插入
        SelExteriorDO selExterior = BeanUtils.toBean(createReqVO, SelExteriorDO.class);
        selExteriorMapper.insert(selExterior);
        // 返回
        return selExterior.getId();
    }

    @Override
    public void updateSelExterior(SelExteriorSaveReqVO updateReqVO) {
        // 校验存在
        validateSelExteriorExists(updateReqVO.getId());
        // 更新
        SelExteriorDO updateObj = BeanUtils.toBean(updateReqVO, SelExteriorDO.class);
        selExteriorMapper.updateById(updateObj);
    }

    @Override
    public void deleteSelExterior(Long id) {
        // 校验存在
        validateSelExteriorExists(id);
        // 删除
        selExteriorMapper.deleteById(id);
    }

    private void validateSelExteriorExists(Long id) {
        if (selExteriorMapper.selectById(id) == null) {
            throw exception(SEL_EXTERIOR_NOT_EXISTS);
        }
    }

    @Override
    public SelExteriorDO getSelExterior(Long id) {
        return selExteriorMapper.selectById(id);
    }

    @Override
    public PageResult<SelExteriorDO> getSelExteriorPage(SelExteriorPageReqVO pageReqVO) {
        return selExteriorMapper.selectPage(pageReqVO);
    }

}