package cn.iocoder.yudao.module.steam.service;

import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.steam.controller.app.AppApiController;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenYoupinApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.TestTrade;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
import cn.iocoder.yudao.module.steam.utils.JacksonUtils;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 开放平台接口
 * @author glzaboy
 */
@Service
@Slf4j
public class OpenApiService<T extends Serializable> {
    @Resource
    private DevAccountService accountService;

    private ObjectMapper objectMapper;
    private Validator validator;

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    public DevAccountDO apiCheck(OpenYoupinApiReqVo<T> openApiReqVo) {
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

    public static void main(String[] args) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        String pvtKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8pOJiazhjzdtJs0JzJ7Mo+YQYLXbqO9I0fC+JXYgLEHeo1Ax1tmabzXlJP+HzNOp1pqBWPzyBebQKiaYQymS4MvRxV/BPRrB8/aEDjV8AGdd1pEpFPS2rd0VvU9P2YXfuXxE3LM8tr9uefCCJ4FQZCP/7vHWyetuvR0uMPYKSltlqFgwZFKPVVzi+6MIOx7vHnJfD2FWyMVyReoDLCHnlktS84jii+jPkzlOsqJXZNRdGCYGkPBcKrW+GhEziDbnEbfWlw2hGaPzvXXzt0vXVtibWpLTX/g1qlTeC0vBBvlx5phnt/OXSSrG60Js+TWHc/cDw7cKQWiavZejI3CeDAgMBAAECggEAfS+ieBOVOU4r/u2x5D9tOnzS90R8jJakOWZMGYlzNXx5HBWUKy2fTDeADGcTZ9Uu3mJ0NqgCXB3Kp8+O38egLUjLRMhJ1iIgDuI1TukrKSL9A7nB+87MluQwtN9ZY0BtWUfHYekfl78DLFpNMZIn3PFHBuSa6pVzYg8bCHbtRp8m/areLbAC4R0i5E5UsPKn8ddKclgGS1Tgmxs8yBD93i5XJeoXVrTHBzOO8u1az7qpgS64Lc1MJnBEVMUI/iYrEt7z45UjzsAEKotwaw/LbZanAojY4wb3eSCKs4hkpR8FVsiA11hNUnWBwCHmnSAHzkMOYQC8g7qxVsCG51CnQQKBgQDdM6+2IqdvFQKy+KKezpdxsAnPfGIjwXPn5+r3dlEHqKFWmO8QQZ3IWHmk5aeOsnCa5DikgMG2awckPXlAnvbDnbf/nUzCpM0ariHvp5aNEzsdv2GfN15hsGCzKK40eAJi9NZ4eT8ALsAI51lF/sLuy8z8Ids5S22puUK5knGZ4wKBgQDaUgTcJ5Zvowi5lrquGbbBkZsysYGNRQ3WzGWG2dwNqgVKz4QEeaisJ/LdHHuTaj02RYZuA7B4+4staAhA2VX/EBPxWCzcOKPXsQhxCPJi7JQqmC2e4mRVjsCokGWQXiogsvFWdDkTP0IXnR7GhgZxvDGJPsyTJMcRlNgPVt0t4QKBgB/EgIOjznABkHWrh49PFCjbo00NC/semUrA39nSQCjdau5I4GxxP/u52R55bOrtbYaRKCFX7HoKPOhTe8pwCfhl+jrXmGKL0Hj4cR897j0sedz300lOZluZPQn92abnZVBY4UREBWw9So78yrFmuRAabMH5CsbbslAhrxd/lJkJAoGBAKZSpfEzF6ClDBiXhFDuthRx4VKVeKUvXoOt5AsAHm0qgi2kOmdOZ/n/1T4uXNbJsSiPfwKBPQhuWnGVN/RvntxaW1caXdLIM8o2zL+QmVhT8+0fUmIhB19HCe9hUn7RvjZ7HPFISdMn9ioXQULtCCvNu89bUG8pLZ9vTcsh2g8BAoGAI91+pPp3gE0mZeXOyqK0M4/aKnj+07viEsg+XjCDYxgEGbLN/u0fOaw+tmmQw4mxMm1mxuva7H1S2mCE3StMKjuRmiGpxAGq8S08g9lqFeM552Y2vV9wLUiGKybDJKdbaSr3t4tvv/kp/1VAOBmpG+BG/ubF/X1uecg8L8CZDv0=";
        String pubKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvKTiYms4Y83bSbNCcyezKPmEGC126jvSNHwviV2ICxB3qNQMdbZmm815ST/h8zTqdaagVj88gXm0CommEMpkuDL0cVfwT0awfP2hA41fABnXdaRKRT0tq3dFb1PT9mF37l8RNyzPLa/bnnwgieBUGQj/+7x1snrbr0dLjD2CkpbZahYMGRSj1Vc4vujCDse7x5yXw9hVsjFckXqAywh55ZLUvOI4ovoz5M5TrKiV2TUXRgmBpDwXCq1vhoRM4g25xG31pcNoRmj871187dL11bYm1qS01/4NapU3gtLwQb5ceaYZ7fzl0kqxutCbPk1h3P3A8O3CkFomr2XoyNwngwIDAQAB";
        OpenYoupinApiReqVo<TestTrade> openYoupinApiReqVo=new OpenYoupinApiReqVo<TestTrade>()
                .setTimestamp(format).setAppKey("5293902");
                //.setData(new TestTrade().setTradeLinks("https://steamcommunity.com/tradeoffer/new/?partner=12345678912&token=LBPW679"));

        sign(openYoupinApiReqVo, pvtKey);
        checkSign(openYoupinApiReqVo,pubKey);
    }
    /**
     * 参数签名兼容有品
     * @param openYoupinApiReqVo
     * @param pubKey
     * @param <T>
     * @throws Exception
     */
    public static <T extends Serializable> void checkSign(OpenYoupinApiReqVo<T> openYoupinApiReqVo,String pubKey) {
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
    public static <T extends Serializable> void sign(OpenYoupinApiReqVo<T> openYoupinApiReqVo,String priKey) {
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
    public String dispatch(OpenApiReqVo openApiReqVo) throws ServiceException {
        DevAccountDO devAccountDO = accountService.selectByUserName(openApiReqVo.getUserName(), UserTypeEnum.MEMBER);
        if(Objects.isNull(devAccountDO)){
            throw new ServiceException(new ErrorCode(1,"接口用户不存在，如有问题请联系客服"));
        }
        if(CommonStatusEnum.isDisable(devAccountDO.getStatus())){
            throw new ServiceException(new ErrorCode(1,"接口已禁用"));
        }

        Method[] declaredMethods = AppApiController.class.getDeclaredMethods();
//        List<Method> collect = Arrays.stream(declaredMethods).filter(item -> item.getName().equals(openApiReqVo.getMethod())).collect(Collectors.toList());
        List<Method> collect = Arrays.stream(declaredMethods).filter(item -> {
            PostMapping postMapping = AnnotationUtils.getAnnotation(item, PostMapping.class);
            if(Objects.isNull(postMapping)){
                return false;
            }
            return Arrays.stream(postMapping.value()).filter(path->path.equals(openApiReqVo.getMethod())).count()>0;

        }).collect(Collectors.toList());
        if(collect.size()!=1){
            log.error("方法找不到{}", JSON.toJSON(openApiReqVo));
            throw new ServiceException(new ErrorCode(1,"方法找不到"));
        }
        Method method = collect.get(0);
        if(method.getParameterCount()>1){
            log.error("方法参数大于1不支持调用{}", JSON.toJSON(openApiReqVo));
            throw new ServiceException(new ErrorCode(1,"方法找不到"));
        }

        //解密接口数据
        try {
            Object execute;
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            if(genericParameterTypes.length==0){
                execute = DevAccountUtils.execute(devAccountDO, () -> {
                    Object invoke = method.invoke(getBean(AppApiController.class));
                    return invoke;
                });
            }else{
                Type genericParameterType = genericParameterTypes[0];
                String s = RSAUtils.decryptByPrivateKey(openApiReqVo.getData(), devAccountDO.getApiPrivateKey());
                Object o = objectMapper.readValue(s, objectMapper.getTypeFactory().constructType(genericParameterType));
                Set<ConstraintViolation<Object>> validate = validator.validate(o);
                if(!validate.isEmpty()){
                    throw new ServiceException(-1,validate.toString());
//                throw new ConstraintViolationException(validate);
                }
                execute = DevAccountUtils.execute(devAccountDO, () -> {
                    Object invoke = method.invoke(getBean(AppApiController.class), o);
                    return invoke;
                });
            }
            log.info("开发接口调用结果{}",objectMapper.writeValueAsString(execute));
            return RSAUtils.encryptByPublicKey(objectMapper.writeValueAsString(execute), devAccountDO.getApiPublicKey());
        } catch (InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            log.error("接口解密失败{}", JSON.toJSON(openApiReqVo));
            throw new ServiceException(new ErrorCode(1,"接口解密失败"));
        } catch (JsonProcessingException e) {
            log.error("数据反序列化失败{}", JSON.toJSON(openApiReqVo));
            throw new ServiceException(new ErrorCode(1,"数据反序列化失败"));
//        } catch (InvocationTargetException |IllegalAccessException  e) {
//            log.error("执行接口异常{},{}", openApiReqVo.getMethod(),JSON.toJSON(openApiReqVo));
//            throw new ServiceException(new ErrorCode(1,"执行接口异常"));
        }
    }
    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private AppApiController getBean(Class<AppApiController> clazz) {
        return SpringUtil.getBean(clazz);
    }
}
