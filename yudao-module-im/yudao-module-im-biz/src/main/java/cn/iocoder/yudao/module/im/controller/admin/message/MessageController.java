package cn.iocoder.yudao.module.im.controller.admin.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessageRespVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessageSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import cn.iocoder.yudao.module.im.service.message.MessageService;
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
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/create")
    @Operation(summary = "创建消息")
    @PreAuthorize("@ss.hasPermission('im:message:create')")
    public CommonResult<Long> createMessage(@Valid @RequestBody MessageSaveReqVO createReqVO) {
        return success(messageService.createMessage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新消息")
    @PreAuthorize("@ss.hasPermission('im:message:update')")
    public CommonResult<Boolean> updateMessage(@Valid @RequestBody MessageSaveReqVO updateReqVO) {
        messageService.updateMessage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除消息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('im:message:delete')")
    public CommonResult<Boolean> deleteMessage(@RequestParam("id") Long id) {
        messageService.deleteMessage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得消息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('im:message:query')")
    public CommonResult<MessageRespVO> getMessage(@RequestParam("id") Long id) {
        MessageDO message = messageService.getMessage(id);
        return success(BeanUtils.toBean(message, MessageRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得消息分页")
    @PreAuthorize("@ss.hasPermission('im:message:query')")
    public CommonResult<PageResult<MessageRespVO>> getMessagePage(@Valid MessagePageReqVO pageReqVO) {
        PageResult<MessageDO> pageResult = messageService.getMessagePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MessageRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出消息 Excel")
    @PreAuthorize("@ss.hasPermission('im:message:export')")
    @OperateLog(type = EXPORT)
    public void exportMessageExcel(@Valid MessagePageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MessageDO> list = messageService.getMessagePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "消息.xls", "数据", MessageRespVO.class,
                BeanUtils.toBean(list, MessageRespVO.class));
    }

    @PostMapping("/send")
    @Operation(summary = "发送私聊消息")
    public CommonResult<Long> sendMessage(@Valid @RequestBody MessageSaveReqVO messageSaveReqVO) {
        return success(messageService.sendPrivateMessage(messageSaveReqVO));
    }

}