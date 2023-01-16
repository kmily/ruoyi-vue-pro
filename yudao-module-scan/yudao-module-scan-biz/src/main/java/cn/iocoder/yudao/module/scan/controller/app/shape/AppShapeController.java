package cn.iocoder.yudao.module.scan.controller.app.shape;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.scan.controller.app.shape.vo.*;
import cn.iocoder.yudao.module.scan.convert.shape.ShapeConvert;
import cn.iocoder.yudao.module.scan.dal.dataobject.shape.ShapeDO;
import cn.iocoder.yudao.module.scan.service.shape.ShapeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Api(tags = "扫描APP - 体型")
@RestController
@RequestMapping("/scan/shape")
@Validated
public class AppShapeController {

    @Resource
    private ShapeService shapeService;

    @GetMapping("/get")
    @ApiOperation("获得体型")
    @ApiImplicitParam(name = "id", value = "编号", required = true,  dataTypeClass = Long.class)
    public CommonResult<AppShapeRespVO> getShape(@RequestParam("id") Long id) {
        ShapeDO shape = shapeService.getShape(id);
        return success(ShapeConvert.INSTANCE.convert02(shape));
    }

    @GetMapping("/list")
    @ApiOperation("获得体型列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true,  dataTypeClass = List.class)
    public CommonResult<List<AppShapeRespVO>> getShapeList(@RequestParam("ids") Collection<Long> ids) {
        List<ShapeDO> list = shapeService.getShapeList(ids);
        return success(ShapeConvert.INSTANCE.convertList03(list));
    }
}
