package cn.iocoder.yudao.module.steam.controller.app.InventorySearch;


import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvMergeToSellPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.InvToMergeVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.SteamInvService;
import cn.iocoder.yudao.module.steam.service.inv.InvService;
import cn.iocoder.yudao.module.steam.service.ioinvupdate.IOInvUpdateService;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private InvMapper invMapper;
    @Resource
    private SellingMapper sellingMapper;

    @Resource
    private IOInvUpdateService ioInvUpdateService;


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
        invPageReqVO.setUserId(loginUser.getId());
        invPageReqVO.setBindUserId(collect.get(0).getId());
        return success(steamInvService.getInvPage1(invPageReqVO));
    }




    /**
     * 入参：steamId 或者 userId
     *
//     * @param invToMergeVO
     */
//    @GetMapping("/mergeToSell")
//    @Operation(summary = "合并库存")
//    public CommonResult<List<AppInvPageReqVO>> mergeToSell(@Valid InvToMergeVO invToMergeVO) {
//        // 访问本地库存 按条件查询库存
//        List<InvDO> invToMerge = ioInvUpdateService.getInvToMerge(invToMergeVO);
//        // 将相同库存合并
//        List<AppInvPageReqVO> allInvToMerge = steamInvService.getAllInvToMerge(invToMerge);
//        return success(allInvToMerge);
//    }



    // =================================
    //             测试  更新库存(重复)
    // =================================
    @GetMapping("/updateFromSteam")
    @Operation(summary = "更新库存 入参steamid")
    @ResponseBody
    public CommonResult<InventoryDto> updateFromSteam(@RequestParam String steamId ,Long id) throws JsonProcessingException {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<BindUserDO> collect = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, loginUser.getId())
                .eq(BindUserDO::getUserType, loginUser.getUserType())
                .eq(BindUserDO::getSteamId, steamId));
        if(Objects.isNull(collect) || collect.isEmpty()){
            throw new ServiceException(-1,"您没有权限获取该用户的库存信息");
        }
        BindUserDO bindUserDO = new BindUserDO();
        bindUserDO.setSteamId(steamId);
        bindUserDO.setId(collect.get(0).getId());
        bindUserDO.setUserId(collect.get(0).getUserId());
        bindUserDO.setUserType(collect.get(0).getUserType());
        // 获取线上 steam 库存
        InventoryDto inventoryDto = ioInvUpdateService.gitInvFromSteam(bindUserDO);
        // 删除原有库存中，getTransferStatus = 0 的库存
        BindUserDO user = new BindUserDO();
        user.setSteamId(bindUserDO.getSteamId());
        user.setUserId(bindUserDO.getUserId());
        user.setId(bindUserDO.getId());
        ioInvUpdateService.deleteInventory(user);
        // 插入库存 TODO 后期优化思路 copy插入库存方法在插入的时候比对Selling表中相同账户下的 AssetId ，有重复就不插入
        ioInvUpdateService.firstInsertInventory(inventoryDto, bindUserDO);
        List<InvDO> invDOS = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .eq(InvDO::getSteamId, steamId)
                .eq(InvDO::getBindUserId, bindUserDO.getId())
                .eq(InvDO::getUserId, bindUserDO.getUserId())
                .eq(InvDO::getTransferStatus, 1));
        if (invDOS.isEmpty()){
            return success(inventoryDto);
        }
        for(InvDO invDO : invDOS){
            invMapper.delete(new LambdaQueryWrapperX<InvDO>().eq(InvDO::getAssetid,invDO.getAssetid()).eq(InvDO::getTransferStatus,0));
        }

        return success(inventoryDto);
    }
}

