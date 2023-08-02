package cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity;

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

import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.*;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargainactivity.BargainActivityDO;
import cn.iocoder.yudao.module.promotion.convert.bargainactivity.BargainActivityConvert;
import cn.iocoder.yudao.module.promotion.service.bargainactivity.BargainActivityService;

@Tag(name = "管理后台 - 砍价")
@RestController
@RequestMapping("/promotion/bargain-activity")
@Validated
public class BargainActivityController {

    @Resource
    private BargainActivityService bargainActivityService;

    @PostMapping("/create")
    @Operation(summary = "创建砍价")
    @PreAuthorize("@ss.hasPermission('promotion:bargain-activity:create')")
    public CommonResult<Long> createBargainActivity(@Valid @RequestBody BargainActivityCreateReqVO createReqVO) {
        return success(bargainActivityService.createBargainActivity(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新砍价")
    @PreAuthorize("@ss.hasPermission('promotion:bargain-activity:update')")
    public CommonResult<Boolean> updateBargainActivity(@Valid @RequestBody BargainActivityUpdateReqVO updateReqVO) {
        bargainActivityService.updateBargainActivity(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除砍价")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:bargain-activity:delete')")
    public CommonResult<Boolean> deleteBargainActivity(@RequestParam("id") Long id) {
        bargainActivityService.deleteBargainActivity(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得砍价")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('promotion:bargain-activity:query')")
    public CommonResult<BargainActivityRespVO> getBargainActivity(@RequestParam("id") Long id) {
        BargainActivityDO bargainActivity = bargainActivityService.getBargainActivity(id);
        return success(BargainActivityConvert.INSTANCE.convert(bargainActivity));
    }

    @GetMapping("/list")
    @Operation(summary = "获得砍价列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('promotion:bargain-activity:query')")
    public CommonResult<List<BargainActivityRespVO>> getBargainActivityList(@RequestParam("ids") Collection<Long> ids) {
        List<BargainActivityDO> list = bargainActivityService.getBargainActivityList(ids);
        return success(BargainActivityConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得砍价分页")
    @PreAuthorize("@ss.hasPermission('promotion:bargain-activity:query')")
    public CommonResult<PageResult<BargainActivityRespVO>> getBargainActivityPage(@Valid BargainActivityPageReqVO pageVO) {
        PageResult<BargainActivityDO> pageResult = bargainActivityService.getBargainActivityPage(pageVO);
        return success(BargainActivityConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出砍价 Excel")
    @PreAuthorize("@ss.hasPermission('promotion:bargain-activity:export')")
    @OperateLog(type = EXPORT)
    public void exportBargainActivityExcel(@Valid BargainActivityExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<BargainActivityDO> list = bargainActivityService.getBargainActivityList(exportReqVO);
        // 导出 Excel
        List<BargainActivityExcelVO> datas = BargainActivityConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "砍价.xls", "数据", BargainActivityExcelVO.class, datas);
    }

}
