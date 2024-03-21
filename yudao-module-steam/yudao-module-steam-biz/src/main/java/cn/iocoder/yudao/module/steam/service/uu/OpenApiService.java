package cn.iocoder.yudao.module.steam.service.uu;

import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 开放平台接口
 * @author glzaboy
 */
@Service
@Slf4j
public class OpenApiService {
    @Resource
    private DevAccountService accountService;
    private Validator validator;

    @Resource
    private ConfigService configService;

    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    public <T extends Serializable> DevAccountDO apiCheck(OpenApiReqVo<T> openApiReqVo) {
        log.info("收到开发平台接口调用{}", JsonUtils.toJsonString(openApiReqVo));
        Set<ConstraintViolation<OpenApiReqVo<T>>> validate = validator.validate(openApiReqVo);
        if(!validate.isEmpty()){
            throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
        }
        DevAccountDO devAccountDO = accountService.selectByUserName(openApiReqVo.getAppKey(), UserTypeEnum.MEMBER);
        if(Objects.isNull(devAccountDO)){
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        if(CommonStatusEnum.isDisable(devAccountDO.getStatus())){
            throw new ServiceException(OpenApiCode.DISABLED);
        }
        checkSign(openApiReqVo,devAccountDO.getApiPublicKey());
        return devAccountDO;
    }
    /**
     * 有品请求公共实现类
     * @param openApiReqVo 有品传入参数，只需要管data
     * @param <T> 入参类型
     * @return 签名后的json对象
     */
    public <T extends Serializable> OpenApiReqVo<T> requestUUSign(OpenApiReqVo<T> openApiReqVo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ConfigDO configApiKey = configService.getConfigByKey("uu.appKey");
        ConfigDO configByKey = configService.getConfigByKey("uu.key1");
        ConfigDO configByKey2 = configService.getConfigByKey("uu.key2");
        ConfigDO configByKey3 = configService.getConfigByKey("uu.key3");
        ConfigDO configByKey4 = configService.getConfigByKey("uu.key4");
        String key=configByKey.getValue()+configByKey2.getValue()+configByKey3.getValue()+configByKey4.getValue();
        openApiReqVo.setTimestamp(simpleDateFormat.format(new Date()));
        openApiReqVo.setAppKey(configApiKey.getValue());
        sign(openApiReqVo,key);
        return openApiReqVo;
    }
    /**
     * 有品请求公共实现类
     * @param openApiReqVo 有品传入参数，只需要管data
     * @param <T> 入参类型
     * @return 签名后的json对象
     */
    public <T extends Serializable> OpenApiReqVo<T> testSign(OpenApiReqVo<T> openApiReqVo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String key="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDCpzqpYg1NOXUgH/GTSRyYmPqatoTaeZ+VK+zv6Z9zP8A/psy4Yc36UX23uFLq5IqH3DphGg9O+8hFGqmihkdI2lROeYsddv1uFxXC445Jf4fgPQQRLGKpe4ok4EA+4QPuXdKXm93xrKhLL7C/XORCy0qHYAUqDR/mEbo9mKGa9piwOg9nmYPP2rRWrYPywB0Yfd6ZPPox9U1Z7Lgy1JOmOFIRedJVPwOBnixIhvMGRuD09ti9L6MxcR+lZ4wfPNtubVANT+XdgOw22lBjZEpyW0+SHiybeMKhhc5SNKXHqaiSwrznBZH8YZMAROF1oqBCbMrjVZzH5l/9K8L1ta2VAgMBAAECggEAf8lpKWnFcb0Wt1BBN3/31fYYDxljfkn6CeQcWCP3GaHwg9js7N7Ialy1O7c2OB5xE1/ws254PlHs1/D5DEk64wjx79K7EUzcczmUf60D7BxdB0kHMn8BBmKj/jF5+82c1w+hAQbCXbYLhdB5KCfDclYjR1wyB7k2B8P7kBRzg1baXae506m4Y0DRx+KRHZ1tE6yqmzVbedF2jpZ9ZmI0EkOBayGQTuq2UphTiMiE5MHG45rGSZOHwbrN1zA1mvuYBF8NRKdAmvaD53rab6gcccBbgo3bQo/4JeLpY16NZ8shRrIoY6lkV90qdTLc/sdXHbwLaGT305Jdj04LMwIQAQKBgQDwePja68u6zO46ylNl+BRDEBYcTntLCrjE9Bkns8LGE2fxyIWyLHfjyQ2gCdG7x0yCHK1tbY32XvHMgb/oAAeVMOCKqoX7cRHG3oKBzRvYHPtW7xW9j/0s0B4/Sluo0ROBWpHVXZMuBMXsbNJW0DPK8KEuh/oUI0aDiAwNNDj7QQKBgQDPONxXQpDhBT+/R69denEkqkl+prj7IUHER6os1nfMTyzLxuOWZe11nBtpfUeOX2cFzGBmPUtRfdjRXb12KukwiNAqLvi5YYnnkgsP7cJUSTU91PVn/1NAMijrb2GBERjoX/cfMiKogXPXl13emu9SYrj96cdNR+596UwbbQ8BVQKBgQCEsuz4uegJ5C6OaLoO6hAcVdMtua1V4svFe8Ip44vXDDxu8y27/cgG/hqzttdzHO6+Wh7l5O/TBd++79a7qtCEZp1yR9l5wJKDwKQaBtqXrp6QAY1otv2J7irS2DCufvmZhyY0rNecLGwgJIkJ+Qirs4/ugoDg/fpaeQfVfXz4QQKBgDwV9bYLpCzLM9fH9m55gXMrOVJTD5Ip64L5cLu9mFESqB+SVQ2YEommsUAeRnBe82V4BH/AyCUiA4t5zeUEvkcHdKy2oAJI6Q7PhwugWlfMPnbmWB5Gp6IWqUzTKAefqIRAx0wxYmFP4AIkbuCMNlbCL3fYoKSk9d9mlV3iepZdAoGBANIyfy2FZkxTWkmKtB3zqKWiEBe0lFENt7lFLEqDh+bpny4lBJCULBjyLJ/DgfVNeFQlTDUszJ05psZ7O+mKhefzeWX1At/kx7w7Ep8/HERoMFoR42Km5buPuOFtvdaPRoehcF4jnLzzladAxKrWPCdMdEMx24ggaEhi5k5F27Tg";
        openApiReqVo.setTimestamp(simpleDateFormat.format(new Date()));
        openApiReqVo.setAppKey(openApiReqVo.getAppKey());
        sign(openApiReqVo,key);
        return openApiReqVo;
    }
    /**
     * 有品请求公共实现类
     * @param url 接口地址
     * @param openApiReqVo 有品传入参数，只需要管data
     * @param classic 返回数据格式
     * @param <T> 入参类型
     * @param <E> 出参类型
     * @return UU数据结果
     */
    public <T extends Serializable,E extends Serializable> ApiResult<E> requestUU(String url, OpenApiReqVo<T> openApiReqVo, Class<E> classic){

        OpenApiReqVo<T> tOpenApiReqVo = requestUUSign(openApiReqVo);
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(url);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(tOpenApiReqVo);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        ApiResult json = sent.json(ApiResult.class);
        Object data = json.getData();
        try {
            ApiResult<E> apiResult=new ApiResult<>();
            E e1 = objectMapper.readValue(objectMapper.writeValueAsString(data), classic);
            apiResult.setData(e1);
            apiResult.setMsg(json.getMsg());
            apiResult.setTimestamp(json.getTimestamp());
            apiResult.setCode(json.getCode());
            return apiResult;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServiceException(-1,"格式转换出错");
        }
    }
    /**
     * 参数签名兼容有品
     * 出现错误时会抛出异常
     * @param openApiReqVo 入参
     * @param pubKey 公钥
     * @param <T> 入参类型
     */
    public <T extends Serializable> void checkSign(OpenApiReqVo<T> openApiReqVo, String pubKey) {
        try{
            //时间检测
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = simpleDateFormat.parse(openApiReqVo.getTimestamp());
            long l = (System.currentTimeMillis() - parse.getTime()) / 1000;
            if(l>600){
                throw new ServiceException(OpenApiCode.CHECK_SIGN_ERROR);
            }
            Map<String, Object> params = new HashMap<>();
            params.put("timestamp",openApiReqVo.getTimestamp());
            params.put("appKey",openApiReqVo.getAppKey());
            T data = openApiReqVo.getData();
            if(Objects.nonNull(data)){
                Field[] declaredFields = data.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    String name = declaredField.getName();
                    declaredField.setAccessible(true);
                    params.put(name,declaredField.get(data));
                }
            }

            // 第一步：检查参数是否已经排序
            String[] keys = params.keySet().toArray(new String[0]);
            Arrays.sort(keys);
            // 第二步：把所有参数名和参数值串在一起
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : keys) {
                Object value = params.get(key);
                if (Objects.nonNull(value)) {
                    stringBuilder.append(key).append(JacksonUtils.writeValueAsString(value));
                }
            }
            //采用私钥签名
            boolean b = RSAUtils.verifyByPublicKey(stringBuilder.toString().getBytes(), pubKey, openApiReqVo.getSign());
            log.info("签名比较结果{}",b);
            if(!b){
                throw new ServiceException(OpenApiCode.CHECK_SIGN_ERROR);
            }
        }catch (InvalidKeySpecException | IllegalAccessException | InvalidKeyException | SignatureException | NoSuchAlgorithmException | ParseException e) {
            e.printStackTrace();
            log.error("解密出错原因{}",e.getMessage());
            throw new ServiceException(OpenApiCode.CHECK_SIGN_ERROR);
        }
    }
    /**
     * 参数签名兼容有品
     * @param openApiReqVo 入参
     * @param priKey 私钥
     * @param <T> 入参类型
     */
    private  <T extends Serializable> void sign(OpenApiReqVo<T> openApiReqVo, String priKey) {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("timestamp",openApiReqVo.getTimestamp());
            params.put("appKey",openApiReqVo.getAppKey());
            T data = openApiReqVo.getData();
            if(Objects.nonNull(data)){
                Field[] declaredFields = data.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    String name = declaredField.getName();
                    declaredField.setAccessible(true);
                    params.put(name,declaredField.get(data));
                }
            }

            // 第一步：检查参数是否已经排序
            String[] keys = params.keySet().toArray(new String[0]);
            Arrays.sort(keys);
            // 第二步：把所有参数名和参数值串在一起
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : keys) {
                Object value = params.get(key);
                if (Objects.nonNull(value)) {
                    stringBuilder.append(key).append(JacksonUtils.writeValueAsString(value));
                }
            }
            try {
                String sign = RSAUtils.signByPrivateKey(stringBuilder.toString().getBytes(), priKey);
                openApiReqVo.setSign(sign);
                log.info("签名sign:{}", JacksonUtils.writeValueAsString(openApiReqVo));
            } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException e) {
                e.printStackTrace();
                throw new ServiceException(OpenApiCode.SIGN_ERROR);
            }
        }catch (IllegalAccessException e){
            log.error("检查签名出错类不可访问{}",e.getMessage());
            throw new ServiceException(OpenApiCode.SIGN_ERROR);
        }
    }
}
