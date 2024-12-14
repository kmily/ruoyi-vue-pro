package cn.iocoder.yudao.module.haoka.service.demo;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.haoka.controller.admin.demo.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.demo.HaokaDemoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.haoka.dal.mysql.demo.HaokaDemoMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.*;

/**
 * 好卡案例 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class HaokaDemoServiceImpl implements HaokaDemoService {

    @Resource
    private HaokaDemoMapper demoMapper;

    @Override
    public Long createDemo(HaokaDemoSaveReqVO createReqVO) {
        // 插入
        HaokaDemoDO demo = BeanUtils.toBean(createReqVO, HaokaDemoDO.class);
        demoMapper.insert(demo);
        // 返回
        return demo.getId();
    }

    @Override
    public void updateDemo(HaokaDemoSaveReqVO updateReqVO) {
        // 校验存在
        validateDemoExists(updateReqVO.getId());
        // 更新
        HaokaDemoDO updateObj = BeanUtils.toBean(updateReqVO, HaokaDemoDO.class);
        demoMapper.updateById(updateObj);
    }

    @Override
    public void deleteDemo(Long id) {
        // 校验存在
        validateDemoExists(id);
        // 删除
        demoMapper.deleteById(id);
    }

    private void validateDemoExists(Long id) {
        if (demoMapper.selectById(id) == null) {
            throw exception(DEMO_NOT_EXISTS);
        }
    }

    @Override
    public HaokaDemoDO getDemo(Long id) {
        return demoMapper.selectById(id);
    }

    @Override
    public PageResult<HaokaDemoDO> getDemoPage(HaokaDemoPageReqVO pageReqVO) {
        return demoMapper.selectPage(pageReqVO);
    }

}