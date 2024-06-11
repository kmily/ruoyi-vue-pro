package cn.iocoder.yudao.module.therapy.controller.admin.VO;

import cn.iocoder.yudao.module.therapy.service.enums.UserTypeEnum;
import lombok.Data;

/**
 * @Author:lidongw_1
 * @Date 2024/6/7
 * @Description: 自动化思维识别聊天记录
 **/

@Data
public class AutomaticThinkingRecognitionChatHistoriesVO {

    private Long id;
    private Long userId;
    private String conversationId;
    private String content;
    private UserTypeEnum userType;

}
