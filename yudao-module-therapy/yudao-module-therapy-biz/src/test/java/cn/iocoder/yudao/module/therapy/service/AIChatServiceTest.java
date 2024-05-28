package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.framework.test.core.util.AssertUtils;
import cn.iocoder.yudao.module.therapy.service.impl.ZhiPuAIChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

/**
 * Author:lidongw_1
 * Date 2024/5/27
 * Description: ai聊天服务 Test
 **/
@ComponentScan(basePackages = {"${yudao.info.base-package}.server", "${yudao.info.base-package}.module"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes =AIChatServiceTest.class)
@ActiveProfiles("unit-test")
@TestPropertySource(properties = {
        "zhipu.api.key=11ea80b48e23f4ee0d611ec4d18aac6e.vpGfjsI5HeMCEx5Z"
})
@Import(ZhiPuAIChatServiceImpl.class)
public class AIChatServiceTest {

    @Resource
    private ZhiPuAIChatServiceImpl aiChatService;

    @BeforeEach
    public void before() {
        System.setProperty("spring.application.name", "my-application-name");
        // mock
      //  when(aiChatService.chat(eq(1L), eq("你好"))).thenReturn("你好，我是智能助手，有什么可以帮助你的吗？");
    }

    @Test
    public void testChat() {
        // 准备参数
        String answer = aiChatService.chat(1L,"", "你好");
        // 校验
        AssertUtils.assertPojoEquals("testChat", answer, "你好，我是智能助手，有什么可以帮助你的吗？");
    }

    @Test
    public void  teenProblemClassificationTest(){
        // 准备参数
        String answer = aiChatService.teenProblemClassification("你好");
        // 校验
        AssertUtils.assertPojoEquals("teenProblemClassificationTest", answer, "你好，我是智能助手，有什么可以帮助你的吗？");
    }

}
