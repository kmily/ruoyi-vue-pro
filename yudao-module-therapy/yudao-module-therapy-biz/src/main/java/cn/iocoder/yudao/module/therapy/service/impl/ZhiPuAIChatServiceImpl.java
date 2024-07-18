package cn.iocoder.yudao.module.therapy.service.impl;

import cn.hutool.json.JSONObject;
import cn.iocoder.boot.module.therapy.enums.SurveyType;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.yudao.module.system.service.dict.DictDataService;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AnAnswerReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.DayitemStepSubmitReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.chat.ChatMessageDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.chat.ChatMessageMapper;
import cn.iocoder.yudao.module.therapy.service.AIChatService;
import cn.iocoder.yudao.module.therapy.service.SurveyService;
import cn.iocoder.yudao.module.therapy.service.TaskFlowService;
import cn.iocoder.yudao.module.therapy.service.dto.SSEMsgDTO;
import cn.iocoder.yudao.module.therapy.service.enums.RequestSourceEnum;
import cn.iocoder.yudao.module.therapy.service.enums.UserTypeEnum;
import cn.iocoder.yudao.module.therapy.taskflow.BaseFlow;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageAccumulator;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.Choice;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import com.zhipu.oapi.service.v4.model.ModelData;
import io.reactivex.Flowable;
import liquibase.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.iocoder.yudao.module.therapy.taskflow.Const.SURVEY_SOURCE_TYPE;

/**
 * Author:lidongw_1
 * Date 2024/5/24
 * Description: ZHIPU AI 大模型
 **/
@Slf4j
@Service
public class ZhiPuAIChatServiceImpl implements AIChatService {


    @Resource
    private DictDataService dictDataService;

    @Resource
    ChatMessageMapper chatMessageMapper;

    @Resource
    SurveyService surveyService;

    @Resource
    TaskFlowService taskFlowService;

    //@Value("${zhipu.api.key:this-is-a-test-key}")
    String API_KEY = "7820338e5c0e1d9228f8c2a5e2bf2e0d.EIWUgIkQCoStAnBU";

    private ClientV4 client;
    @Autowired
    private JsonUtils jsonUtils;

    @PostConstruct
    public void init() {
        client = new ClientV4.Builder(API_KEY).build();
    }

