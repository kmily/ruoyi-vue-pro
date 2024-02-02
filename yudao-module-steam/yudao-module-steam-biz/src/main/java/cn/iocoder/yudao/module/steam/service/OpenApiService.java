package cn.iocoder.yudao.module.steam.service;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo.DevAccountPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.OpenApiReqVo;
import cn.iocoder.yudao.module.steam.controller.app.vo.Test;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.service.devaccount.DevAccountService;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class OpenApiService {
    @Resource
    private DevAccountService accountService;
    @Autowired
    private ObjectMapper objectMapper;
    public String despatch(OpenApiReqVo openApiReqVo) throws ServiceException {
        DevAccountDO devAccountDO = accountService.selectByUserName(openApiReqVo.getUserName());
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
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Type genericParameterType = genericParameterTypes[0];

        //解密接口数据
        try {
            String s = RSAUtils.decryptByPrivateKey(openApiReqVo.getData(), devAccountDO.getApiPrivateKey());
            Object o = objectMapper.readValue(s, objectMapper.getTypeFactory().constructType(genericParameterType));
            Object invoke = method.invoke(this,o);
            String s1 = RSAUtils.encryptByPublicKey(objectMapper.writeValueAsString(invoke), devAccountDO.getApiPublicKey());
            return s1;
        } catch (InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            log.error("接口解密失败{}", JSON.toJSON(openApiReqVo));
            throw new ServiceException(new ErrorCode(1,"接口解密失败"));
        } catch (JsonProcessingException e) {
            log.error("数据反序列化失败{}", JSON.toJSON(openApiReqVo));
            throw new ServiceException(new ErrorCode(1,"数据反序列化失败"));
        } catch (InvocationTargetException |IllegalAccessException  e) {
            log.error("执行接口异常{},{}", openApiReqVo.getMethod(),JSON.toJSON(openApiReqVo));
            throw new ServiceException(new ErrorCode(1,"执行接口异常"));
        }
    }
    public String tese(Test o){
        return "Hello"+o.getName();
    }
}
