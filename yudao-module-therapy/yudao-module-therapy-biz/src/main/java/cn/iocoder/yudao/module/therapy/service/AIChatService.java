package cn.iocoder.yudao.module.therapy.service;

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
    String chat(Long userId, String content);




}
