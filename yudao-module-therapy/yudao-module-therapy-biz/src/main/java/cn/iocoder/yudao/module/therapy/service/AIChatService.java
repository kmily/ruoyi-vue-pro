package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.admin.VO.AutomaticThinkingRecognitionChatHistoriesVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.chat.ChatMessageDO;
import cn.iocoder.yudao.module.therapy.service.dto.SSEMsgDTO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Author: Lidong Wang <lidongw_1@example.com>
 * Date 2024/5/24
 * Description: 大预言模型
 **/
public interface AIChatService {

    /**
     * 大预言模型
     * @param userId 用户编号
     * @param content 内容
     * @return 结果
     */
    String chat(Long userId,String conversationId, String content);

    /**
     * @param userId 用户编号
     * @param conversationId 会话编号
     * @param content 用户聊天内容
     * @return 结果
     */
    String chatForStream(Long userId, String conversationId, String content);

    /**
     * 数据分析
     * @param sysPrompt 提示
     * @param analyseData 分析数据
     * @return 结果
     */
    String dataAnalyse (String sysPrompt, String analyseData);

    /**
     * 青少年问题分类
     * @param problems 问题
     * @return 结果
     */
    String teenProblemClassification (String problems);

    /**
     * 自动思维识别
     *
     * @param userId         用户编号
     * @param conversationId 会话编号
     * @param content        用户聊天问题
     * @return 结果
     */
    Flux<SSEMsgDTO> automaticThinkingRecognition(Long userId, String conversationId, String content);

    List<ChatMessageDO> queryChatHistories(Long userId, Integer pageNo, Integer pageSize);

    Long queryChatHistoriesCount(Long userId);
}
