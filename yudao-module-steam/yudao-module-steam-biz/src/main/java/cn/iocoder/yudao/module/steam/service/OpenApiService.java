package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiCheckTradeUrlReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenYoupinApiReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
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
public class OpenApiService<T extends Serializable> {
    @Resource
    private DevAccountService accountService;
    private Validator validator;

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    public DevAccountDO apiCheck(OpenYoupinApiReqVo<T> openApiReqVo) {
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

    public static void main(String[] args) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        String pvtKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8pOJiazhjzdtJs0JzJ7Mo+YQYLXbqO9I0fC+JXYgLEHeo1Ax1tmabzXlJP+HzNOp1pqBWPzyBebQKiaYQymS4MvRxV/BPRrB8/aEDjV8AGdd1pEpFPS2rd0VvU9P2YXfuXxE3LM8tr9uefCCJ4FQZCP/7vHWyetuvR0uMPYKSltlqFgwZFKPVVzi+6MIOx7vHnJfD2FWyMVyReoDLCHnlktS84jii+jPkzlOsqJXZNRdGCYGkPBcKrW+GhEziDbnEbfWlw2hGaPzvXXzt0vXVtibWpLTX/g1qlTeC0vBBvlx5phnt/OXSSrG60Js+TWHc/cDw7cKQWiavZejI3CeDAgMBAAECggEAfS+ieBOVOU4r/u2x5D9tOnzS90R8jJakOWZMGYlzNXx5HBWUKy2fTDeADGcTZ9Uu3mJ0NqgCXB3Kp8+O38egLUjLRMhJ1iIgDuI1TukrKSL9A7nB+87MluQwtN9ZY0BtWUfHYekfl78DLFpNMZIn3PFHBuSa6pVzYg8bCHbtRp8m/areLbAC4R0i5E5UsPKn8ddKclgGS1Tgmxs8yBD93i5XJeoXVrTHBzOO8u1az7qpgS64Lc1MJnBEVMUI/iYrEt7z45UjzsAEKotwaw/LbZanAojY4wb3eSCKs4hkpR8FVsiA11hNUnWBwCHmnSAHzkMOYQC8g7qxVsCG51CnQQKBgQDdM6+2IqdvFQKy+KKezpdxsAnPfGIjwXPn5+r3dlEHqKFWmO8QQZ3IWHmk5aeOsnCa5DikgMG2awckPXlAnvbDnbf/nUzCpM0ariHvp5aNEzsdv2GfN15hsGCzKK40eAJi9NZ4eT8ALsAI51lF/sLuy8z8Ids5S22puUK5knGZ4wKBgQDaUgTcJ5Zvowi5lrquGbbBkZsysYGNRQ3WzGWG2dwNqgVKz4QEeaisJ/LdHHuTaj02RYZuA7B4+4staAhA2VX/EBPxWCzcOKPXsQhxCPJi7JQqmC2e4mRVjsCokGWQXiogsvFWdDkTP0IXnR7GhgZxvDGJPsyTJMcRlNgPVt0t4QKBgB/EgIOjznABkHWrh49PFCjbo00NC/semUrA39nSQCjdau5I4GxxP/u52R55bOrtbYaRKCFX7HoKPOhTe8pwCfhl+jrXmGKL0Hj4cR897j0sedz300lOZluZPQn92abnZVBY4UREBWw9So78yrFmuRAabMH5CsbbslAhrxd/lJkJAoGBAKZSpfEzF6ClDBiXhFDuthRx4VKVeKUvXoOt5AsAHm0qgi2kOmdOZ/n/1T4uXNbJsSiPfwKBPQhuWnGVN/RvntxaW1caXdLIM8o2zL+QmVhT8+0fUmIhB19HCe9hUn7RvjZ7HPFISdMn9ioXQULtCCvNu89bUG8pLZ9vTcsh2g8BAoGAI91+pPp3gE0mZeXOyqK0M4/aKnj+07viEsg+XjCDYxgEGbLN/u0fOaw+tmmQw4mxMm1mxuva7H1S2mCE3StMKjuRmiGpxAGq8S08g9lqFeM552Y2vV9wLUiGKybDJKdbaSr3t4tvv/kp/1VAOBmpG+BG/ubF/X1uecg8L8CZDv0=";
        String pubKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvKTiYms4Y83bSbNCcyezKPmEGC126jvSNHwviV2ICxB3qNQMdbZmm815ST/h8zTqdaagVj88gXm0CommEMpkuDL0cVfwT0awfP2hA41fABnXdaRKRT0tq3dFb1PT9mF37l8RNyzPLa/bnnwgieBUGQj/+7x1snrbr0dLjD2CkpbZahYMGRSj1Vc4vujCDse7x5yXw9hVsjFckXqAywh55ZLUvOI4ovoz5M5TrKiV2TUXRgmBpDwXCq1vhoRM4g25xG31pcNoRmj871187dL11bYm1qS01/4NapU3gtLwQb5ceaYZ7fzl0kqxutCbPk1h3P3A8O3CkFomr2XoyNwngwIDAQAB";
        OpenYoupinApiReqVo<ApiCheckTradeUrlReqVo> openYoupinApiReqVo=new OpenYoupinApiReqVo<ApiCheckTradeUrlReqVo>()
                .setTimestamp(format).setAppKey("5293902")
                .setData(new ApiCheckTradeUrlReqVo().setTradeLinks("https://steamcommunity.com/tradeoffer/new/?partner=1432096359&token=giLGhxtN"));

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
}
