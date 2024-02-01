package cn.iocoder.yudao.module.steam.service.selquality;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.selquality.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.selquality.SelQualityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.selquality.SelQualityMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 类别选择 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Resource
public class SelQualityServiceImpl implements SelQualityService {

    @Resource
    private SelQualityMapper selQualityMapper;

    @Override
    public Long createSelQuality(SelQualitySaveReqVO createReqVO) {
        // 插入
        SelQualityDO selQuality = BeanUtils.toBean(createReqVO, SelQualityDO.class);
        selQualityMapper.insert(selQuality);
        // 返回
        return selQuality.getId();
    }

    @Override
    public void updateSelQuality(SelQualitySaveReqVO updateReqVO) {
        // 校验存在
        validateSelQualityExists(updateReqVO.getId());
        // 更新
        SelQualityDO updateObj = BeanUtils.toBean(updateReqVO, SelQualityDO.class);
        selQualityMapper.updateById(updateObj);
    }

    @Override
    public void deleteSelQuality(Long id) {
        // 校验存在
        validateSelQualityExists(id);
        // 删除
        selQualityMapper.deleteById(id);
    }

    private void validateSelQualityExists(Long id) {
        if (selQualityMapper.selectById(id) == null) {
            throw exception(SEL_QUALITY_NOT_EXISTS);
        }
    }

    @Override
    public SelQualityDO getSelQuality(Long id) {
        return selQualityMapper.selectById(id);
    }

    @Override
    public PageResult<SelQualityDO> getSelQualityPage(SelQualityPageReqVO pageReqVO) {
        return selQualityMapper.selectPage(pageReqVO);
    }

}