    private static final ObjectMapper mapper = defaultObjectMapper();

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper;
    }

    @Override
    public String chat(Long userId, String conversationId, String content) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "你好，我是智能助手，有什么可以帮助你的吗？");
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), content);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse chat = chat(messages, false);

        if (chat == null) {
            return "我不知道你在说什么";
        }

        if (!chat.isSuccess()) {
            log.error("chat error:{}", JSON.toJSONString(chat));
            return "刚才说的没有理解，你可以在说一遍吗？";
        }

        ModelData data = chat.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            return "我不知道你在说什么";
        }
        return choices.get(0).getDelta().getContent();
    }

    @Override
    public String chatForStream(Long userId, String conversationId, String content) {
        return "";
    }

    @Override
    public String dataAnalyse(String sysPrompt, String analyseData) {

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), sysPrompt);
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), analyseData);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse chat = chat(messages, false);

        if (chat == null) {
            log.error("chat error:{}", "系统返回为NULL");
            return "我不知道你在说什么";
        }

        if (!chat.isSuccess()) {
            log.error("chat error:{}", JSON.toJSONString(chat));
            return "刚才说的没有理解，你可以在说一遍吗？";
        }

        ModelData data = chat.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            log.error("chat error:{}", "choices为空");
            return "我不知道你在说什么";
        }
        return choices.get(0).getDelta().getContent();

    }

    /**
     * 青少年问题分类
     *
     * @param problems 问题
     * @return
     */
    @Override
    public String teenProblemClassification(String problems) {
        ModelApiResponse chat = problemClassification(problems);
        if (chat == null) return "系统提示未配置";

        if (!chat.isSuccess()) {
            log.error("chat problem:{} error:{}", problems, JSON.toJSONString(chat));
            return "解析失败：" + chat.getMsg();
        }

        ModelData data = chat.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            log.error("chat problem:{} error:{}", problems, "choices为空");
            return "解析失败，choices为空";
        }
        return String.valueOf(choices.get(0).getMessage().getContent());
    }

    private ModelApiResponse problemClassification(String problems) {
        DictDataDO dictDataDO = dictDataService.parseDictData("ai_system_prompt", "juvenile_problems_classification");
        if (dictDataDO == null) {
            log.error("dictDataDO is null, dictType:{} label:{}", "ai_system_prompt", "juvenile_problems_classification");
            return null;
        }

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), dictDataDO.getValue());
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), problems);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse chat = chat(messages, false);
        return chat;
    }

    @Override
    public List<String> teenProblemClassificationV2(String problems) {
        ModelApiResponse modelApiResponse = problemClassification(problems);

        if (modelApiResponse == null) {
            log.error("chat problem:{} error:{}", problems, "系统返回为NULL");
            return Collections.emptyList();
        }

        if (!modelApiResponse.isSuccess()) {
            log.error("chat problem:{} error:{}", problems, JSON.toJSONString(modelApiResponse));
            return Collections.emptyList();
        }

        ModelData data = modelApiResponse.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            log.error("chat problem:{} error:{}", problems, "choices为空");
            return Collections.emptyList();
        }

        String s = String.valueOf(choices.get(0).getMessage().getContent());
        if (JsonUtils.isJson(s)) {
            List<String> list = JsonUtils.parseArray(s, String.class);
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 自动化思维识别
     *
     * @param userId         用户编号
     * @param conversationId 会话编号
     * @param content        用户聊天问题
     * @return
     */
    @Override
    public Flux<SSEMsgDTO> automaticThinkingRecognition(Long userId, String conversationId, String content, RequestSourceEnum sourceEnum, Long dayItemInstanceId, String stepId) {
        //保存用户消息
        saveUserChatMessage(conversationId, userId, 0L, UserTypeEnum.USER, content, sourceEnum, dayItemInstanceId, stepId);

        DictDataDO dictDataDO = dictDataService.parseDictData("ai_system_prompt", "automatic_thinking_recognition_prompt");
        if (dictDataDO == null) {
            SSEMsgDTO sseMsgDTO = new SSEMsgDTO();
            sseMsgDTO.setContent("----END----");
            sseMsgDTO.setFinished(true);
            sseMsgDTO.setCode(500);
            sseMsgDTO.setMsg("系统提示未配置");
            log.warn("dictDataDO is null, dictType:{} label:{}", "ai_system_prompt", "juvenile_problems_classification");
            return Flux.just(sseMsgDTO);
        }
        List<ChatMessage> chatMessages = assembleHistoryList(conversationId, dictDataDO.getValue());

        ModelApiResponse sseModelApiResp = chat(chatMessages, true);
        if (sseModelApiResp.isSuccess()) {
            StringBuffer sb = new StringBuffer();
            Flowable<SSEMsgDTO> flowable = sseModelApiResp.getFlowable().map(modelData -> {
                // Convert ModelData to JSON string or desired string format
                SSEMsgDTO jsonString = convertModelDataToString(modelData);
                // Save to database
                //saveToDatabase(jsonString);
                log.info("AI Response :{}", JSON.toJSONString(jsonString));
                sb.append(jsonString.getContent());
                return jsonString;
            });
            return Flux.create(emitter -> flowable.subscribe(emitter::next, emitter::error, () -> {
                SSEMsgDTO sseMsgDTO = new SSEMsgDTO();
                sseMsgDTO.setContent("----END----");
                sseMsgDTO.setFinished(true);
                sseMsgDTO.setCode(200);
                emitter.next(sseMsgDTO);
                saveUserChatMessage(conversationId, 0L, userId, UserTypeEnum.ROBOT, sb.toString(), sourceEnum, dayItemInstanceId, stepId);
                emitter.complete();
            })).publishOn(Schedulers.boundedElastic()).cast(SSEMsgDTO.class);
        } else {
            return Flux.error(new RuntimeException("API call failed")).cast(SSEMsgDTO.class);
        }
    }

    @Override
    public List<ChatMessageDO> queryChatHistories(Long userId, Integer pageNo, Integer pageSize) {

        QueryWrapper<ChatMessageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("send_user_id", userId);
        queryWrapper.or();
        queryWrapper.eq("receive_user_id", userId);
        queryWrapper.orderByDesc("id");
        //分页
        queryWrapper.last("limit " + (pageNo - 1) * pageSize + "," + pageSize);
        List<ChatMessageDO> chatMessageDOS = chatMessageMapper.selectList(queryWrapper);

        return chatMessageDOS;
    }

    @Override
    public Long queryChatHistoriesCount(Long userId) {
        QueryWrapper<ChatMessageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("send_user_id", userId);
        queryWrapper.or();
        queryWrapper.eq("receive_user_id", userId);
        Long l = chatMessageMapper.selectCount(queryWrapper);
        return l;
    }

    private List<ChatMessage> assembleHistoryList(String conversationId, String sysPrompt) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), sysPrompt);
        messages.add(sysMessage);

        QueryWrapper<ChatMessageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id", conversationId);
        List<ChatMessageDO> chatMessageDOS = chatMessageMapper.selectList(queryWrapper);
        for (ChatMessageDO chatMessageDO : chatMessageDOS) {
            ChatMessage chatMessage = new ChatMessage();
            if (UserTypeEnum.USER.name().compareToIgnoreCase(chatMessageDO.getSendUserType()) == 0) {
                chatMessage.setRole(ChatMessageRole.USER.value());
            } else {
                chatMessage.setRole(ChatMessageRole.ASSISTANT.value());
            }
            chatMessage.setContent(chatMessageDO.getContent());
            messages.add(chatMessage);
        }
        return messages;

    }


    /**
     * 保存用户聊天消息
     *
     * @param conversationId 会话编号
     * @param sendUserId     发送用户编号
     * @param userType       用户类型
     * @param message        消息内容
     */
    private void saveUserChatMessage(String conversationId, long sendUserId, long receiveUserId, UserTypeEnum userType, String message, RequestSourceEnum sourceEnum, Long dayItemInstanceId, String stepId) {

        ChatMessageDO info = ChatMessageDO.builder().sendUserId(sendUserId).sendUserType(userType.name()).conversationId(conversationId).content(message).receiveUserId(receiveUserId).build();

        if (userType == UserTypeEnum.ROBOT) {
            try {
                long answerId = saveAutomatedThoughtsConclusion(message, sourceEnum);
                if (NumberUtils.gtZero(answerId) && RequestSourceEnum.MAIN_PROCESS.equals(sourceEnum)) {
                    try {
                        BaseFlow taskFlow = taskFlowService.getTaskFlow(receiveUserId, dayItemInstanceId);
                        DayitemStepSubmitReqVO dayitemStepSubmitReqVO = new DayitemStepSubmitReqVO();
                        dayitemStepSubmitReqVO.setStep_id(stepId);
                        taskFlowService.userSubmit(taskFlow, dayItemInstanceId, stepId, dayitemStepSubmitReqVO);
                        log.info("设置结束任务成功，会话ID: {}  dayItemInstanceId:{} stepId:{}", info.getConversationId(), dayItemInstanceId, stepId);
                    }catch (Exception e){
                        log.error("设置任务失败。receiveUserId：{} dayItemInstanceId:{} stepId:{}",receiveUserId,dayItemInstanceId,stepId );
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                log.error("创建问券报错。{}",info.getConversationId(),e);
            }
        }
        chatMessageMapper.insert(info);
    }


    /**
     * 保存自动化思维识别结论
     *
     * @param message
     * @param sourceEnum
     * @return
     */
    private Long saveAutomatedThoughtsConclusion(String message, RequestSourceEnum sourceEnum) {

        // [日期/时间：XXX。 情境：XXXX。 想法：自动化思维内容。情绪与身体感觉：情绪及评分，身体感觉及评分。行为及后果：XXX。]
        //Pattern pattern = Pattern.compile("\\[日期/时间：(.*?)。情境：(.*?)。自动化思维：“(.*?)”，“(.*?)”。情绪与身体感觉：(.*?)。行为及后果：(.*?)。\\]");

        Pattern isResult = Pattern.compile("\\[(.*?)\\]", Pattern.DOTALL);
        Matcher matcher1 = isResult.matcher(message);
        if (!matcher1.find()) {
            log.info("不是自动化思维识别结论, 消息源： {}  ", message);
            return 0L;
        }

        Pattern pattern = Pattern.compile("\\[日期/时间：(.*?)。\\s*情境：(.*?)。\\s*想法：(.*?)（自动化思维）。\\s*情绪与身体感觉：(.*?)。\\s*行为及后果：(.*?)。\\]");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String dateTime = matcher.group(1);
            String situation = matcher.group(2);
            String automaticThought1 = matcher.group(3);
            String emotionsAndBodyFeelings = matcher.group(4);
            String behaviorAndConsequences = matcher.group(5);

            //{
            //    "time": "5月24日下午",
            //        "scene": "准备上台演讲的前5分钟",
            //        "autoThought": "我一点不会想起来该说什么",
            //        "response": "紧张的不行",
            //        "result": "闭目深呼吸"
            //}

            SubmitSurveyReqVO submitSurveyReqVO = new SubmitSurveyReqVO();
            submitSurveyReqVO.setSurveyType(11);

            JSONObject jobj = new JSONObject();
            jobj.put("time", dateTime);
            jobj.put("scene", situation);
            jobj.put("autoThought", automaticThought1);
            jobj.put("response", emotionsAndBodyFeelings);
            jobj.put("result", behaviorAndConsequences);

            AnAnswerReqVO anAnswerReqVO = new AnAnswerReqVO();
            anAnswerReqVO.setQstCode(UUID.randomUUID().toString());
            anAnswerReqVO.setAnswer(jobj);
            submitSurveyReqVO.setQstList(Collections.singletonList(anAnswerReqVO));

            if (RequestSourceEnum.TOOLBOX.equals(sourceEnum)) {
                return surveyService.submitSurveyForTools(submitSurveyReqVO);
            } else {
                return surveyService.submitSurveyForFlow(submitSurveyReqVO);
            }
        } else {
            String s = checkIsAutomaticResult(message);
            if ("no".equalsIgnoreCase(s)) {
                log.info("大模型识别不是自动化思维结论：{}", message);
                String s1 = twoParseResult(message);
                s = checkIsAutomaticResult(s1);
                if ("no".equalsIgnoreCase(s)) {
                    log.warn("二次解析也失败了,{}", s1);
                    return 0L;
                }
            }

            if (!jsonUtils.isJson(s)) {
                String regex = "\\{[^}]+\\}";
                // 编译正则表达式
                pattern = Pattern.compile(regex);
                // 在输入文本中查找匹配项
                matcher = pattern.matcher(s);
                if (matcher.find()) {
                    s = matcher.group(); // 提取匹配到的JSON部分
                } else {
                    log.error("大模型识别返回不是JSON。 {}", s);
                    return 0L;
                }
            }

            JSONObject jsonObject = new JSONObject(s);
            SubmitSurveyReqVO submitSurveyReqVO = new SubmitSurveyReqVO();
            submitSurveyReqVO.setSurveyType(11);
            AnAnswerReqVO anAnswerReqVO = new AnAnswerReqVO();
            anAnswerReqVO.setQstCode(UUID.randomUUID().toString());
            anAnswerReqVO.setAnswer(jsonObject);
            submitSurveyReqVO.setQstList(Collections.singletonList(anAnswerReqVO));

            if (RequestSourceEnum.TOOLBOX.equals(sourceEnum)) {
                return surveyService.submitSurveyForTools(submitSurveyReqVO);
            } else {
                Long id = surveyService.initSurveyAnswer(SurveyType.AUTO_THOUGHT_RECOGNITION.getCode(), SURVEY_SOURCE_TYPE);
                if (NumberUtils.isZero(id)) {
                    log.error("创建自动化思维问卷实例失败。{}", JsonUtils.toJsonString(submitSurveyReqVO));
                    return 0L;
                }
                submitSurveyReqVO.setId(id);
                return surveyService.submitSurveyForFlow(submitSurveyReqVO);
            }
        }
    }

    private String twoParseResult(String msg) {
        // 编译正则表达式
        Pattern pattern = Pattern.compile("\\[(.*?)\\]", Pattern.DOTALL);
        // 在输入文本中查找匹配项
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(); // 提取匹配到的JSON部分
        } else {
            log.error("二次识别也失败了。 {}", msg);
            return StringUtils.EMPTY;
        }

    }


    private String checkIsAutomaticResult(String message) {

        DictDataDO dictDataDO = dictDataService.parseDictData("ai_system_prompt", "check_is_automatic_result");
        if (dictDataDO == null) {
            log.error("dictDataDO is null, dictType:{} label:{}", "ai_system_prompt", "juvenile_problems_classification");
            return null;
        }

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage sysMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), dictDataDO.getValue());
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        messages.add(sysMessage);
        messages.add(chatMessage);
        ModelApiResponse chat = chat(messages, false);

        if (chat == null) return "系统提示未配置";

        if (!chat.isSuccess()) {
            log.error("chat problem:{} error:{}", messages, JSON.toJSONString(chat));
            return "解析失败：" + chat.getMsg();
        }

        ModelData data = chat.getData();
        List<Choice> choices = data.getChoices();
        if (choices == null || choices.isEmpty()) {
            log.error("chat problem:{} error:{}", messages, "choices为空");
            return "解析失败，choices为空";
        }
        return String.valueOf(choices.get(0).getMessage().getContent());
    }


    private SSEMsgDTO convertModelDataToString(ModelData modelData) {
        // Implement conversion logic
        SSEMsgDTO sseMsgDTO = new SSEMsgDTO();
        try {
            Choice choice = modelData.getChoices().get(0);
            sseMsgDTO.setContent(choice.getDelta().getContent());
            sseMsgDTO.setId(modelData.getId());
            sseMsgDTO.setFinished(BooleanUtil.parseBoolean(choice.getFinishReason()));
            sseMsgDTO.setCode(200);
            return sseMsgDTO;
        } catch (Exception e) {
            sseMsgDTO.setCode(500);
            sseMsgDTO.setContent("数据解析失败");
            sseMsgDTO.setMsg(e.getMessage());
            sseMsgDTO.setFinished(true);
        }
        return sseMsgDTO; // Simplified for this example
    }

    public static Flowable<ChatMessageAccumulator> mapStreamToAccumulator(Flowable<ModelData> flowable) {
        return flowable.map(chunk -> {
            return new ChatMessageAccumulator(chunk.getChoices().get(0).getDelta(), null, chunk.getChoices().get(0), chunk.getUsage(), chunk.getCreated(), chunk.getId());
        });
    }


    private ModelApiResponse chat(List<ChatMessage> messages, Boolean isStream) {
        System.out.println("chat input: " + JSON.toJSONString(messages));
        String requestIdTemplate = "chat-%s";
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model(Constants.ModelChatGLM4).stream(isStream).invokeMethod(Constants.invokeMethod).temperature(0.67f).messages(messages).requestId(requestId).build();

        ModelApiResponse modelApiResponse = client.invokeModelApi(chatCompletionRequest);

        log.info("chat response:{}", JSON.toJSONString(modelApiResponse));
        return modelApiResponse;
    }
}
