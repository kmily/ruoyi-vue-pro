package com.cw.module.trade.controller.admin.followrecord;

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

import com.cw.module.trade.controller.admin.followrecord.vo.*;
import com.cw.module.trade.dal.dataobject.followrecord.FollowRecordDO;
import com.cw.module.trade.convert.followrecord.FollowRecordConvert;
import com.cw.module.trade.service.followrecord.FollowRecordService;

@Tag(name = "管理后台 - 账号跟随记录")
@RestController
@RequestMapping("/account/follow-record")
@Validated
public class FollowRecordController {

    @Resource
    private FollowRecordService followRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建账号跟随记录")
    @PreAuthorize("@ss.hasPermission('account:follow-record:create')")
    public CommonResult<Long> createFollowRecord(@Valid @RequestBody FollowRecordCreateReqVO createReqVO) {
        return success(followRecordService.createFollowRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新账号跟随记录")
    @PreAuthorize("@ss.hasPermission('account:follow-record:update')")
    public CommonResult<Boolean> updateFollowRecord(@Valid @RequestBody FollowRecordUpdateReqVO updateReqVO) {
        followRecordService.updateFollowRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除账号跟随记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('account:follow-record:delete')")
    public CommonResult<Boolean> deleteFollowRecord(@RequestParam("id") Long id) {
        followRecordService.deleteFollowRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得账号跟随记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('account:follow-record:query')")
    public CommonResult<FollowRecordRespVO> getFollowRecord(@RequestParam("id") Long id) {
        FollowRecordDO followRecord = followRecordService.getFollowRecord(id);
        return success(FollowRecordConvert.INSTANCE.convert(followRecord));
    }

    @GetMapping("/list")
    @Operation(summary = "获得账号跟随记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('account:follow-record:query')")
    public CommonResult<List<FollowRecordRespVO>> getFollowRecordList(@RequestParam("ids") Collection<Long> ids) {
        List<FollowRecordDO> list = followRecordService.getFollowRecordList(ids);
        return success(FollowRecordConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得账号跟随记录分页")
    @PreAuthorize("@ss.hasPermission('account:follow-record:query')")
    public CommonResult<PageResult<FollowRecordRespVO>> getFollowRecordPage(@Valid FollowRecordPageReqVO pageVO) {
        PageResult<FollowRecordDO> pageResult = followRecordService.getFollowRecordPage(pageVO);
        return success(FollowRecordConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出账号跟随记录 Excel")
    @PreAuthorize("@ss.hasPermission('account:follow-record:export')")
    @OperateLog(type = EXPORT)
    public void exportFollowRecordExcel(@Valid FollowRecordExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<FollowRecordDO> list = followRecordService.getFollowRecordList(exportReqVO);
        // 导出 Excel
        List<FollowRecordExcelVO> datas = FollowRecordConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "账号跟随记录.xls", "数据", FollowRecordExcelVO.class, datas);
    }

}
