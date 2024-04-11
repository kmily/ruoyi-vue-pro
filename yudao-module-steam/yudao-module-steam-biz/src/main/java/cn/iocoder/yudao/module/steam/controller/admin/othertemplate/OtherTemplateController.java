package cn.iocoder.yudao.module.steam.controller.admin.othertemplate;

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

import cn.iocoder.yudao.module.steam.controller.admin.othertemplate.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.othertemplate.OtherTemplateDO;
import cn.iocoder.yudao.module.steam.service.othertemplate.OtherTemplateService;

@Tag(name = "管理后台 - 其他平台模板")
@RestController
@RequestMapping("/steam/other-template")
@Validated
public class OtherTemplateController {

    @Resource
    private OtherTemplateService otherTemplateService;

    @PostMapping("/create")
    @Operation(summary = "创建其他平台模板")
    @PreAuthorize("@ss.hasPermission('steam:other-template:create')")
    public CommonResult<Integer> createOtherTemplate(@Valid @RequestBody OtherTemplateSaveReqVO createReqVO) {
        return success(otherTemplateService.createOtherTemplate(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新其他平台模板")
    @PreAuthorize("@ss.hasPermission('steam:other-template:update')")
    public CommonResult<Boolean> updateOtherTemplate(@Valid @RequestBody OtherTemplateSaveReqVO updateReqVO) {
        otherTemplateService.updateOtherTemplate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除其他平台模板")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:other-template:delete')")
    public CommonResult<Boolean> deleteOtherTemplate(@RequestParam("id") Integer id) {
        otherTemplateService.deleteOtherTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得其他平台模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:other-template:query')")
    public CommonResult<OtherTemplateRespVO> getOtherTemplate(@RequestParam("id") Integer id) {
        OtherTemplateDO otherTemplate = otherTemplateService.getOtherTemplate(id);
        return success(BeanUtils.toBean(otherTemplate, OtherTemplateRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得其他平台模板分页")
    @PreAuthorize("@ss.hasPermission('steam:other-template:query')")
    public CommonResult<PageResult<OtherTemplateRespVO>> getOtherTemplatePage(@Valid OtherTemplatePageReqVO pageReqVO) {
        PageResult<OtherTemplateDO> pageResult = otherTemplateService.getOtherTemplatePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OtherTemplateRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出其他平台模板 Excel")
    @PreAuthorize("@ss.hasPermission('steam:other-template:export')")
    @OperateLog(type = EXPORT)
    public void exportOtherTemplateExcel(@Valid OtherTemplatePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OtherTemplateDO> list = otherTemplateService.getOtherTemplatePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "其他平台模板.xls", "数据", OtherTemplateRespVO.class,
                        BeanUtils.toBean(list, OtherTemplateRespVO.class));
    }

}