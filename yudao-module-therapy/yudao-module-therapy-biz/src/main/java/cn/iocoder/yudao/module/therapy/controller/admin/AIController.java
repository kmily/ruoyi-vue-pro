package cn.iocoder.yudao.module.therapy.controller.admin;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.therapy.controller.admin.VO.AutomaticThinkingRecognitionChatHistoriesVO;
import cn.iocoder.yudao.module.therapy.controller.admin.VO.ChatHistoryRequest;
import cn.iocoder.yudao.module.therapy.dal.dataobject.chat.ChatMessageDO;
import cn.iocoder.yudao.module.therapy.service.AIChatService;
import cn.iocoder.yudao.module.therapy.service.enums.UserTypeEnum;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:lidongw_1
 * @Date 2024/6/7
 * @Description: Ai控制器
 **/

@Tag(name = "管理后台 - AI")
@RestController
@RequestMapping("/therapy/ai")
@Validated
@Slf4j
public class AIController {

    @Resource
    AIChatService aiChatService;

    @PostMapping("/chat-histories")
    public CommonResult<List<AutomaticThinkingRecognitionChatHistoriesVO>> getChatHistories(@Validated @RequestBody ChatHistoryRequest request) {

        try {
            List<ChatMessageDO> chatMessageDOS = aiChatService.queryChatHistories(request.getUserId(), request.getPageNo(), request.getPageSize());
            List<AutomaticThinkingRecognitionChatHistoriesVO> automaticThinkingRecognitionChatHistoriesVOS = Lists.newArrayList();
            for (ChatMessageDO chatMessageDO : chatMessageDOS) {
                AutomaticThinkingRecognitionChatHistoriesVO automaticThinkingRecognitionChatHistoriesVO = new AutomaticThinkingRecognitionChatHistoriesVO();
                automaticThinkingRecognitionChatHistoriesVO.setId(chatMessageDO.getId());
                automaticThinkingRecognitionChatHistoriesVO.setUserId(chatMessageDO.getSendUserId());
                automaticThinkingRecognitionChatHistoriesVO.setConversationId(chatMessageDO.getConversationId());
                automaticThinkingRecognitionChatHistoriesVO.setContent(chatMessageDO.getContent());
                automaticThinkingRecognitionChatHistoriesVO.setUserType(EnumUtils.getEnum(UserTypeEnum.class, chatMessageDO.getSendUserType(), UserTypeEnum.UNKNOWN));
                automaticThinkingRecognitionChatHistoriesVOS.add(automaticThinkingRecognitionChatHistoriesVO);
            }
            return CommonResult.success(automaticThinkingRecognitionChatHistoriesVOS);
        }catch (Exception e){
            log.error("getChatHistories error",e);
            return CommonResult.error(new ServiceException(new ErrorCode(500,e.getMessage())));
        }
    }

    @PostMapping("/chat-count")
    public CommonResult<Long> getChatHistoriesCount(@Validated @RequestBody ChatHistoryRequest request) {

        try {
            Long l = aiChatService.queryChatHistoriesCount(request.getUserId());

            return CommonResult.success(l);
        }catch (Exception e){
            log.error("getChatHistories error",e);
            return CommonResult.error(new ServiceException(new ErrorCode(500,e.getMessage())));
        }
    }
}