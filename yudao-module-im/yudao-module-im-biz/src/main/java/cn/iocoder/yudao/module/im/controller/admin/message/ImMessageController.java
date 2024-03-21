package cn.iocoder.yudao.module.im.controller.admin.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessagePageReqVO;
import cn.iocoder.yudao.module.im.controller.admin.message.vo.ImMessageRespVO;
import cn.iocoder.yudao.module.im.dal.dataobject.message.ImMessageDO;
import cn.iocoder.yudao.module.im.service.message.ImMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - IM 消息")
@RestController
@RequestMapping("/im/message")
@Validated
public class ImMessageController {

    @Resource
    private ImMessageService imMessageService;

    @GetMapping("/get-message-by-sequence") // TODO @hao：list-by-sequence
    @Operation(summary = "拉取消息-增量拉取大于 seq 的消息") // TODO @hao：可以改成 拉取大于 sequence 的消息列表
    @Parameter(name = "sequence", description = "序号", required = true, example = "1")
    @Parameter(name = "size", description = "条数", required = true, example = "10")
    @PreAuthorize("@ss.hasPermission('im:message:query')") // TODO @hao：权限可以删除哈；
    public CommonResult<List<ImMessageRespVO>> loadMessage(@RequestParam("sequence") Long sequence,
                                                           @RequestParam("size") Integer size) {
        // TODO @hao：方法名，可以改成 getMessageListBySequence
        // TODO @hao：如果是返回列表，变量要用 messages， 或者 messageList，体现出是复数
        List<ImMessageDO> message = imMessageService.loadMessage(getLoginUserId(), sequence, size);
        return success(BeanUtils.toBean(message, ImMessageRespVO.class));
    }

    // TODO @hao：这个接口的使用场景是哪个哈？
    @GetMapping("/get-all-message")
    @Operation(summary = "拉取全部消息")
    @Parameter(name = "size", description = "条数", required = true, example = "10")
    @PreAuthorize("@ss.hasPermission('im:message:query')")
    public CommonResult<List<ImMessageRespVO>> loadAllMessage(@RequestParam("size") Integer size) {
        List<ImMessageDO> message = imMessageService.loadAllMessage(getLoginUserId(), size);
        return success(BeanUtils.toBean(message, ImMessageRespVO.class));
    }

    // TODO @hao：是不是分页参数不太对哈？应该只有 conversationNo，sendTime 字段；然后，做链式分页的查询，不太适合传统的 pageSize + number 查询；
    @GetMapping("/page")
    @Operation(summary = "查询聊天记录-分页")
    @PreAuthorize("@ss.hasPermission('im:message:query')") // TODO @hao：权限可以删除哈；
    public CommonResult<PageResult<ImMessageRespVO>> getMessagePage(@Valid ImMessagePageReqVO pageReqVO) {
        PageResult<ImMessageDO> messagePage = imMessageService.getMessagePage(pageReqVO);
        return success(BeanUtils.toBean(messagePage, ImMessageRespVO.class));
    }

}