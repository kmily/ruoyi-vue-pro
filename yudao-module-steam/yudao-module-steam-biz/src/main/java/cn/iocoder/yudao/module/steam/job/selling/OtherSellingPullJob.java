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
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private OtherTemplateMapper otherTemplateMapper;

    @Autowired
    public void setOtherInsertInventory(IOInvUpdateService ioInvUpdateService) {
        this.ioInvUpdateService = ioInvUpdateService;
    }

    @Override
    public String execute(String param) {
        Integer execute = TenantUtils.execute(1L, () -> {
            List<Long> allItemIds = getItemIds();
            for (Long itemId : allItemIds) {
                rabbitTemplate.convertAndSend("steam", "steam_other_selling", itemId);
            }
            return 1;
        });
        return String.format("执行关闭成功 %s 个", execute);
    }
    private List<Long> getItemIds() {
        //创建一个Integer类型的列表
        List<Long> itemIds = new ArrayList<>();
        List<OtherTemplateDO> otherTemplateDOS = otherTemplateMapper.selectList(new QueryWrapper<OtherTemplateDO>().select("item_id"));
        for (OtherTemplateDO otherTemplateDO : otherTemplateDOS) {
            itemIds.add(otherTemplateDO.getItemId());
        }
        //返回templateIds列表
        return itemIds;
    }
}
