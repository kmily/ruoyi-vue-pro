package cn.iocoder.yudao.module.im.controller.admin.group;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.im.controller.admin.group.vo.*;
import cn.iocoder.yudao.module.im.dal.dataobject.group.GroupDO;
import cn.iocoder.yudao.module.im.service.group.ImGroupService;

@Tag(name = "管理后台 - 群")
@RestController
@RequestMapping("/im/group")
@Validated
public class ImGroupController {

    @Resource
    private ImGroupService imGroupService;

    @PostMapping("/create")
    @Operation(summary = "创建群")
    @PreAuthorize("@ss.hasPermission('im:group:create')")
    public CommonResult<Long> createGroup(@Valid @RequestBody ImGroupSaveReqVO createReqVO) {
        return success(imGroupService.createGroup(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新群")
    @PreAuthorize("@ss.hasPermission('im:group:update')")
    public CommonResult<Boolean> updateGroup(@Valid @RequestBody ImGroupSaveReqVO updateReqVO) {
        imGroupService.updateGroup(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除群")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('im:group:delete')")
    public CommonResult<Boolean> deleteGroup(@RequestParam("id") Long id) {
        imGroupService.deleteGroup(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得群")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('im:group:query')")
    public CommonResult<ImGroupRespVO> getGroup(@RequestParam("id") Long id) {
        GroupDO group = imGroupService.getGroup(id);
        return success(BeanUtils.toBean(group, ImGroupRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得群分页")
    @PreAuthorize("@ss.hasPermission('im:group:query')")
    public CommonResult<PageResult<ImGroupRespVO>> getGroupPage(@Valid ImGroupPageReqVO pageReqVO) {
        PageResult<GroupDO> pageResult = imGroupService.getGroupPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ImGroupRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出群 Excel")
    @PreAuthorize("@ss.hasPermission('im:group:export')")
    @OperateLog(type = EXPORT)
    public void exportGroupExcel(@Valid ImGroupPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GroupDO> list = imGroupService.getGroupPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "群.xls", "数据", ImGroupRespVO.class,
                        BeanUtils.toBean(list, ImGroupRespVO.class));
    }

}