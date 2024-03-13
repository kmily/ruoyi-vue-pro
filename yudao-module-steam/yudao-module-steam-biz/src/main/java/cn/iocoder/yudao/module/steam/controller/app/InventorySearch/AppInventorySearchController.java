package cn.iocoder.yudao.module.steam.controller.app.InventorySearch;


import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvMergeToSellPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.service.SteamInvService;
import cn.iocoder.yudao.module.steam.service.inv.InvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

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
    @Resource
    private InvService invService;
    @Resource
    private BindUserMapper bindUserMapper;
    @Resource
    private InvDescMapper invDescMapper;
    @Resource
    private InvMapper invMapper;


    /**
     * 用户手动查询自己的 steam_inv 库存（从数据库中获取数据）
     *
     * @param invPageReqVO steamid
     * @return
     */
    @GetMapping("/after_SearchInDB")
    @Operation(summary = "从数据库中查询数据")
    public CommonResult<PageResult<AppInvPageReqVO>> SearchInDB(@Valid InvPageReqVO invPageReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<BindUserDO> collect = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, loginUser.getId())
                .eq(BindUserDO::getUserType, loginUser.getUserType())
                .eq(BindUserDO::getSteamId, invPageReqVO.getSteamId()));
        if(Objects.isNull(collect) || collect.isEmpty()){
            throw new ServiceException(-1,"您没有权限获取该用户的库存信息");
        }
        invPageReqVO.setBindUserId(collect.get(0).getId());
        return success(steamInvService.getInvPage1(invPageReqVO));
    }


    //
    @GetMapping("/after_SearchFromSteam")
    @Operation(summary = "查询数据库的库存数据")
    public void SearchFromSteam(@RequestParam Long id) throws JsonProcessingException {
        steamInvService.FistGetInventory(id,"730");
    }


    /**
     * 入参：steamId 或者 userId
     * @param invPageReqVO
     */
    @GetMapping("/mergeToSell")
    @Operation(summary = "合并库存")
    public List<AppInvMergeToSellPageReqVO> mergeToSell(@Valid InvPageReqVO invPageReqVO) {
        PageResult<AppInvPageReqVO> invPage1 = steamInvService.getInvPage1(invPageReqVO);
        return steamInvService.merge(invPage1);
    }
}

