package cn.iocoder.yudao.module.steam.service.alipay;

import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.pay.core.client.PayClient;
import cn.iocoder.yudao.framework.pay.core.client.impl.alipay.AlipayPayClientConfig;
import cn.iocoder.yudao.framework.pay.core.enums.channel.PayChannelEnum;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.pay.dal.dataobject.channel.PayChannelDO;
import cn.iocoder.yudao.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import cn.iocoder.yudao.module.steam.controller.app.alipay.vo.CreateIsvVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.enums.PayApiCode;
import cn.iocoder.yudao.module.steam.service.alipayisv.AlipayIsvExtService;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenInviteOrderCreateRequest;
import com.alipay.api.request.AlipayOpenInviteOrderQueryRequest;
import com.alipay.api.response.AlipayOpenInviteOrderCreateResponse;
import com.alipay.api.response.AlipayOpenInviteOrderQueryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PayClientService {
    private ObjectMapper objectMapper;
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    private Validator validator;

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Resource
    private PayChannelService channelService;

    private static final Long PAY_WITHDRAWAL_APP_ID = 10L;

    private AlipayIsvExtService alipayIsvExtService;

    @Resource
    private PayNoRedisDAO noRedisDAO;

    @Autowired
    public void setAlipayIsvExtService(AlipayIsvExtService alipayIsvExtService) {
        this.alipayIsvExtService = alipayIsvExtService;
    }
    private PayChannelDO getPayConfig(){
        return channelService.validPayChannel(PAY_WITHDRAWAL_APP_ID, PayChannelEnum.ALIPAY_PC.getCode());
    }
    private DefaultAlipayClient getPayClient(){
        PayChannelDO channel = getPayConfig();
        PayClient client = channelService.getPayClient(channel.getId());
        return client.getDefaultAliPayClient();
    }

    /**
     * 签约授权页面申请（获取签约链接）
     * @param req
     * @return
     */
    public String createIsv(CreateIsvVo req, LoginUser loginUser) {
        req.setIsvBizId(noRedisDAO.generate("ISV"));
        AlipayIsvDO alipayIsvDO = alipayIsvExtService.initIsv(req,loginUser);
        DefaultAlipayClient payClient = getPayClient();
        AlipayOpenInviteOrderCreateRequest request = new AlipayOpenInviteOrderCreateRequest();
        request.setBizContent(JacksonUtils.writeValueAsString(req));
        try{
            AlipayOpenInviteOrderCreateResponse response = payClient.pageExecute(request);
            checkAlipayResponse(response);
            String submitFormData = response.getBody();
            return submitFormData;
        }catch (AlipayApiException e){
            throw new ServiceException(PayApiCode.PAY_ERROR);
        }
    }
    public void payNotify(HttpServletRequest httpServletRequest,String msgMethod,String notifyType){
        String method= StringUtils.hasText(msgMethod)?msgMethod:notifyType;
        if(Objects.isNull(method)){
            throw new ServiceException(PayApiCode.PAY_ERROR);
        }
        dispatch(method,httpServletRequest.getParameter("biz_content"));
        switch (method){
            case "open_app_auth_notify":
                openAppAuthNotify();
                break;

        }

    }
    public Object dispatch(String disMethod,String data){
        Method[] declaredMethods = this.getClass().getDeclaredMethods();
        List<Method> collect = Arrays.stream(declaredMethods).filter(item -> item.getName().equals(disMethod)).collect(Collectors.toList());
        if(collect.size()!=1){
            log.error("方法找不到{}", JSON.toJSON(disMethod));
            throw new ServiceException(new ErrorCode(1,"方法找不到"));
        }
        Method method = collect.get(0);
        if(method.getParameterCount()>1){
            log.error("方法参数大于1不支持调用{}", JSON.toJSON(disMethod));
            throw new ServiceException(new ErrorCode(1,"方法找不到"));
        }
        try {
            Object execute;
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            if(genericParameterTypes.length==0){
                execute = DevAccountUtils.tenantExecute(1L, () -> {
                    Object invoke = method.invoke(getSelf());
                    return invoke;
                });
            }else{
                Type genericParameterType = genericParameterTypes[0];
                Object o = objectMapper.readValue(data, objectMapper.getTypeFactory().constructType(genericParameterType));
                Set<ConstraintViolation<Object>> validate = validator.validate(o);
                if(!validate.isEmpty()){
                    throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
                }
                execute = DevAccountUtils.tenantExecute(1L, () -> {
                    Object invoke = method.invoke(getSelf(), o);
                    return invoke;
                });
            }
            log.info("开发接口调用结果{}",objectMapper.writeValueAsString(execute));
            return execute;
        } catch (JsonProcessingException e) {
            log.error("数据反序列化失败{}", JSON.toJSON(data));
            throw new ServiceException(new ErrorCode(1,"数据反序列化失败"));
        }
    }

    /**
     * 签约授权成功异步消息通知
     */
    private void openAppAuthNotify(){

    }

    /**
     * 回调签名验证
     * @param httpServletRequest
     */
    private boolean checkNotifySign(HttpServletRequest httpServletRequest){
        DefaultAlipayClient payClient = getPayClient();
        Map<String,String> params = new HashMap<>();
        Map requestParams = httpServletRequest.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext ();) {
            String name =  ( String )iter.next();
            String[] values = (String[])requestParams.get(name);
            String valueStr="";
            for(int i = 0;i < values.length; i++){
                valueStr = (i== values.length-1)?valueStr+values[i]:valueStr+values[i] + ",";
            }
            params.put(name,valueStr);
        }
        PayChannelDO payConfig = getPayConfig();
        AlipayPayClientConfig config = (AlipayPayClientConfig)payConfig.getConfig();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", "crt");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            fileOutputStream.write(config.getAlipayPublicCertContent().getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
            boolean rsa2 = AlipaySignature.verifyV1(params, tempFile.getAbsolutePath(), "UTF-8", "RSA");
            log.info("校验结果{}",rsa2);
            return rsa2;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("签名验证失败{}",e.getMessage());
            throw new ServiceException(PayApiCode.PAY_SIGN_ERR);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("签名验证失败{}",e.getErrMsg()+e.getErrCode()+e.getMessage());
            throw new ServiceException(PayApiCode.PAY_SIGN_ERR);
        } finally {
            if(Objects.nonNull(tempFile)){
                tempFile.delete();
            }
        }
    }



    /**
     * 签约授权页面申请（获取签约链接）
     * @param alipayIsvDO
     * @return
     */
    public AlipayIsvDO checkIsv(AlipayIsvDO alipayIsvDO) {
        DefaultAlipayClient payClient = getPayClient();
        AlipayOpenInviteOrderQueryRequest  request = new AlipayOpenInviteOrderQueryRequest();
        Map<String,String> isvquery=new HashMap<>();
        isvquery.put("isv_biz_id",alipayIsvDO.getIsvBizId());
        request.setBizContent(JacksonUtils.writeValueAsString(isvquery));
        try{
            AlipayOpenInviteOrderQueryResponse response = payClient.pageExecute(request);
            checkAlipayResponse(response);
            alipayIsvDO.setMerchantPid(response.getMerchantPid());
            alipayIsvDO.setSignStatusList(response.getSignStatusList());
            if(Objects.nonNull(response.getSignStatusList().get(0))){
                alipayIsvDO.setSignStatus(response.getSignStatusList().get(0).getStatus());
            }
            return alipayIsvDO;
        }catch (AlipayApiException e){
            throw new ServiceException(PayApiCode.PAY_ERROR);
        }
    }
    private void checkAlipayResponse(AlipayResponse response){
        log.error("alipay checkAlipayResponse{}", JsonUtils.toJsonString(response));
        if(Objects.isNull(response) && Objects.isNull(response.getBody())){
            throw new ServiceException(OpenApiCode.TO_MANY);
        }
        if(response.isSuccess()){
            return;
        }
        throw new ServiceException(Integer.valueOf(response.getCode()),response.getMsg()+"--"+response.getSubMsg());
    }
    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private PayClientService getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
