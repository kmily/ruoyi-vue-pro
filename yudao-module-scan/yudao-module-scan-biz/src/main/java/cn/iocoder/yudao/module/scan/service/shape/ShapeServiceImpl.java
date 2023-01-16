package cn.iocoder.yudao.module.scan.service.shape;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.scan.controller.admin.shape.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.shape.ShapeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.scan.convert.shape.ShapeConvert;
import cn.iocoder.yudao.module.scan.dal.mysql.shape.ShapeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;

/**
 * 体型 Service 实现类
 *
 * @author lyz
 */
@Service
@Validated
public class ShapeServiceImpl implements ShapeService {

    @Resource
    private ShapeMapper shapeMapper;

    @Override
    public Long createShape(ShapeCreateReqVO createReqVO) {
        // 插入
        ShapeDO shape = ShapeConvert.INSTANCE.convert(createReqVO);
        shapeMapper.insert(shape);
        // 返回
        return shape.getId();
    }

    @Override
    public void updateShape(ShapeUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateShapeExists(updateReqVO.getId());
        // 更新
        ShapeDO updateObj = ShapeConvert.INSTANCE.convert(updateReqVO);
        shapeMapper.updateById(updateObj);
    }

    @Override
    public void deleteShape(Long id) {
        // 校验存在
        this.validateShapeExists(id);
        // 删除
        shapeMapper.deleteById(id);
    }

    private void validateShapeExists(Long id) {
        if (shapeMapper.selectById(id) == null) {
            throw exception(SHAPE_NOT_EXISTS);
        }
    }

    @Override
    public ShapeDO getShape(Long id) {
        return shapeMapper.selectById(id);
    }

    @Override
    public List<ShapeDO> getShapeList(Collection<Long> ids) {
        return shapeMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ShapeDO> getShapePage(ShapePageReqVO pageReqVO) {
        return shapeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ShapeDO> getShapeList(ShapeExportReqVO exportReqVO) {
        return shapeMapper.selectList(exportReqVO);
    }

}
