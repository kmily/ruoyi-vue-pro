package cn.iocoder.yudao.module.radar.controller.admin.banner;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.radar.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.radar.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.module.radar.convert.banner.BannerConvert;
import cn.iocoder.yudao.module.radar.service.banner.BannerService;

@Tag(name = "管理后台 - 雷达模块banner图")
@RestController
@RequestMapping("/radar/banner")
@Validated
public class BannerController {

    @Resource
    private BannerService bannerService;

    @PostMapping("/create")
    @Operation(summary = "创建雷达模块banner图")
    @PreAuthorize("@ss.hasPermission('radar:banner:create')")
    public CommonResult<Long> createBanner(@Valid @RequestBody BannerCreateReqVO createReqVO) {
        return success(bannerService.createBanner(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新雷达模块banner图")
    @PreAuthorize("@ss.hasPermission('radar:banner:update')")
    public CommonResult<Boolean> updateBanner(@Valid @RequestBody BannerUpdateReqVO updateReqVO) {
        bannerService.updateBanner(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除雷达模块banner图")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('radar:banner:delete')")
    public CommonResult<Boolean> deleteBanner(@RequestParam("id") Long id) {
        bannerService.deleteBanner(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得雷达模块banner图")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('radar:banner:query')")
    public CommonResult<BannerRespVO> getBanner(@RequestParam("id") Long id) {
        BannerDO banner = bannerService.getBanner(id);
        return success(BannerConvert.INSTANCE.convert(banner));
    }

    @GetMapping("/list")
    @Operation(summary = "获得雷达模块banner图列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('radar:banner:query')")
    public CommonResult<List<BannerRespVO>> getBannerList(@RequestParam("ids") Collection<Long> ids) {
        List<BannerDO> list = bannerService.getBannerList(ids);
        return success(BannerConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得雷达模块banner图分页")
    @PreAuthorize("@ss.hasPermission('radar:banner:query')")
    public CommonResult<PageResult<BannerRespVO>> getBannerPage(@Valid BannerPageReqVO pageVO) {
        PageResult<BannerDO> pageResult = bannerService.getBannerPage(pageVO);
        return success(BannerConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出雷达模块banner图 Excel")
    @PreAuthorize("@ss.hasPermission('radar:banner:export')")
    @OperateLog(type = EXPORT)
    public void exportBannerExcel(@Valid BannerExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<BannerDO> list = bannerService.getBannerList(exportReqVO);
        // 导出 Excel
        List<BannerExcelVO> datas = BannerConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "雷达模块banner图.xls", "数据", BannerExcelVO.class, datas);
    }

}
