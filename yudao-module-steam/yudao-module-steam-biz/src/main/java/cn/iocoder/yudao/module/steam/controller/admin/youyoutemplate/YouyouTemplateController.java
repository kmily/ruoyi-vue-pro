package cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate;

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

import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.service.youyoutemplate.YouyouTemplateService;

@Tag(name = "管理后台 - 悠悠商品数据")
@RestController
@RequestMapping("/steam/youyou-template")
@Validated
public class YouyouTemplateController {

    @Resource
    private YouyouTemplateService youyouTemplateService;

    @PostMapping("/create")
    @Operation(summary = "创建悠悠商品数据")
    @PreAuthorize("@ss.hasPermission('steam:youyou-template:create')")
    public CommonResult<Integer> createYouyouTemplate(@Valid @RequestBody YouyouTemplateSaveReqVO createReqVO) {
        return success(youyouTemplateService.createYouyouTemplate(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新悠悠商品数据")
    @PreAuthorize("@ss.hasPermission('steam:youyou-template:update')")
    public CommonResult<Boolean> updateYouyouTemplate(@Valid @RequestBody YouyouTemplateSaveReqVO updateReqVO) {
        youyouTemplateService.updateYouyouTemplate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除悠悠商品数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:youyou-template:delete')")
    public CommonResult<Boolean> deleteYouyouTemplate(@RequestParam("id") Integer id) {
        youyouTemplateService.deleteYouyouTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得悠悠商品数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:youyou-template:query')")
    public CommonResult<YouyouTemplateRespVO> getYouyouTemplate(@RequestParam("id") Integer id) {
        YouyouTemplateDO youyouTemplate = youyouTemplateService.getYouyouTemplate(id);
        return success(BeanUtils.toBean(youyouTemplate, YouyouTemplateRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得悠悠商品数据分页")
    @PreAuthorize("@ss.hasPermission('steam:youyou-template:query')")
    public CommonResult<PageResult<YouyouTemplateRespVO>> getYouyouTemplatePage(@Valid YouyouTemplatePageReqVO pageReqVO) {
        PageResult<YouyouTemplateDO> pageResult = youyouTemplateService.getYouyouTemplatePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, YouyouTemplateRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出悠悠商品数据 Excel")
    @PreAuthorize("@ss.hasPermission('steam:youyou-template:export')")
    @OperateLog(type = EXPORT)
    public void exportYouyouTemplateExcel(@Valid YouyouTemplatePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<YouyouTemplateDO> list = youyouTemplateService.getYouyouTemplatePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "悠悠商品数据.xls", "数据", YouyouTemplateRespVO.class,
                        BeanUtils.toBean(list, YouyouTemplateRespVO.class));
    }

}