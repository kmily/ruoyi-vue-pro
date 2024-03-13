package cn.iocoder.yudao.module.im.controller.admin.inbox;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxRespVO;
import cn.iocoder.yudao.module.im.controller.admin.inbox.vo.InboxSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.inbox.ImInboxDO;
import cn.iocoder.yudao.module.im.service.inbox.InboxService;
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
public class InboxController {

    @Resource
    private InboxService inboxService;

    @PostMapping("/create")
    @Operation(summary = "创建收件箱")
    @PreAuthorize("@ss.hasPermission('im:inbox:create')")
    public CommonResult<Long> createInbox(@Valid @RequestBody InboxSaveReqVO createReqVO) {
        return success(inboxService.createInbox(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新收件箱")
    @PreAuthorize("@ss.hasPermission('im:inbox:update')")
    public CommonResult<Boolean> updateInbox(@Valid @RequestBody InboxSaveReqVO updateReqVO) {
        inboxService.updateInbox(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收件箱")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('im:inbox:delete')")
    public CommonResult<Boolean> deleteInbox(@RequestParam("id") Long id) {
        inboxService.deleteInbox(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得收件箱")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('im:inbox:query')")
    public CommonResult<InboxRespVO> getInbox(@RequestParam("id") Long id) {
        ImInboxDO inbox = inboxService.getInbox(id);
        return success(BeanUtils.toBean(inbox, InboxRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得收件箱分页")
    @PreAuthorize("@ss.hasPermission('im:inbox:query')")
    public CommonResult<PageResult<InboxRespVO>> getInboxPage(@Valid InboxPageReqVO pageReqVO) {
        PageResult<ImInboxDO> pageResult = inboxService.getInboxPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InboxRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出收件箱 Excel")
    @PreAuthorize("@ss.hasPermission('im:inbox:export')")
    @OperateLog(type = EXPORT)
    public void exportInboxExcel(@Valid InboxPageReqVO pageReqVO,
                                 HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ImInboxDO> list = inboxService.getInboxPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "收件箱.xls", "数据", InboxRespVO.class,
                BeanUtils.toBean(list, InboxRespVO.class));
    }

}