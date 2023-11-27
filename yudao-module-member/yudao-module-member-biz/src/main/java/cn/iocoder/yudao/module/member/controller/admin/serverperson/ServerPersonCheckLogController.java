package cn.iocoder.yudao.module.member.controller.admin.serverperson;

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

import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonCheckLogDO;
import cn.iocoder.yudao.module.member.convert.serverperson.ServerPersonCheckLogConvert;
import cn.iocoder.yudao.module.member.service.serverperson.ServerPersonCheckLogService;

@Tag(name = "管理后台 - 被户人审核记录")
@RestController
@RequestMapping("/member/server-person-check-log")
@Validated
public class ServerPersonCheckLogController {

    @Resource
    private ServerPersonCheckLogService serverPersonCheckLogService;


    @GetMapping("/get")
    @Operation(summary = "获得被户人审核记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:server-person-check-log:query')")
    public CommonResult<ServerPersonCheckLogRespVO> getServerPersonCheckLog(@RequestParam("id") Long id) {
        ServerPersonCheckLogDO serverPersonCheckLog = serverPersonCheckLogService.getServerPersonCheckLog(id);
        return success(ServerPersonCheckLogConvert.INSTANCE.convert(serverPersonCheckLog));
    }

    @GetMapping("/list")
    @Operation(summary = "获得被户人审核记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:server-person-check-log:query')")
    public CommonResult<List<ServerPersonCheckLogRespVO>> getServerPersonCheckLogList(@RequestParam("ids") Collection<Long> ids) {
        List<ServerPersonCheckLogDO> list = serverPersonCheckLogService.getServerPersonCheckLogList(ids);
        return success(ServerPersonCheckLogConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得被户人审核记录分页")
    @PreAuthorize("@ss.hasPermission('member:server-person-check-log:query')")
    public CommonResult<PageResult<ServerPersonCheckLogRespVO>> getServerPersonCheckLogPage(@Valid ServerPersonCheckLogPageReqVO pageVO) {
        PageResult<ServerPersonCheckLogDO> pageResult = serverPersonCheckLogService.getServerPersonCheckLogPage(pageVO);
        return success(ServerPersonCheckLogConvert.INSTANCE.convertPage(pageResult));
    }

}
