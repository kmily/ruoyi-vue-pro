package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenYoupinApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.buy.CreateReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
import cn.iocoder.yudao.module.steam.service.steam.YouPingOrder;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
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

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    public <T extends Serializable> DevAccountDO apiCheck(OpenYoupinApiReqVo<T> openApiReqVo) {
        Set<ConstraintViolation<OpenYoupinApiReqVo<T>>> validate = validator.validate(openApiReqVo);
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
     * @param url
     * @param openYoupinApiReqVo
     * @param classic
     * @param <T>
     * @param <E>
     * @return
     */
    public <T extends Serializable,E extends Serializable> E requestUU(String url,OpenYoupinApiReqVo<T> openYoupinApiReqVo,Class<E> classic){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ConfigDO configApiKey = configService.getConfigByKey("uu.appKey");
        ConfigDO configByKey = configService.getConfigByKey("uu.key1");
        ConfigDO configByKey2 = configService.getConfigByKey("uu.key2");
        ConfigDO configByKey3 = configService.getConfigByKey("uu.key3");
        ConfigDO configByKey4 = configService.getConfigByKey("uu.key4");
        String key=configByKey.getValue()+configByKey2.getValue()+configByKey3.getValue()+configByKey4.getValue();
        openYoupinApiReqVo.setTimestamp(simpleDateFormat.format(new Date()));
        openYoupinApiReqVo.setAppKey(configApiKey.getValue());
        sign(openYoupinApiReqVo,key);
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.url(url);
        builder.method(HttpUtil.Method.JSON);
        builder.postObject(openYoupinApiReqVo);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build());
        E json = sent.json(classic);
        return json;
    }
    /**
     * 参数签名兼容有品
     * @param openYoupinApiReqVo
     * @param pubKey
     * @param <T>
     * @throws Exception
     */
    public <T extends Serializable> void checkSign(OpenYoupinApiReqVo<T> openYoupinApiReqVo,String pubKey) {
        try{
            //时间检测
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = simpleDateFormat.parse(openYoupinApiReqVo.getTimestamp());
            long l = (System.currentTimeMillis() - parse.getTime()) / 1000;
            if(l>600){
                throw new ServiceException(OpenApiCode.CHECK_SIGN_ERROR);
            }


            Map<String, Object> params = new HashMap<>();
            params.put("timestamp",openYoupinApiReqVo.getTimestamp());
            params.put("appKey",openYoupinApiReqVo.getAppKey());
            T data = openYoupinApiReqVo.getData();
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
            boolean b = RSAUtils.verifyByPublicKey(stringBuilder.toString().getBytes(), pubKey, openYoupinApiReqVo.getSign());
            if(!b){
                throw new ServiceException(OpenApiCode.CHECK_SIGN_ERROR);
            }
            log.info("解密结果{}",b);
        }catch (InvalidKeySpecException | IllegalAccessException | InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServiceException(OpenApiCode.CHECK_SIGN_ERROR);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ServiceException(OpenApiCode.CHECK_SIGN_ERROR);
        }
    }
    /**
     * 参数签名兼容有品
     * @param openYoupinApiReqVo
     * @param priKey
     * @param <T>
     * @throws Exception
     */
    public <T extends Serializable> void sign(OpenYoupinApiReqVo<T> openYoupinApiReqVo,String priKey) {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("timestamp",openYoupinApiReqVo.getTimestamp());
            params.put("appKey",openYoupinApiReqVo.getAppKey());
            T data = openYoupinApiReqVo.getData();
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
                openYoupinApiReqVo.setSign(sign);
                log.info("签名sign:{}", JacksonUtils.writeValueAsString(openYoupinApiReqVo));
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
