package cn.iocoder.yudao.module.im.controller.admin.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.SendMessageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.SendMessageRespVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.MessageDO;
import cn.iocoder.yudao.module.im.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - IM 消息")
@RestController
@RequestMapping("/im/message")
@Validated
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/send")
    @Operation(summary = "发送消息")
    public CommonResult<SendMessageRespVO> sendMessage(@Valid @RequestBody SendMessageReqVO message) {
        return success(messageService.sendMessage(getLoginUserId(), message));
    }

    // TODO @anhaohao：我在想，这个接口，改成叫 pullMessageList，会不会更好理解？拉取消息列表
    @GetMapping("/list-by-sequence")
    @Operation(summary = "拉取大于 sequence 的消息列表")
    @Parameter(name = "sequence", description = "序号", required = true, example = "1")
    @Parameter(name = "size", description = "条数", required = true, example = "10")
    public CommonResult<List<MessageReqVO>> getMessageListBySequence(@RequestParam("sequence") Long sequence,
                                                                     @RequestParam("size") Integer size) {
        List<MessageDO> messages = messageService.getMessageListBySequence(getLoginUserId(), sequence, size);
        return success(BeanUtils.toBean(messages, MessageReqVO.class));
    }

    // TODO @anhaohao：直接叫 getMessageList，不叫历史哈；因为它只是给用户叫历史，对系统来说，就是消息列表
    @GetMapping("/history")
    @Operation(summary = "查询聊天记录-根据会话标志和发送时间进行分页查询")
    public CommonResult<List<MessageReqVO>> getHistoryMessage(@Valid MessagePageReqVO pageReqVO) {
        //根据会话标志和发送时间进行分页查询
        List<MessageDO> messagePage = messageService.getHistoryMessage(pageReqVO);
        return success(BeanUtils.toBean(messagePage, MessageReqVO.class));
    }

}