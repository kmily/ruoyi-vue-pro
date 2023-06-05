package com.cw.module.trade.controller.admin.syncrecord;

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

import com.cw.module.trade.controller.admin.syncrecord.vo.*;
import com.cw.module.trade.dal.dataobject.syncrecord.SyncRecordDO;
import com.cw.module.trade.convert.syncrecord.SyncRecordConvert;
import com.cw.module.trade.service.syncrecord.SyncRecordService;

@Tag(name = "管理后台 - 账号同步记录")
@RestController
@RequestMapping("/account/sync-record")
@Validated
public class SyncRecordController {

    @Resource
    private SyncRecordService syncRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建账号同步记录")
    @PreAuthorize("@ss.hasPermission('account:sync-record:create')")
    public CommonResult<Long> createSyncRecord(@Valid @RequestBody SyncRecordCreateReqVO createReqVO) {
        return success(syncRecordService.createSyncRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新账号同步记录")
    @PreAuthorize("@ss.hasPermission('account:sync-record:update')")
    public CommonResult<Boolean> updateSyncRecord(@Valid @RequestBody SyncRecordUpdateReqVO updateReqVO) {
        syncRecordService.updateSyncRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除账号同步记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('account:sync-record:delete')")
    public CommonResult<Boolean> deleteSyncRecord(@RequestParam("id") Long id) {
        syncRecordService.deleteSyncRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得账号同步记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('account:sync-record:query')")
    public CommonResult<SyncRecordRespVO> getSyncRecord(@RequestParam("id") Long id) {
        SyncRecordDO syncRecord = syncRecordService.getSyncRecord(id);
        return success(SyncRecordConvert.INSTANCE.convert(syncRecord));
    }

    @GetMapping("/list")
    @Operation(summary = "获得账号同步记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('account:sync-record:query')")
    public CommonResult<List<SyncRecordRespVO>> getSyncRecordList(@RequestParam("ids") Collection<Long> ids) {
        List<SyncRecordDO> list = syncRecordService.getSyncRecordList(ids);
        return success(SyncRecordConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得账号同步记录分页")
    @PreAuthorize("@ss.hasPermission('account:sync-record:query')")
    public CommonResult<PageResult<SyncRecordRespVO>> getSyncRecordPage(@Valid SyncRecordPageReqVO pageVO) {
        PageResult<SyncRecordDO> pageResult = syncRecordService.getSyncRecordPage(pageVO);
        return success(SyncRecordConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出账号同步记录 Excel")
    @PreAuthorize("@ss.hasPermission('account:sync-record:export')")
    @OperateLog(type = EXPORT)
    public void exportSyncRecordExcel(@Valid SyncRecordExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<SyncRecordDO> list = syncRecordService.getSyncRecordList(exportReqVO);
        // 导出 Excel
        List<SyncRecordExcelVO> datas = SyncRecordConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "账号同步记录.xls", "数据", SyncRecordExcelVO.class, datas);
    }

}
