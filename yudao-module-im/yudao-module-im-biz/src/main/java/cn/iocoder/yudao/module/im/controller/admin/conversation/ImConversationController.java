package cn.iocoder.yudao.module.im.controller.admin.conversation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationUpdateLastReadTimeReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationRespVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationUpdatePinnedReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ImConversationDO;
import cn.iocoder.yudao.module.im.service.conversation.ImConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - IM 会话")
@RestController
@RequestMapping("/im/conversation")
@Validated
public class ImConversationController {

    @Resource
    private ImConversationService imConversationService;

    @GetMapping("/list")
    @Operation(summary = "获得用户的会话列表")
    public CommonResult<List<ImConversationRespVO>> getConversationList() {
        List<ImConversationDO> conversationList = imConversationService.getConversationList();
        return success(BeanUtils.toBean(conversationList, ImConversationRespVO.class));
    }

    @PostMapping("/update-pinned")
    @Operation(summary = "置顶会话")
    public CommonResult<Boolean> updatePinned(@Valid @RequestBody ImConversationUpdatePinnedReqVO updateReqVO) {
        imConversationService.updatePinned(getLoginUserId(),updateReqVO);
        return success(true);
    }

    @PostMapping("/update-last-read-time")
    @Operation(summary = "更新最后已读时间")
    public CommonResult<Boolean> updateLastReadTime(@Valid @RequestBody ImConversationUpdateLastReadTimeReqVO updateReqVO) {
        imConversationService.updateLastReadTime(getLoginUserId(),updateReqVO);
        return success(true);
    }

}