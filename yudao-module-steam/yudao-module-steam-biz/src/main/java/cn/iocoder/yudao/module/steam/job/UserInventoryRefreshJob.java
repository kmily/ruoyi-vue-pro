package cn.iocoder.yudao.module.steam.job;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.SteamInvService;
import cn.iocoder.yudao.module.steam.service.SteamService;
import cn.iocoder.yudao.module.steam.service.selling.SellingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 定时任务-刷新用户库存,查询用户表 steam_bind_user 获取 steamId
 * CSGO固定 appid 为 730
 */
@Component
@Slf4j
//@TenantJob
public class UserInventoryRefreshJob implements JobHandler {

    @Resource
    private SteamInvService steamInvService;

    @Resource
    private BindUserMapper bindUserMapper;

    @Resource
    private InvMapper invMapper;
    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private SellingService sellingService;

    @Override
    public String execute(String param) {

        String execute = TenantUtils.execute(1l, () -> {
//
//            List<BindUserDO> bindUserDO= bindUserMapper.selectList();
//            if(bindUserDO.isEmpty()){
//                throw new ServiceException(-1,"未查询到用户");
//            }
//            if(bindUserDO.stream().map(BindUserDO::getSteamId).count() == 0){
//                throw new ServiceException(-1,"用户未绑定steam");
//            }
//            for(BindUserDO bindUser:bindUserDO ){
//
//                bindUser.getSteamId();
//            }
            List<SellingDO> sellingDOS = sellingMapper.selectList();
            if(sellingDOS.isEmpty()){
                throw new ServiceException(-1,"未查询到用户库存");
            }
            for(SellingDO sellingDO:sellingDOS){
                steamInvService.FistGetInventory(sellingDO.getId(),"730");
            }
            return "完成";
        });
        return String.format("执行关闭成功 %s 个", execute);


    }
}
