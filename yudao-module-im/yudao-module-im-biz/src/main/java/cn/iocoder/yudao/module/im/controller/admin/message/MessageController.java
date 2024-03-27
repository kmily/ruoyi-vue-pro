package cn.iocoder.yudao.module.im.controller.admin.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.MessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageReqVO;
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

    @GetMapping("/list-by-sequence")
    @Operation(summary = "拉取大于 sequence 的消息列表")
    @Parameter(name = "sequence", description = "序号", required = true, example = "1")
    @Parameter(name = "size", description = "条数", required = true, example = "10")
    public CommonResult<List<ImMessageReqVO>> loadMessage(@RequestParam("sequence") Long sequence,
                                                          @RequestParam("size") Integer size) {
        List<MessageDO> messages = messageService.getMessageListBySequence(getLoginUserId(), sequence, size);
        return success(BeanUtils.toBean(messages, ImMessageReqVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "查询聊天记录-分页")
    public CommonResult<List<ImMessageReqVO>> getMessagePage(@Valid MessagePageReqVO pageReqVO) {
        //根据会话标志和发送时间进行分页查询
        List<MessageDO> messagePage = messageService.getMessagePage(pageReqVO);
        return success(BeanUtils.toBean(messagePage, ImMessageReqVO.class));
    }

}