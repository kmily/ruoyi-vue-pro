package cn.iocoder.yudao.module.steam.controller.app.InventorySearch;

//import cn.iocoder.yudao.module.steam.service.SteamInvService;
import cn.iocoder.yudao.module.steam.service.SteamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 库存查询
 */
@RestController
@RequestMapping("/steam-app/inventory")
@Validated
@Slf4j
public class AppInventorySearchController {
//
//    @Resource
//    private SteamInvService steamInvService;

    @Resource
    private SteamService steamService;


    // 查询库存 TODO 切换到 SteamInvService
    @GetMapping("/search")
    public void search(@RequestParam Integer bindUserId) {
        // 用户主动获取已绑定的 steam 账号的库存
        steamService.fetchInventory("730",bindUserId);
    }

}

