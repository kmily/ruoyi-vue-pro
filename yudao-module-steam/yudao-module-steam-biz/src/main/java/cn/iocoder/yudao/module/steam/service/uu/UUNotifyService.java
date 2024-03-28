package cn.iocoder.yudao.module.steam.service.uu;

import cn.iocoder.yudao.module.infra.dal.dataobject.config.ConfigDO;
import cn.iocoder.yudao.module.infra.service.config.ConfigService;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyounotify.YouyouNotifyDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyounotify.YouyouNotifyMapper;
import cn.iocoder.yudao.module.steam.service.fin.UUOrderService;
import cn.iocoder.yudao.module.steam.service.uu.vo.notify.NotifyReq;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UUNotifyService {
    @Resource
    private YouyouNotifyMapper youyouNotifyMapper;
    @Resource
    private UUOrderService uuOrderService;

    private ConfigService configService;

    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void notify(NotifyReq notifyReq) {
        ConfigDO pubKey = configService.getConfigByKey("uu.pubkey");
        Map<String, Object> params = new HashMap<>();
        params.put("messageNo","1265679");
        //注意接收到的callBackInfo是含有双引号转译符"\" 文档上无法体现只需要在验证签名是直接把callBackInfo值当成字符串即可以
        params.put("callBackInfo",notifyReq.getCallBackInfo());

        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keys) {
            Object value = params.get(key);
            if (Objects.nonNull(value)) {
                stringBuilder.append(key).append(value);
            }
        }
        try {
            boolean flag = RSAUtils.verifyByPublicKey(stringBuilder.toString().getBytes(), pubKey.getValue(), notifyReq.getSign());
            log.info("stringBuilder:{}",stringBuilder);
            YouyouNotifyDO youyouNotifyDO=new YouyouNotifyDO();
            youyouNotifyDO.setMsg(notifyReq);
            youyouNotifyDO.setMessageNo(notifyReq.getMessageNo());
            youyouNotifyMapper.insert(youyouNotifyDO);
            rabbitTemplate.convertAndSend("steam","steam_notify",notifyReq);

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

    }
}
