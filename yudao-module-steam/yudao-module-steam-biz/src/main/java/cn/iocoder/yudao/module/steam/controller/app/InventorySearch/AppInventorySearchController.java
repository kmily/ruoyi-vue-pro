package cn.iocoder.yudao.module.steam.controller.app.InventorySearch;


import cn.iocoder.yudao.module.steam.service.SteamInvService;
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

    @Resource
    private SteamInvService steamInvService;

    // 查询库存
    @GetMapping("/search")
    public void search(@RequestParam("id") Long id) {
        log.info("查询库存");
        steamInvService.fetchInventory(id,"730");
    }

}

