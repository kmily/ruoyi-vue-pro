package cn.iocoder.yudao.module.scan.controller.admin.appversion;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.*;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.scan.controller.admin.appversion.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.appversion.AppVersionDO;
import cn.iocoder.yudao.module.scan.convert.appversion.AppVersionConvert;
import cn.iocoder.yudao.module.scan.service.appversion.AppVersionService;

@Api(tags = "管理后台 - 应用版本记录")
@RestController
@RequestMapping("/scan/app-version")
@Validated
public class AppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @PostMapping("/create")
    @ApiOperation("创建应用版本记录")
    @PreAuthorize("@ss.hasPermission('scan:app-version:create')")
    public CommonResult<Long> createAppVersion(@Valid @RequestBody AppVersionCreateReqVO createReqVO) {
        return success(appVersionService.createAppVersion(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新应用版本记录")
    @PreAuthorize("@ss.hasPermission('scan:app-version:update')")
    public CommonResult<Boolean> updateAppVersion(@Valid @RequestBody AppVersionUpdateReqVO updateReqVO) {
        appVersionService.updateAppVersion(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除应用版本记录")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:app-version:delete')")
    public CommonResult<Boolean> deleteAppVersion(@RequestParam("id") Long id) {
        appVersionService.deleteAppVersion(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得应用版本记录")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('scan:app-version:query')")
    public CommonResult<AppVersionRespVO> getAppVersion(@RequestParam("id") Long id) {
        AppVersionDO appVersion = appVersionService.getAppVersion(id);
        return success(AppVersionConvert.INSTANCE.convert(appVersion));
    }

    @GetMapping("/list")
    @ApiOperation("获得应用版本记录列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('scan:app-version:query')")
    public CommonResult<List<AppVersionRespVO>> getAppVersionList(@RequestParam("ids") Collection<Long> ids) {
        List<AppVersionDO> list = appVersionService.getAppVersionList(ids);
        return success(AppVersionConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得应用版本记录分页")
    @PreAuthorize("@ss.hasPermission('scan:app-version:query')")
    public CommonResult<PageResult<AppVersionRespVO>> getAppVersionPage(@Valid AppVersionPageReqVO pageVO) {
        PageResult<AppVersionDO> pageResult = appVersionService.getAppVersionPage(pageVO);
        return success(AppVersionConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出应用版本记录 Excel")
    @PreAuthorize("@ss.hasPermission('scan:app-version:export')")
    @OperateLog(type = EXPORT)
    public void exportAppVersionExcel(@Valid AppVersionExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<AppVersionDO> list = appVersionService.getAppVersionList(exportReqVO);
        // 导出 Excel
        List<AppVersionExcelVO> datas = AppVersionConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "应用版本记录.xls", "数据", AppVersionExcelVO.class, datas);
    }

}
