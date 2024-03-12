package cn.iocoder.yudao.module.im.controller.admin.conversation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationPageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationRespVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import cn.iocoder.yudao.module.im.service.conversation.ConversationService;
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

@Tag(name = "管理后台 - 会话")
@RestController
@RequestMapping("/im/conversation")
@Validated
public class ConversationController {

    @Resource
    private ConversationService conversationService;

    @PostMapping("/create")
    @Operation(summary = "创建会话")
    @PreAuthorize("@ss.hasPermission('im:conversation:create')")
    public CommonResult<Long> createConversation(@Valid @RequestBody ConversationSaveReqVO createReqVO) {
        return success(conversationService.createConversation(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会话")
    @PreAuthorize("@ss.hasPermission('im:conversation:update')")
    public CommonResult<Boolean> updateConversation(@Valid @RequestBody ConversationSaveReqVO updateReqVO) {
        conversationService.updateConversation(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会话")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('im:conversation:delete')")
    public CommonResult<Boolean> deleteConversation(@RequestParam("id") Long id) {
        conversationService.deleteConversation(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会话")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('im:conversation:query')")
    public CommonResult<ConversationRespVO> getConversation(@RequestParam("id") Long id) {
        ConversationDO conversation = conversationService.getConversation(id);
        return success(BeanUtils.toBean(conversation, ConversationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会话分页")
    @PreAuthorize("@ss.hasPermission('im:conversation:query')")
    public CommonResult<PageResult<ConversationRespVO>> getConversationPage(@Valid ConversationPageReqVO pageReqVO) {
        PageResult<ConversationDO> pageResult = conversationService.getConversationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ConversationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会话 Excel")
    @PreAuthorize("@ss.hasPermission('im:conversation:export')")
    @OperateLog(type = EXPORT)
    public void exportConversationExcel(@Valid ConversationPageReqVO pageReqVO,
                                        HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ConversationDO> list = conversationService.getConversationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会话.xls", "数据", ConversationRespVO.class,
                BeanUtils.toBean(list, ConversationRespVO.class));
    }

}