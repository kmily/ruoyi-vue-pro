package cn.iocoder.yudao.module.system.api.helpcenter;

import cn.iocoder.yudao.module.system.service.helpcenter.HelpCenterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HelpCenterApiImpl implements HelpCenterApi {

    @Resource
    private HelpCenterService helpCenterService;
}
