package cn.iocoder.yudao.module.steam.service;

import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pay.controller.app.wallet.vo.wallet.AppPayWalletRespVO;
import cn.iocoder.yudao.module.pay.convert.wallet.PayWalletConvert;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.Test;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
import cn.iocoder.yudao.module.steam.utils.DevAccountContextHolder;
import cn.iocoder.yudao.module.steam.utils.DevAccountUtils;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 开放平台接口
 * @author glzaboy
 */
@Service
@Slf4j
public class OpenApiService {
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

    public String dispatch(OpenApiReqVo openApiReqVo) throws ServiceException {
        DevAccountDO devAccountDO = accountService.selectByUserName(openApiReqVo.getUserName(), UserTypeEnum.MEMBER);
        if(Objects.isNull(devAccountDO)){
            throw new ServiceException(new ErrorCode(1,"接口用户不存在，如有问题请联系客服"));
        }

        Method[] declaredMethods = this.getClass().getDeclaredMethods();
        List<Method> collect = Arrays.stream(declaredMethods).filter(item -> item.getName().equals(openApiReqVo.getMethod())).collect(Collectors.toList());
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
                    Object invoke = method.invoke(getSelf());
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
                    Object invoke = method.invoke(getSelf(), o);
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
    private OpenApiService getSelf() {
        return SpringUtil.getBean(getClass());
    }
    public String tese(@Valid Test o){
        return "Hello"+o.getPassword();
    }
    @Resource
    private PayWalletService payWalletService;

    /**
     * api余额接口
     * @return
     */
    public CommonResult<AppPayWalletRespVO> getPayWallet() {
        DevAccountDO devAccount = DevAccountContextHolder.getDevAccount();
        PayWalletDO wallet = payWalletService.getOrCreateWallet(devAccount.getUserId(), devAccount.getUserType());
        return success(PayWalletConvert.INSTANCE.convert(wallet));
    }
}
