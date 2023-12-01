package cn.iocoder.yudao.module.member.controller.app.serveraddress;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.*;
import cn.iocoder.yudao.module.member.controller.app.serveraddress.vo.ServerAddressAppCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.serveraddress.vo.ServerAddressAppExportReqVO;
import cn.iocoder.yudao.module.member.controller.app.serveraddress.vo.ServerAddressAppPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.serveraddress.vo.ServerAddressAppUpdateReqVO;
import cn.iocoder.yudao.module.member.convert.serveraddress.ServerAddressConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.serveraddress.ServerAddressDO;
import cn.iocoder.yudao.module.member.service.serveraddress.ServerAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 服务地址")
@RestController
@RequestMapping("/member/server-address")
@Validated
public class ServerAddressAppController {

    @Resource
    private ServerAddressService serverAddressService;

    @PostMapping("/create")
    @Operation(summary = "创建服务地址")
    @PreAuthorize("@ss.hasPermission('member:server-address:create')")
    public CommonResult<Long> createServerAddress(@Valid @RequestBody ServerAddressAppCreateReqVO createReqVO) {
        return success(serverAddressService.createServerAddress(ServerAddressConvert.INSTANCE.convert(createReqVO)));
    }

    @PutMapping("/update")
    @Operation(summary = "更新服务地址")
    @PreAuthorize("@ss.hasPermission('member:server-address:update')")
    public CommonResult<Boolean> updateServerAddress(@Valid @RequestBody ServerAddressAppUpdateReqVO updateReqVO) {
        serverAddressService.updateServerAddress(ServerAddressConvert.INSTANCE.convert(updateReqVO));
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
    public CommonResult<PageResult<ServerAddressRespVO>> getServerAddressPage(@Valid ServerAddressAppPageReqVO pageVO) {
        PageResult<ServerAddressDO> pageResult = serverAddressService.getServerAddressPage(ServerAddressConvert.INSTANCE.convert(pageVO));
        return success(ServerAddressConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出服务地址 Excel")
    @PreAuthorize("@ss.hasPermission('member:server-address:export')")
    @OperateLog(type = EXPORT)
    public void exportServerAddressExcel(@Valid ServerAddressAppExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ServerAddressDO> list = serverAddressService.getServerAddressList(ServerAddressConvert.INSTANCE.convert(exportReqVO));
        // 导出 Excel
        List<ServerAddressExcelVO> datas = ServerAddressConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "服务地址.xls", "数据", ServerAddressExcelVO.class, datas);
    }

}
