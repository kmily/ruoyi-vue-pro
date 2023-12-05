package cn.iocoder.yudao.module.member.controller.admin.serveraddress;

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

import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serveraddress.ServerAddressDO;
import cn.iocoder.yudao.module.member.convert.serveraddress.ServerAddressConvert;
import cn.iocoder.yudao.module.member.service.serveraddress.ServerAddressService;

@Tag(name = "管理后台 - 服务地址")
@RestController
@RequestMapping("/member/server-address")
@Validated
public class ServerAddressController {

    @Resource
    private ServerAddressService serverAddressService;

    @PostMapping("/create")
    @Operation(summary = "创建服务地址")
    @PreAuthorize("@ss.hasPermission('member:server-address:create')")
    public CommonResult<Long> createServerAddress(@Valid @RequestBody ServerAddressCreateReqVO createReqVO) {
        return success(serverAddressService.createServerAddress(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新服务地址")
    @PreAuthorize("@ss.hasPermission('member:server-address:update')")
    public CommonResult<Boolean> updateServerAddress(@Valid @RequestBody ServerAddressUpdateReqVO updateReqVO) {
        serverAddressService.updateServerAddress(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除服务地址")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:server-address:delete')")
    public CommonResult<Boolean> deleteServerAddress(@RequestParam("id") Long id) {
        serverAddressService.deleteServerAddress(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得服务地址")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:server-address:query')")
    public CommonResult<ServerAddressRespVO> getServerAddress(@RequestParam("id") Long id) {
        ServerAddressDO serverAddress = serverAddressService.getServerAddress(id);
        return success(ServerAddressConvert.INSTANCE.convert(serverAddress));
    }

    @GetMapping("/list")
    @Operation(summary = "获得服务地址列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:server-address:query')")
    public CommonResult<List<ServerAddressRespVO>> getServerAddressList(@RequestParam("ids") Collection<Long> ids) {
        List<ServerAddressDO> list = serverAddressService.getServerAddressList(ids);
        return success(ServerAddressConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得服务地址分页")
    @PreAuthorize("@ss.hasPermission('member:server-address:query')")
    public CommonResult<PageResult<ServerAddressRespVO>> getServerAddressPage(@Valid ServerAddressPageReqVO pageVO) {
        PageResult<ServerAddressDO> pageResult = serverAddressService.getServerAddressPage(pageVO);
        return success(ServerAddressConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出服务地址 Excel")
    @PreAuthorize("@ss.hasPermission('member:server-address:export')")
    @OperateLog(type = EXPORT)
    public void exportServerAddressExcel(@Valid ServerAddressExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ServerAddressDO> list = serverAddressService.getServerAddressList(exportReqVO);
        // 导出 Excel
        List<ServerAddressExcelVO> datas = ServerAddressConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "服务地址.xls", "数据", ServerAddressExcelVO.class, datas);
    }

    /**
     * 会员详情-获取会员的服务地址
     * @param userId
     * @return
     */
    @GetMapping("/queryByUserId/list")
    @Operation(summary = "获得用户服务地址列表")
    @Parameter(name = "userId", description = "用户编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:server-address:query')")
    public CommonResult<List<ServerAddressRespVO>> getAddressListByUserId(@RequestParam("userId") Long userId) {
        List<ServerAddressDO> list = serverAddressService.getAddressListByUserId(userId);
        return success(ServerAddressConvert.INSTANCE.convertList(list));
    }


}
