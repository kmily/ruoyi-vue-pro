package cn.iocoder.yudao.module.steam.controller.app.selling;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.InvPageReqVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.service.selling.SellingExtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Validated

public class AppSellingController {

    @Resource
    private SellingExtService sellingExtService;

    @GetMapping("/onsale")
    @Operation(summary = "饰品上架出售")
    public CommonResult<SellingDO> getOnsale(@Valid InvPageReqVo invPageReqVo) {
        // 入参：id  price
        // 查询 steam_inv
        SellingDO invPage = sellingExtService.getInvPage(invPageReqVo);
        return CommonResult.success(invPage);

    }
}

