package cn.iocoder.yudao.module.system.controller.admin.area;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.service.area.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import cn.iocoder.yudao.module.system.controller.admin.area.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.area.AreaDO;
import cn.iocoder.yudao.module.system.convert.area.AreaConvert;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Api(tags = "管理后台 - 行政区域")
@RestController
@RequestMapping("/system/area")
@Validated
public class AreaController {

    @Resource
    private AreaService areaService;

    @PostMapping("init")
    @ApiOperation("初始化行政区(此方法耗时较长建议后台运行)")
//    @PreAuthorize("@ss.hasPermission('system:area:create')")
    public CommonResult<Boolean> init() {
        Boolean flag = areaService.initArea();
        return success(flag);
    }


    @PutMapping("/update")
    @ApiOperation("更新行政区域")
    @PreAuthorize("@ss.hasPermission('system:area:update')")
    public CommonResult<Boolean> updateArea(@Valid @RequestBody AreaUpdateReqVO updateReqVO) {
        areaService.updateArea(updateReqVO);
        return success(true);
    }



    @GetMapping("/page")
    @ApiOperation("获得行政区域分页")
    @PreAuthorize("@ss.hasPermission('system:area:query')")
    public CommonResult<PageResult<AreaRespVO>> getAreaPage(@Valid AreaPageReqVO pageVO) {
        PageResult<AreaDO> pageResult = areaService.getAreaPage(pageVO);
        return success(AreaConvert.INSTANCE.convertPage(pageResult));
    }


    /**
     * 获得全国所有省份
     *
     * @return 全国所有省份
     */
    @PostMapping("/provinces")
    @ApiOperation("获得全国所有省份直辖市")
    @ApiImplicitParam(name = "provinces", value = "获得全国所有省份", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('system:area:query')")
    public CommonResult<List<AreaRespVO>> provinces(@RequestBody  AreaRespVO areaRespVO) {
        List<AreaDO> list = areaService.provinces(areaRespVO);
        return success(AreaConvert.INSTANCE.convertList(list));
    }

    /**
     * 获得省份直辖市下的所有城市
     *
     * @return 获得省份直辖市下的所有城市
     */
    @PostMapping("/cities")
    @ApiOperation("获得省份直辖市下的所有城市")
    @ApiImplicitParam(name = "cities", value = "获得省份直辖市下的所有城市", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('system:area:query')")
    public CommonResult<List<AreaRespVO>> cities(@RequestBody  AreaRespVO areaRespVO) {
        List<AreaDO> list = areaService.cities(areaRespVO);
        return success(AreaConvert.INSTANCE.convertList(list));
    }

    /**
     * 获得城市下的所有区县
     *
     * @return 获得城市下的所有区县
     */
    @PostMapping("/counties")
    @ApiOperation("获得城市下的所有区县")
    @ApiImplicitParam(name = "counties", value = "获得城市下的所有区县", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('system:area:query')")
    public CommonResult<List<AreaRespVO>> counties(@RequestBody AreaRespVO areaRespVO) {
        List<AreaDO> list = areaService.counties(areaRespVO);
        return success(AreaConvert.INSTANCE.convertList(list));
    }

    /**
     * 获得区县下的所有街道
     *
     * @return 获得区县下的所有街道
     */
    @PostMapping("/towns")
    @ApiOperation("获得区县下的所有街道")
    @ApiImplicitParam(name = "towns", value = "获得区县下的所有街道", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('system:area:query')")
    public CommonResult<List<AreaRespVO>> towns(@RequestBody AreaRespVO areaRespVO) {
        List<AreaDO> list = areaService.towns(areaRespVO);
        return success(AreaConvert.INSTANCE.convertList(list));
    }

    /**
     * 获得街道的所有社区村
     *
     * @return 获得街道的所有社区村
     */
    @PostMapping("/villages")
    @ApiOperation("获得街道的所有社区村")
    @ApiImplicitParam(name = "villages", value = "获得街道的所有社区村", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('system:area:query')")
    public CommonResult<List<AreaRespVO>> villages(@RequestBody AreaRespVO areaRespVO) {
        List<AreaDO> list = areaService.villages(areaRespVO);
        return success(AreaConvert.INSTANCE.convertList(list));
    }

}
