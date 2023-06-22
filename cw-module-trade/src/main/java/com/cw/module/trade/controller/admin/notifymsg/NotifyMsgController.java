package com.cw.module.trade.controller.admin.notifymsg;

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

import com.cw.module.trade.controller.admin.notifymsg.vo.*;
import com.cw.module.trade.dal.dataobject.notifymsg.NotifyMsgDO;
import com.cw.module.trade.convert.notifymsg.NotifyMsgConvert;
import com.cw.module.trade.service.notifymsg.NotifyMsgService;

@Tag(name = "管理后台 - 账号通知记录")
@RestController
@RequestMapping("/account/notify-msg")
@Validated
public class NotifyMsgController {

    @Resource
    private NotifyMsgService notifyMsgService;

    @PostMapping("/create")
    @Operation(summary = "创建账号通知记录")
    @PreAuthorize("@ss.hasPermission('account:notify-msg:create')")
    public CommonResult<Long> createNotifyMsg(@Valid @RequestBody NotifyMsgCreateReqVO createReqVO) {
        return success(notifyMsgService.createNotifyMsg(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新账号通知记录")
    @PreAuthorize("@ss.hasPermission('account:notify-msg:update')")
    public CommonResult<Boolean> updateNotifyMsg(@Valid @RequestBody NotifyMsgUpdateReqVO updateReqVO) {
        notifyMsgService.updateNotifyMsg(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除账号通知记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('account:notify-msg:delete')")
    public CommonResult<Boolean> deleteNotifyMsg(@RequestParam("id") Long id) {
        notifyMsgService.deleteNotifyMsg(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得账号通知记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('account:notify-msg:query')")
    public CommonResult<NotifyMsgRespVO> getNotifyMsg(@RequestParam("id") Long id) {
        NotifyMsgDO notifyMsg = notifyMsgService.getNotifyMsg(id);
        return success(NotifyMsgConvert.INSTANCE.convert(notifyMsg));
    }

    @GetMapping("/list")
    @Operation(summary = "获得账号通知记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('account:notify-msg:query')")
    public CommonResult<List<NotifyMsgRespVO>> getNotifyMsgList(@RequestParam("ids") Collection<Long> ids) {
        List<NotifyMsgDO> list = notifyMsgService.getNotifyMsgList(ids);
        return success(NotifyMsgConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得账号通知记录分页")
    @PreAuthorize("@ss.hasPermission('account:notify-msg:query')")
    public CommonResult<PageResult<NotifyMsgRespVO>> getNotifyMsgPage(@Valid NotifyMsgPageReqVO pageVO) {
        PageResult<NotifyMsgDO> pageResult = notifyMsgService.getNotifyMsgPage(pageVO);
        return success(NotifyMsgConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出账号通知记录 Excel")
    @PreAuthorize("@ss.hasPermission('account:notify-msg:export')")
    @OperateLog(type = EXPORT)
    public void exportNotifyMsgExcel(@Valid NotifyMsgExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<NotifyMsgDO> list = notifyMsgService.getNotifyMsgList(exportReqVO);
        // 导出 Excel
        List<NotifyMsgExcelVO> datas = NotifyMsgConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "账号通知记录.xls", "数据", NotifyMsgExcelVO.class, datas);
    }

}
