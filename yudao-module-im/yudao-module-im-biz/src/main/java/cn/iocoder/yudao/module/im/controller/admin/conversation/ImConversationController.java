package cn.iocoder.yudao.module.im.controller.admin.conversation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationRespVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
import cn.iocoder.yudao.module.im.service.conversation.ImConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 会话")
@RestController
@RequestMapping("/im/conversation")
@Validated
public class ImConversationController {

    @Resource
    private ImConversationService imConversationService;

    @GetMapping("/get-conversation")
    @Operation(summary = "获得用户的会话列表")
    @PreAuthorize("hasAuthority('im:conversation:query')")
    public CommonResult<List<ImConversationRespVO>> getConversationList() {
        List<ImConversationDO> conversationList = imConversationService.getConversationList();
        return success(BeanUtils.toBean(conversationList, ImConversationRespVO.class));
    }


    @PostMapping("/update-top")
    @Operation(summary = "置顶会话")
    @PreAuthorize("hasAuthority('im:conversation:update')")
    public CommonResult<Boolean> updateTop(@Valid @RequestBody ImConversationSaveReqVO updateReqVO) {
        imConversationService.updateTop(updateReqVO);
        return success(true);
    }


    @PostMapping("/update-last-read-time")
    @Operation(summary = "更新最后已读时间")
    @PreAuthorize("hasAuthority('im:conversation:update')")
    public CommonResult<Boolean> updateLastReadTime(@Valid @RequestBody ImConversationSaveReqVO updateReqVO) {
        imConversationService.updateLastReadTime(updateReqVO);
        return success(true);
    }

}