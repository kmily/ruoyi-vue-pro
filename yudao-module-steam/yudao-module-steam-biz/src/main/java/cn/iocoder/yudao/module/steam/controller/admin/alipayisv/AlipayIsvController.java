package cn.iocoder.yudao.module.steam.controller.admin.alipayisv;

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

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.steam.controller.admin.alipayisv.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import cn.iocoder.yudao.module.steam.service.alipayisv.AlipayIsvService;

@Tag(name = "管理后台 - 签约ISV用户")
@RestController
@RequestMapping("/steam/alipay-isv")
@Validated
public class AlipayIsvController {

    @Resource
    private AlipayIsvService alipayIsvService;

    @PostMapping("/create")
    @Operation(summary = "创建签约ISV用户")
    @PreAuthorize("@ss.hasPermission('steam:alipay-isv:create')")
    public CommonResult<Long> createAlipayIsv(@Valid @RequestBody AlipayIsvSaveReqVO createReqVO) {
        return success(alipayIsvService.createAlipayIsv(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新签约ISV用户")
    @PreAuthorize("@ss.hasPermission('steam:alipay-isv:update')")
    public CommonResult<Boolean> updateAlipayIsv(@Valid @RequestBody AlipayIsvSaveReqVO updateReqVO) {
        alipayIsvService.updateAlipayIsv(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除签约ISV用户")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:alipay-isv:delete')")
    public CommonResult<Boolean> deleteAlipayIsv(@RequestParam("id") Long id) {
        alipayIsvService.deleteAlipayIsv(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得签约ISV用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:alipay-isv:query')")
    public CommonResult<AlipayIsvRespVO> getAlipayIsv(@RequestParam("id") Long id) {
        AlipayIsvDO alipayIsv = alipayIsvService.getAlipayIsv(id);
        return success(BeanUtils.toBean(alipayIsv, AlipayIsvRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得签约ISV用户分页")
    @PreAuthorize("@ss.hasPermission('steam:alipay-isv:query')")
    public CommonResult<PageResult<AlipayIsvRespVO>> getAlipayIsvPage(@Valid AlipayIsvPageReqVO pageReqVO) {
        PageResult<AlipayIsvDO> pageResult = alipayIsvService.getAlipayIsvPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AlipayIsvRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出签约ISV用户 Excel")
    @PreAuthorize("@ss.hasPermission('steam:alipay-isv:export')")
    @OperateLog(type = EXPORT)
    public void exportAlipayIsvExcel(@Valid AlipayIsvPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AlipayIsvDO> list = alipayIsvService.getAlipayIsvPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "签约ISV用户.xls", "数据", AlipayIsvRespVO.class,
                        BeanUtils.toBean(list, AlipayIsvRespVO.class));
    }

}