package cn.iocoder.yudao.module.im.controller.admin.conversation;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationRespVO;
import cn.iocoder.yudao.module.im.controller.admin.conversation.vo.ImConversationSaveReqVO;
import cn.iocoder.yudao.module.im.dal.dataobject.conversation.ConversationDO;
import cn.iocoder.yudao.module.im.service.conversation.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

// TODO @hao: 管理后台 - IM 会话
@Tag(name = "管理后台 - IM 会话")
@RestController
@RequestMapping("/im/conversation")
@Validated
public class ConversationController {

    // TODO @hao: conversationService 即可
    @Resource
    private ConversationService conversationService;

    // TODO @hao：/list 会话列表；一般 get 是给单个对象，或者 get-count 这种；然后 conversation 已经是模块名了，所以可以简化
    @GetMapping("/get-conversation")
    @Operation(summary = "获得用户的会话列表")
    @PreAuthorize("hasAuthority('im:conversation:query')") // TODO @hao：不用权限哈
    public CommonResult<List<ImConversationRespVO>> getConversationList() {
        List<ConversationDO> conversationList = conversationService.getConversationList();
        return success(BeanUtils.toBean(conversationList, ImConversationRespVO.class));
    }

    // TODO @hao：/update-pinned 保持和 db 字段一致哈；
    // TODO @hao：这个接口，需要单独的 VO 哈；
    @PostMapping("/update-top")
    @Operation(summary = "置顶会话")
    @PreAuthorize("hasAuthority('im:conversation:update')") // TODO @hao：不用权限哈；因为肯定会判断是不是自己的
    public CommonResult<Boolean> updateTop(@Valid @RequestBody ImConversationSaveReqVO updateReqVO) {
        conversationService.updateTop(updateReqVO);
        return success(true);
    }


    // TODO @hao：这个接口，需要单独的 VO 哈；
    @PostMapping("/update-last-read-time")
    @Operation(summary = "更新最后已读时间")
    @PreAuthorize("hasAuthority('im:conversation:update')")  // TODO @hao：不用权限哈；因为肯定会判断是不是自己的
    public CommonResult<Boolean> updateLastReadTime(@Valid @RequestBody ImConversationSaveReqVO updateReqVO) {
        conversationService.updateLastReadTime(updateReqVO);
        return success(true);
    }

}