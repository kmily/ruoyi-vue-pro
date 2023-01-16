package cn.iocoder.yudao.module.scan.controller.app.shapesolution;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.scan.controller.app.shapesolution.vo.*;
import cn.iocoder.yudao.module.scan.convert.shapesolution.ShapeSolutionConvert;
import cn.iocoder.yudao.module.scan.dal.dataobject.shapesolution.ShapeSolutionDO;
import cn.iocoder.yudao.module.scan.service.shapesolution.ShapeSolutionService;
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

@Api(tags = "扫描APP - 体型解决方案")
@RestController
@RequestMapping("/scan/shape-solution")
@Validated
public class AppShapeSolutionController {

    @Resource
    private ShapeSolutionService shapeSolutionService;


    @GetMapping("/get")
    @ApiOperation("获得体型解决方案")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    public CommonResult<AppShapeSolutionRespVO> getShapeSolution(@RequestParam("id") Long id) {
        ShapeSolutionDO shapeSolution = shapeSolutionService.getShapeSolution(id);
        return success(ShapeSolutionConvert.INSTANCE.convert02(shapeSolution));
    }

    @GetMapping("/list")
    @ApiOperation("获得体型解决方案列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, dataTypeClass = List.class)
    public CommonResult<List<AppShapeSolutionRespVO>> getShapeSolutionList(@RequestParam("ids") Collection<Long> ids) {
        List<ShapeSolutionDO> list = shapeSolutionService.getShapeSolutionList(ids);
        return success(ShapeSolutionConvert.INSTANCE.convertList03(list));
    }

}
