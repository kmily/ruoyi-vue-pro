package cn.iocoder.yudao.module.haoka.service.demo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.haoka.controller.admin.demo.vo.DemoPageReqVO;
import cn.iocoder.yudao.module.haoka.controller.admin.demo.vo.DemoSaveReqVO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.demo.DemoDO;
import cn.iocoder.yudao.module.haoka.dal.mysql.demo.DemoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.haoka.enums.ErrorCodeConstants.DEMO_NOT_EXISTS;

/**
 * 好卡案例 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DemoServiceImpl implements DemoService {

    @Resource
    private DemoMapper demoMapper;

    @Override
    public Long createDemo(DemoSaveReqVO createReqVO) {
        // 插入
        DemoDO demo = BeanUtils.toBean(createReqVO, DemoDO.class);
        demoMapper.insert(demo);
        // 返回
        return demo.getId();
    }

    @Override
    public void updateDemo(DemoSaveReqVO updateReqVO) {
        // 校验存在
        validateDemoExists(updateReqVO.getId());
        // 更新
        DemoDO updateObj = BeanUtils.toBean(updateReqVO, DemoDO.class);
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
    public DemoDO getDemo(Long id) {
        return demoMapper.selectById(id);
    }

    @Override
    public PageResult<DemoDO> getDemoPage(DemoPageReqVO pageReqVO) {
        return demoMapper.selectPage(pageReqVO);
    }

}
