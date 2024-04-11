package cn.iocoder.yudao.module.steam.job.selling;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.dal.dataobject.othertemplate.OtherTemplateDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.othertemplate.OtherTemplateMapper;
import cn.iocoder.yudao.module.steam.service.ioinvupdate.IOInvUpdateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OtherSellingPullJob implements JobHandler {

    @Resource
    private IOInvUpdateService ioInvUpdateService;

    @Autowired
    public void setOtherInsertInventory(IOInvUpdateService ioInvUpdateService) {
        this.ioInvUpdateService = ioInvUpdateService;
    }

    @Override
    public String execute(String param) {

        TenantUtils.execute(1L, () ->ioInvUpdateService.otherTemplateInsert());

        TenantUtils.execute(1L, () ->ioInvUpdateService.otherTemplateInsertV5());

        return "已完成";
    }
}
