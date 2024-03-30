package cn.iocoder.yudao.module.im.controller.admin.conversation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationLastTimeReqVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationRespVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ConversationPinnedReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import cn.iocoder.yudao.module.im.service.conversation.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

// TODO @anhaohao：im 前缀少啦
@Tag(name = "管理后台 - IM 会话")
@RestController
@RequestMapping("/im/conversation")
@Validated
public class ConversationController {

    @Resource
    private ConversationService conversationService;

    @GetMapping("/list")
    @Operation(summary = "获得用户的会话列表")
    public CommonResult<List<ImConversationRespVO>> getConversationList() {
        List<ConversationDO> conversationList = conversationService.getConversationList();
        return success(BeanUtils.toBean(conversationList, ImConversationRespVO.class));
    }

    // TODO @hao：这个接口，需要单独的 VO 哈；
    @PostMapping("/update-pinned")
    @Operation(summary = "置顶会话")
    public CommonResult<Boolean> updatePinned(@Valid @RequestBody ConversationPinnedReqVO updateReqVO) {
        conversationService.updatePinned(updateReqVO);
        return success(true);
    }

    // TODO @hao：这个接口，需要单独的 VO 哈；
    @PostMapping("/update-last-read-time")
    @Operation(summary = "更新最后已读时间")
    public CommonResult<Boolean> updateLastReadTime(@Valid @RequestBody ConversationLastTimeReqVO updateReqVO) {
        conversationService.updateLastReadTime(updateReqVO);
        return success(true);
    }

}