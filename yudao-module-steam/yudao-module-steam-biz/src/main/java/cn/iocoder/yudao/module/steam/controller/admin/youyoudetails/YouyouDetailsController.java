package cn.iocoder.yudao.module.steam.controller.admin.youyoudetails;

import cn.iocoder.yudao.module.steam.dal.mysql.youyoudetails.YouyouDetailsMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoudetails.YouyouDetailsDO;
import cn.iocoder.yudao.module.steam.service.youyoudetails.YouyouDetailsService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "管理后台 - 用户查询明细")
@RestController
@RequestMapping("/steam/youyou-details")
@Validated
public class YouyouDetailsController {

    @Resource
    private YouyouDetailsService youyouDetailsService;

    @PostMapping("/create")
    @Operation(summary = "创建用户查询明细")
    @PreAuthorize("@ss.hasPermission('steam:youyou-details:create')")
    public CommonResult<Integer> createYouyouDetails(@Valid @RequestBody YouyouDetailsSaveReqVO createReqVO) {
        return success(youyouDetailsService.createYouyouDetails(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户查询明细")
    @PreAuthorize("@ss.hasPermission('steam:youyou-details:update')")
    public CommonResult<Boolean> updateYouyouDetails(@Valid @RequestBody YouyouDetailsSaveReqVO updateReqVO) {
        youyouDetailsService.updateYouyouDetails(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户查询明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('steam:youyou-details:delete')")
    public CommonResult<Boolean> deleteYouyouDetails(@RequestParam("id") Integer id) {
        youyouDetailsService.deleteYouyouDetails(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户查询明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('steam:youyou-details:query')")
    public CommonResult<YouyouDetailsRespVO> getYouyouDetails(@RequestParam("id") Integer id) {
        YouyouDetailsDO youyouDetails = youyouDetailsService.getYouyouDetails(id);
        return success(BeanUtils.toBean(youyouDetails, YouyouDetailsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户查询明细分页")
    @PreAuthorize("@ss.hasPermission('steam:youyou-details:query')")
    public CommonResult<PageResult<YouyouDetailsRespVO>> getYouyouDetailsPage(@Valid YouyouDetailsPageReqVO pageReqVO) {
        PageResult<YouyouDetailsDO> pageResult = youyouDetailsService.getYouyouDetailsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, YouyouDetailsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户查询明细 Excel")
    @PreAuthorize("@ss.hasPermission('steam:youyou-details:export')")
    @OperateLog(type = EXPORT)
    public void exportYouyouDetailsExcel(@Valid YouyouDetailsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<YouyouDetailsDO> list = youyouDetailsService.getYouyouDetailsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "用户查询明细.xls", "数据", YouyouDetailsRespVO.class,
                        BeanUtils.toBean(list, YouyouDetailsRespVO.class));
    }

}