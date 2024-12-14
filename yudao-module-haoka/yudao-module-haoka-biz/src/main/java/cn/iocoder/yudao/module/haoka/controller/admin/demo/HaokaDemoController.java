package cn.iocoder.yudao.module.haoka.controller.admin.demo;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.haoka.controller.admin.demo.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.demo.HaokaDemoDO;
import cn.iocoder.yudao.module.haoka.service.demo.HaokaDemoService;

@Tag(name = "管理后台 - 好卡案例")
@RestController
@RequestMapping("/haoka/demo")
@Validated
public class HaokaDemoController {

    @Resource
    private HaokaDemoService demoService;

    @PostMapping("/create")
    @Operation(summary = "创建好卡案例")
    @PreAuthorize("@ss.hasPermission('haoka:demo:create')")
    public CommonResult<Long> createDemo(@Valid @RequestBody HaokaDemoSaveReqVO createReqVO) {
        return success(demoService.createDemo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新好卡案例")
    @PreAuthorize("@ss.hasPermission('haoka:demo:update')")
    public CommonResult<Boolean> updateDemo(@Valid @RequestBody HaokaDemoSaveReqVO updateReqVO) {
        demoService.updateDemo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除好卡案例")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('haoka:demo:delete')")
    public CommonResult<Boolean> deleteDemo(@RequestParam("id") Long id) {
        demoService.deleteDemo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得好卡案例")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('haoka:demo:query')")
    public CommonResult<HaokaDemoRespVO> getDemo(@RequestParam("id") Long id) {
        HaokaDemoDO demo = demoService.getDemo(id);
        return success(BeanUtils.toBean(demo, HaokaDemoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得好卡案例分页")
    @PreAuthorize("@ss.hasPermission('haoka:demo:query')")
    public CommonResult<PageResult<HaokaDemoRespVO>> getDemoPage(@Valid HaokaDemoPageReqVO pageReqVO) {
        PageResult<HaokaDemoDO> pageResult = demoService.getDemoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, HaokaDemoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出好卡案例 Excel")
    @PreAuthorize("@ss.hasPermission('haoka:demo:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDemoExcel(@Valid HaokaDemoPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<HaokaDemoDO> list = demoService.getDemoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "好卡案例.xls", "数据", HaokaDemoRespVO.class,
                        BeanUtils.toBean(list, HaokaDemoRespVO.class));
    }

}