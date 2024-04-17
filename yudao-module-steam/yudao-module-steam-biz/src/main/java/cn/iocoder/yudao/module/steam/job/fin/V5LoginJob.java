package cn.iocoder.yudao.module.steam.job.fin;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.service.fin.v5.utils.V5Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("V5LoginJob")
public class V5LoginJob implements JobHandler {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public String execute(String param) {

        Integer execute = TenantUtils.execute(1L, () -> {


            V5Login v5Login = new V5Login();
            String token = v5Login.loginV5();
            log.info("V5Token{}",token);
            Integer integer=0;
            if (token != null) {
                // 将 token 存储到 Redis 中
                redisTemplate.opsForValue().set("v5_login_token", token);
                integer++;
            }
            return integer;

        });
        return String.format("执行关闭成功 %s 个", execute);
    }
}

