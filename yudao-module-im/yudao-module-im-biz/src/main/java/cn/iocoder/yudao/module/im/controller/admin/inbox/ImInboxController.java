package cn.iocoder.yudao.module.im.controller.admin.inbox;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxRespVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.ImInboxSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import cn.iocoder.yudao.module.im.service.inbox.ImInboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 收件箱")
@RestController
@RequestMapping("/im/inbox")
@Validated
public class ImInboxController {

    @Resource
    private ImInboxService imInboxService;

    @PostMapping("/create")
    @Operation(summary = "创建收件箱")
    @PreAuthorize("@ss.hasPermission('im:inbox:create')")
    public CommonResult<Long> createInbox(@Valid @RequestBody ImInboxSaveReqVO createReqVO) {
        return success(imInboxService.createInbox(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新收件箱")
    @PreAuthorize("@ss.hasPermission('im:inbox:update')")
    public CommonResult<Boolean> updateInbox(@Valid @RequestBody ImInboxSaveReqVO updateReqVO) {
        imInboxService.updateInbox(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收件箱")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('im:inbox:delete')")
    public CommonResult<Boolean> deleteInbox(@RequestParam("id") Long id) {
        imInboxService.deleteInbox(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得收件箱")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('im:inbox:query')")
    public CommonResult<ImInboxRespVO> getInbox(@RequestParam("id") Long id) {
        ImInboxDO inbox = imInboxService.getInbox(id);
        return success(BeanUtils.toBean(inbox, ImInboxRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得收件箱分页")
    @PreAuthorize("@ss.hasPermission('im:inbox:query')")
    public CommonResult<PageResult<ImInboxRespVO>> getInboxPage(@Valid ImInboxPageReqVO pageReqVO) {
        PageResult<ImInboxDO> pageResult = imInboxService.getInboxPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ImInboxRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出收件箱 Excel")
    @PreAuthorize("@ss.hasPermission('im:inbox:export')")
    @OperateLog(type = EXPORT)
    public void exportInboxExcel(@Valid ImInboxPageReqVO pageReqVO,
                                 HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ImInboxDO> list = imInboxService.getInboxPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "收件箱.xls", "数据", ImInboxRespVO.class,
                BeanUtils.toBean(list, ImInboxRespVO.class));
    }

}