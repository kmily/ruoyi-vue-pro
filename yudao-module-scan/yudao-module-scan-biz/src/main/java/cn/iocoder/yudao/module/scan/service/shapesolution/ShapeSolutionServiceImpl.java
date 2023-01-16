package cn.iocoder.yudao.module.scan.service.shapesolution;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.scan.controller.admin.shapesolution.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shapesolution.ShapeSolutionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.convert.shapesolution.ShapeSolutionConvert;
import cn.iocoder.yudao.module.scan.dal.mysql.shapesolution.ShapeSolutionMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;

/**
 * 体型解决方案 Service 实现类
 *
 * @author lyz
 */
@Service
@Validated
public class ShapeSolutionServiceImpl implements ShapeSolutionService {

    @Resource
    private ShapeSolutionMapper shapeSolutionMapper;

    @Override
    public Long createShapeSolution(ShapeSolutionCreateReqVO createReqVO) {
        // 插入
        ShapeSolutionDO shapeSolution = ShapeSolutionConvert.INSTANCE.convert(createReqVO);
        shapeSolutionMapper.insert(shapeSolution);
        // 返回
        return shapeSolution.getId();
    }

    @Override
    public void updateShapeSolution(ShapeSolutionUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateShapeSolutionExists(updateReqVO.getId());
        // 更新
        ShapeSolutionDO updateObj = ShapeSolutionConvert.INSTANCE.convert(updateReqVO);
        shapeSolutionMapper.updateById(updateObj);
    }

    @Override
    public void deleteShapeSolution(Long id) {
        // 校验存在
        this.validateShapeSolutionExists(id);
        // 删除
        shapeSolutionMapper.deleteById(id);
    }

    private void validateShapeSolutionExists(Long id) {
        if (shapeSolutionMapper.selectById(id) == null) {
            throw exception(SHAPE_SOLUTION_NOT_EXISTS);
        }
    }

    @Override
    public ShapeSolutionDO getShapeSolution(Long id) {
        return shapeSolutionMapper.selectById(id);
    }

    @Override
    public List<ShapeSolutionDO> getShapeSolutionList(Collection<Long> ids) {
        return shapeSolutionMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ShapeSolutionDO> getShapeSolutionPage(ShapeSolutionPageReqVO pageReqVO) {
        return shapeSolutionMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ShapeSolutionDO> getShapeSolutionList(ShapeSolutionExportReqVO exportReqVO) {
        return shapeSolutionMapper.selectList(exportReqVO);
    }

}
