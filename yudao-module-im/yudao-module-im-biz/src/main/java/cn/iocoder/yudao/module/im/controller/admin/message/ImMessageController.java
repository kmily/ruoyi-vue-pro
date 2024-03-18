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

@Tag(name = "管理后台 - 消息")
@RestController
@RequestMapping("/im/message")
@Validated
public class ImMessageController {

    @Resource
    private ImMessageService imMessageService;

    @GetMapping("/get-message-by-sequence")
    @Operation(summary = "拉取消息-增量拉取大于 seq 的消息")
    @Parameter(name = "sequence", description = "序号", required = true, example = "1")
    @Parameter(name = "size", description = "条数", required = true, example = "10")
    @PreAuthorize("@ss.hasPermission('im:message:query')")
    public CommonResult<List<ImMessageRespVO>> loadMessage(
            @RequestParam("sequence") Long sequence,
            @RequestParam("size") Integer size) {
        List<ImMessageDO> message = imMessageService.loadMessage(getLoginUserId(), sequence, size);
        return success(BeanUtils.toBean(message, ImMessageRespVO.class));
    }


    @GetMapping("/get-all-message")
    @Operation(summary = "拉取全部消息")
    @Parameter(name = "size", description = "条数", required = true, example = "10")
    @PreAuthorize("@ss.hasPermission('im:message:query')")
    public CommonResult<List<ImMessageRespVO>> loadAllMessage(@RequestParam("size") Integer size) {
        List<ImMessageDO> message = imMessageService.loadAllMessage(getLoginUserId(), size);
        return success(BeanUtils.toBean(message, ImMessageRespVO.class));
    }


    @GetMapping("/page")
    @Operation(summary = "查询聊天记录-分页")
    @PreAuthorize("@ss.hasPermission('im:message:query')")
    public CommonResult<PageResult<ImMessageRespVO>> getMessagePage(@Valid ImMessagePageReqVO pageReqVO) {
        PageResult<ImMessageDO> messagePage = imMessageService.getMessagePage(pageReqVO);
        return success(BeanUtils.toBean(messagePage, ImMessageRespVO.class));
    }


}