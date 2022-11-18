package cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop;

import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.*;
import cn.iocoder.yudao.module.yr.convert.sys.sysshop.SysShopConvert;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.sysshop.SysShopDO;
import cn.iocoder.yudao.module.yr.service.sys.sysshop.SysShopService;
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


@Api(tags = "管理后台 - 店面")
@RestController
@RequestMapping("/qg/sys-shop")
@Validated
public class SysShopController {

    @Resource
    private SysShopService sysShopService;

    @PostMapping("/create")
    @ApiOperation("创建店面")
    @PreAuthorize("@ss.hasPermission('qg:sys-shop:create')")
    public CommonResult<Long> createSysShop(@Valid @RequestBody SysShopCreateReqVO createReqVO) {
        return success(sysShopService.createSysShop(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新店面")
    @PreAuthorize("@ss.hasPermission('qg:sys-shop:update')")
    public CommonResult<Boolean> updateSysShop(@Valid @RequestBody SysShopUpdateReqVO updateReqVO) {
        sysShopService.updateSysShop(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除店面")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('qg:sys-shop:delete')")
    public CommonResult<Boolean> deleteSysShop(@RequestParam("id") Long id) {
        sysShopService.deleteSysShop(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得店面")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('qg:sys-shop:query')")
    public CommonResult<SysShopRespVO> getSysShop(@RequestParam("id") Long id) {
        SysShopDO sysShop = sysShopService.getSysShop(id);
        return success(SysShopConvert.INSTANCE.convert(sysShop));
    }

    @GetMapping("/list")
    @ApiOperation("获得店面列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('qg:sys-shop:query')")
    public CommonResult<List<SysShopRespVO>> getSysShopList(@RequestParam("ids") Collection<Long> ids) {
        List<SysShopDO> list = sysShopService.getSysShopList(ids);
        return success(SysShopConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @ApiOperation("获得店面分页")
    @PreAuthorize("@ss.hasPermission('qg:sys-shop:query')")
    public CommonResult<PageResult<SysShopRespVO>> getSysShopPage(@Valid SysShopPageReqVO pageVO) {
        PageResult<SysShopDO> pageResult = sysShopService.getSysShopPage(pageVO);
        return success(SysShopConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出店面 Excel")
    @PreAuthorize("@ss.hasPermission('qg:sys-shop:export')")
    @OperateLog(type = EXPORT)
    public void exportSysShopExcel(@Valid SysShopExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<SysShopDO> list = sysShopService.getSysShopList(exportReqVO);
        // 导出 Excel
        List<SysShopExcelVO> datas = SysShopConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "店面.xls", "数据", SysShopExcelVO.class, datas);
    }

}
