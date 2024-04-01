package cn.iocoder.yudao.module.steam.job.uu;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodeityService;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodityDO;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodityReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.CommodityList;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoutemplate.YouyouTemplateMapper;
import cn.iocoder.yudao.module.steam.service.uu.UUService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class UUGetCommodityJob implements JobHandler {

    @Resource
    private ApiUUCommodeityService apiUUCommodeityService;
    @Resource
    private YouyouTemplateMapper youyouTemplateMapper;

    @Resource
    private UUService uuService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setApiUUCommodityService(ApiUUCommodeityService apiUUCommodeityService) {
        this.apiUUCommodeityService = apiUUCommodeityService;
    }

    @Override
    public String execute(String param) {
        Integer execute = TenantUtils.execute(1L, () -> {
            // 查询所有模板 ID
            List<Integer> allTemplateIds = getTemplateIds();

            for (Integer templateId : allTemplateIds) {
                rabbitTemplate.convertAndSend("steam", "steam_commodity", templateId);
            }
            return 1;
        });

        return String.format("执行关闭成功 %s 个", execute);
    }

    //获取模板ids
    private List<Integer> getTemplateIds() {
        //创建一个Integer类型的列表
        List<Integer> templateIds = new ArrayList<>();
        //查询出所有的YouyouTemplateDO对象，只选择template_id字段
        List<YouyouTemplateDO> youyouTemplateDOS = youyouTemplateMapper.selectList(new QueryWrapper<YouyouTemplateDO>().select("template_id"));
        //遍历所有的YouyouTemplateDO对象
        for (YouyouTemplateDO youyouTemplateDO : youyouTemplateDOS) {
            //将所有的template_id添加到templateIds列表中
            templateIds.add(youyouTemplateDO.getTemplateId());
        }
        //返回templateIds列表
        return templateIds;
    }
}