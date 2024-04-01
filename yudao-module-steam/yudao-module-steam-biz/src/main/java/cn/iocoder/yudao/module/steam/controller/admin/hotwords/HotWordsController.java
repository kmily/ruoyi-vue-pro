package cn.iocoder.yudao.module.steam.controller.admin.hotwords;

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

import cn.iocoder.yudao.module.steam.controller.admin.hotwords.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.hotwords.HotWordsDO;
import cn.iocoder.yudao.module.steam.service.hotwords.HotWordsService;

@Tag(name = "管理后台 - 热词搜索")
@RestController
@RequestMapping("/steam/hot-words")
@Validated
public class HotWordsController {

    @Resource
    private HotWordsService hotWordsService;

    @PostMapping("/create")
    @Operation(summary = "创建热词搜索")
    @PreAuthorize("@ss.hasPermission('steam:hot-words:create')")
    public CommonResult<Integer> createHotWords(@Valid @RequestBody HotWordsSaveReqVO createReqVO) {
        return success(hotWordsService.createHotWords(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新热词搜索")
    @PreAuthorize("@ss.hasPermission('steam:hot-words:update')")
    public CommonResult<Boolean> updateHotWords(@Valid @RequestBody HotWordsSaveReqVO updateReqVO) {
        hotWordsService.updateHotWords(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除热词搜索")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:hot-words:delete')")
    public CommonResult<Boolean> deleteHotWords(@RequestParam("id") Integer id) {
        hotWordsService.deleteHotWords(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得热词搜索")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:hot-words:query')")
    public CommonResult<HotWordsRespVO> getHotWords(@RequestParam("id") Integer id) {
        HotWordsDO hotWords = hotWordsService.getHotWords(id);
        return success(BeanUtils.toBean(hotWords, HotWordsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得热词搜索分页")
    @PreAuthorize("@ss.hasPermission('steam:hot-words:query')")
    public CommonResult<PageResult<HotWordsRespVO>> getHotWordsPage(@Valid HotWordsPageReqVO pageReqVO) {
        PageResult<HotWordsDO> pageResult = hotWordsService.getHotWordsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, HotWordsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出热词搜索 Excel")
    @PreAuthorize("@ss.hasPermission('steam:hot-words:export')")
    @OperateLog(type = EXPORT)
    public void exportHotWordsExcel(@Valid HotWordsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<HotWordsDO> list = hotWordsService.getHotWordsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "热词搜索.xls", "数据", HotWordsRespVO.class,
                        BeanUtils.toBean(list, HotWordsRespVO.class));
    }

}