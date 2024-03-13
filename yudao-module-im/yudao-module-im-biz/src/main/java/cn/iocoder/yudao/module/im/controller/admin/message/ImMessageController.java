package cn.iocoder.yudao.module.im.controller.admin.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageRespVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.service.message.ImMessageService;
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

@Tag(name = "管理后台 - 消息")
@RestController
@RequestMapping("/im/message")
@Validated
public class ImMessageController {

    @Resource
    private ImMessageService imMessageService;

    @PostMapping("/create")
    @Operation(summary = "创建消息")
    @PreAuthorize("@ss.hasPermission('im:message:create')")
    public CommonResult<Long> createMessage(@Valid @RequestBody ImMessageSaveReqVO createReqVO) {
        return success(imMessageService.createMessage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新消息")
    @PreAuthorize("@ss.hasPermission('im:message:update')")
    public CommonResult<Boolean> updateMessage(@Valid @RequestBody ImMessageSaveReqVO updateReqVO) {
        imMessageService.updateMessage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除消息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('im:message:delete')")
    public CommonResult<Boolean> deleteMessage(@RequestParam("id") Long id) {
        imMessageService.deleteMessage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得消息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('im:message:query')")
    public CommonResult<ImMessageRespVO> getMessage(@RequestParam("id") Long id) {
        ImMessageDO message = imMessageService.getMessage(id);
        return success(BeanUtils.toBean(message, ImMessageRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得消息分页")
    @PreAuthorize("@ss.hasPermission('im:message:query')")
    public CommonResult<PageResult<ImMessageRespVO>> getMessagePage(@Valid ImMessagePageReqVO pageReqVO) {
        PageResult<ImMessageDO> pageResult = imMessageService.getMessagePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ImMessageRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出消息 Excel")
    @PreAuthorize("@ss.hasPermission('im:message:export')")
    @OperateLog(type = EXPORT)
    public void exportMessageExcel(@Valid ImMessagePageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ImMessageDO> list = imMessageService.getMessagePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "消息.xls", "数据", ImMessageRespVO.class,
                BeanUtils.toBean(list, ImMessageRespVO.class));
    }

    @PostMapping("/send")
    @Operation(summary = "发送私聊消息")
    public CommonResult<Long> sendMessage(@Valid @RequestBody ImMessageSaveReqVO imMessageSaveReqVO) {
        return success(imMessageService.sendPrivateMessage(imMessageSaveReqVO));
    }

}