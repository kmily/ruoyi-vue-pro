package cn.iocoder.yudao.module.steam.service.alipayisv;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.alipayisv.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.alipayisv.AlipayIsvMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 签约ISV用户 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class AlipayIsvServiceImpl implements AlipayIsvService {

    @Resource
    private AlipayIsvMapper alipayIsvMapper;

    @Override
    public Long createAlipayIsv(AlipayIsvSaveReqVO createReqVO) {
        // 插入
        AlipayIsvDO alipayIsv = BeanUtils.toBean(createReqVO, AlipayIsvDO.class);
        alipayIsvMapper.insert(alipayIsv);
        // 返回
        return alipayIsv.getId();
    }

    @Override
    public void updateAlipayIsv(AlipayIsvSaveReqVO updateReqVO) {
        // 校验存在
        validateAlipayIsvExists(updateReqVO.getId());
        // 更新
        AlipayIsvDO updateObj = BeanUtils.toBean(updateReqVO, AlipayIsvDO.class);
        alipayIsvMapper.updateById(updateObj);
    }

    @Override
    public void deleteAlipayIsv(Long id) {
        // 校验存在
        validateAlipayIsvExists(id);
        // 删除
        alipayIsvMapper.deleteById(id);
    }

    private void validateAlipayIsvExists(Long id) {
        if (alipayIsvMapper.selectById(id) == null) {
            throw exception(ALIPAY_ISV_NOT_EXISTS);
        }
    }

    @Override
    public AlipayIsvDO getAlipayIsv(Long id) {
        return alipayIsvMapper.selectById(id);
    }

    @Override
    public PageResult<AlipayIsvDO> getAlipayIsvPage(AlipayIsvPageReqVO pageReqVO) {
        return alipayIsvMapper.selectPage(pageReqVO);
    }

}